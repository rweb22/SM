# Samrat App Release Process

This document describes how to ship a new Android build from code change to
production rollout, including the dev-only test channel and how reverts work.

Repositories:
- App: `SM` — push to `origin` (`git@github.com:rweb22/SM.git`) branch `main`; CI builds the APK.
- Backend: `Matka` — push to `origin` (`git@github.com:DeepakDhaka201/Matka.git`) branch `delhi`; deployed on `ssh samrat` at `:8006` (`/root/Matka`).

---

## 1. Versioning model

Two version fields in `SM/app/build.gradle`:

| Field | Example | Meaning |
|---|---|---|
| `versionCode` | `10`, `11`, `12` | Always-incrementing counter. This is what Android's PackageInstaller uses for upgrade checks. **Must never go backwards.** |
| `versionName` | `1.0.4`, `1.0.5` | Display-only metadata. **May go backwards.** |

### Reverts

Because Android refuses to install an APK with a lower `versionCode` over a
higher one (`INSTALL_FAILED_VERSION_DOWNGRADE`), a true in-place downgrade is
impossible for signed production builds.

To revert to older behavior: ship a **new** APK with a **higher** `versionCode`
but an **older** `versionName`, e.g. `(14, 1.0.4)`. Every device happily installs
it (counter climbs), and the UI just shows the older version name.

Rules of thumb:
- The counter always climbs; `versionCode` for a revert must be `currentMax + 1`.
- Update checks compare `latest_version` (the counter) against the installed `versionCode`.
- The version name is never used in upgrade decisions.

---

## 2. Release flow (dev channel → stable)

### Step 1 — Code, bump version, push, CI builds

1. Make code changes in `SM/`.
2. Bump `versionCode` and `versionName` in `SM/app/build.gradle`.
3. Commit and push to `origin main`:
   ```bash
   cd SM
   git add -A
   git commit -m "..."
   git push origin main
   ```
4. GitHub Actions runs `.github/workflows/build-apk.yml` (JDK 17, release keystore
   injected via the `KEYSTORE_BASE64` secret) and produces the signed APK:
   ```bash
   gh run watch <run-id> --repo rweb22/SM --exit-status
   gh run download <run-id> --repo rweb22/SM
   # artifact is at samrat-satta-release-apk/app-release.apk
   ```

### Step 2 — Upload the APK to the server

Upload under a **distinct filename** so the dev build never overwrites the
currently served production `samrat.apk`:

```bash
scp samrat-satta-release-apk/app-release.apk samrat:/tmp/samrat-v14.apk
ssh samrat "cp /tmp/samrat-v14.apk /root/Matka/static/download/samrat-v1.0.8.apk"
```

The public URL is `https://samrat-satta.com/admin/static/download/<filename>`.
Keep the distinct name until the build is promoted; users on the existing stable
link still get the old APK while you test.

### Step 3 — Add the update row (admin UI)

Open `https://samrat-satta.com/admin/app_updates` → **Push new update**
(or go directly to `/admin/add_app_update`) and fill the form:

| Field | Value for a dev test |
|---|---|
| Version code (counter) | `14` (must be higher than current max) |
| Version name | `1.0.8` |
| Channel | **`dev`** |
| APK Link | `https://samrat-satta.com/admin/static/download/samrat-v1.0.8.apk` |
| Release notes | what changed |

Submit POSTs JSON to `/admin/api/add_app_update` (`Matka/api/admin.py`), which
inserts a row into the `app_update` table.

### Step 4 — Test on the dev account

- The dev account mobile is stored in the `setting` table as `DEV_PHONE`
  (currently `9649617995`).
- `/get_config` (`Matka/api/login.py`) compares the requesting app's `mobile`
  param against `DEV_PHONE`, normalizing both sides so `+91`/`91` prefixes are
  ignored. A match makes the response include `dev_latest_version` /
  `dev_update_link` / `dev_update_log` / `dev_update_version_name`.
- Reopen the app logged in as the dev account: splash (`splash.java`) sees
  `dev_latest_version > VERSION_CODE` and shows the update immediately —
  **dev updates bypass the 2-day throttle** (by design, so you always get the
  newest dev build; the prompt re-appears on every launch until you update).
- Regular users receive no dev fields, so they see nothing.

### Step 5 — Promote to stable

Once the build is verified, flip the row to `stable` (currently a direct DB
UPDATE; the admin UI has Add/Delete only — no Promote button yet):

```bash
ssh samrat
mysql -uroot -pspaceback3423 samrat -e "UPDATE app_update SET channel='stable', version_name='1.0.8' WHERE id=8;"
```

Now every user's app sees `latest_version` (the counter) and shows the update
prompt, throttled to once every 2 days.

### Step 6 — Update the canonical APK

The website download page and older update rows link to the plain
`samrat.apk`. Back it up, then replace it with the new build, and keep the
repo copy in sync:

```bash
ssh samrat "cd /root/Matka/static/download && cp samrat.apk samrat.apk.bak-$(date +%Y%m%d-%H%M) && cp /tmp/samrat-v14.apk samrat.apk && md5sum samrat.apk"
curl -s "https://samrat-satta.com/admin/static/download/samrat.apk" | md5sum   # should match CI md5
```

```bash
cd Matka && cp /path/to/app-release.apk static/download/samrat.apk
git add static/download/samrat.apk && git commit -m "chore: promote vX.Y.Z as production APK" && git push origin delhi
```

---

## 3. Deploying backend changes

1. Commit + push `Matka` to `origin delhi`.
2. On the server:
   ```bash
   ssh samrat
   cd /root/Matka
   git stash push -u -m "pre-deploy"   # only if local files conflict (e.g. samrat.apk, backups)
   git pull origin delhi
   # restore any stash files you need, then drop the stash
   PID=$(lsof -t -i :8006); kill $PID; sleep 2
   cd /root/Matka && nohup python3 app.py > /tmp/matka.log 2>&1 &
   sleep 6; lsof -i :8006 | grep LISTEN
   curl -s http://localhost:8006/get_config
   ```
3. Note: the prod database is `samrat` (hardcoded in `app.py:38` as
   `mysql://root:spaceback3423@localhost:3306/samrat`), **not** `samrat2`.

### DB schema for the update system

The `app_update` table has (beyond the original `id/version/link/log/created_at`):
- `version_name VARCHAR(20)` — display version, may go backwards.
- `channel VARCHAR(20) NOT NULL DEFAULT 'stable'` — `stable` (everyone) or `dev` (dev account only).

Migration (already applied to prod):
```sql
ALTER TABLE app_update ADD COLUMN version_name VARCHAR(20) NULL AFTER version;
ALTER TABLE app_update ADD COLUMN channel VARCHAR(20) NOT NULL DEFAULT 'stable' AFTER version_name;
```

The `DEV_PHONE` key lives in the `setting` table and is editable from the admin
Settings page.

---

## 4. Gotchas

- **Old apps can't see dev builds.** Apps built before the dev-channel logic
  (e.g. v1.0.6) only check the **stable** `latest_version`. If their installed
  code is already at or above stable's counter, they get no prompt at all. To
  get such a device onto a dev build, install the APK URL manually; otherwise
  promote to stable.
- **Dev updates bypass the throttle**, so the prompt re-appears on every launch
  until the dev updates (by design).
- **The counter must always increase.** Even for reverts. Never lower
  `versionCode`.
- **Admin UI has no Promote button** — promoting dev→stable is a direct DB
  UPDATE for now.
- Keep the distinct dev APK filename until promotion; the stable link
  (`samrat.apk`) must point at the promoted build.
