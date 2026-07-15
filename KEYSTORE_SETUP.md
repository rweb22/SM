# Keystore Setup Guide

## Production Keystore Configuration

This document describes the keystore setup for signing the Samrat Satta Android app.

---

## Current Production Keystore

**File:** `samrat-release.keystore` (stored as `samrat-release.keystore.b64` in git)  
**Location:** `SM/app/samrat-release.keystore` (generated from base64 during build)  
**Certificate Details:**
- **Owner:** CN=Samrat777, OU=Development, O=Samrat777, L=Delhi, ST=Delhi, C=IN
- **Alias:** samrat777-key
- **Password:** samrat777secure (store & key password are the same)
- **Valid:** Apr 15, 2026 → Aug 31, 2053
- **SHA1:** A5:49:F6:38:E9:0B:EF:78:4A:CE:7A:D0:FC:70:1E:C5:3C:93:6D:AD
- **SHA256:** B8:71:87:AC:91:62:34:5C:92:5F:0E:4B:E9:ED:27:33:8B:BA:92:1C:C0:A5:39:33:B3:A4:27:1A:7C:E6:60:BF

---

## Setup Instructions

### Option 1: Automatic Setup (Recommended)

The keystore is automatically decoded from the base64 version during build:

```bash
cd SM
base64 -d samrat-release.keystore.b64 > app/samrat-release.keystore
```

### Option 2: Manual Setup

If you need to set it up manually:

1. Navigate to the SM directory
2. Decode the base64 keystore:
   ```bash
   base64 -d samrat-release.keystore.b64 > app/samrat-release.keystore
   ```
3. Verify the keystore:
   ```bash
   keytool -list -v -keystore app/samrat-release.keystore \
     -storepass samrat777secure -alias samrat777-key
   ```

---

## Building a Release APK

### Using Gradle

```bash
cd SM
./gradlew assembleRelease
```

The signed APK will be at: `app/build/outputs/apk/release/app-release.apk`

### Environment Variables (Optional)

You can override the default keystore credentials using environment variables:

```bash
export KEYSTORE_PASSWORD="samrat777secure"
export KEY_ALIAS="samrat777-key"
export KEY_PASSWORD="samrat777secure"
./gradlew assembleRelease
```

---

## Verifying APK Signature

To verify an APK is signed with the correct keystore:

```bash
# Extract certificate from APK
unzip -p app-release.apk META-INF/CERT.RSA | keytool -printcert

# Check the SHA1 fingerprint matches:
# SHA1: A5:49:F6:38:E9:0B:EF:78:4A:CE:7A:D0:FC:70:1E:C5:3C:93:6D:AD
```

---

## Important Notes

### ⚠️ CRITICAL: Never Lose This Keystore!

- **Backup the `samrat-release.keystore.b64` file** in multiple secure locations
- If lost, you CANNOT update the app on user devices
- Users would have to uninstall and reinstall (losing all data)

### Git Configuration

- The actual `.keystore` files are in `.gitignore`
- Only the base64-encoded version (`samrat-release.keystore.b64`) is committed
- This prevents accidentally exposing the raw keystore

### Password Security

- The default password is hardcoded in `app/build.gradle`
- For production CI/CD, use environment variables instead
- Never commit password changes to public repositories

---

## Historical Context

### Why Some Users Can't Update

**Problem:** Users who installed APKs signed with the old debug certificate cannot update to APKs signed with the production certificate.

**Old Debug Certificate (May 2024 - Apr 2026):**
- SHA1: BA:73:1C:CA:F0:2A:97:18:C4:C0:5B:C4:2C:2A:E7:E7:C4:66:70:9E
- Owner: C=US, O=Android, CN=Android Debug

**Current Production Certificate (May 2026+):**
- SHA1: A5:49:F6:38:E9:0B:EF:78:4A:CE:7A:D0:FC:70:1E:C5:3C:93:6D:AD
- Owner: CN=Samrat777, OU=Development, O=Samrat777

**Solution for Affected Users:**
Users must completely uninstall the old app before installing the new version.

---

## Deployment Checklist

Before releasing a new APK:

- [ ] Verify keystore is in `SM/app/samrat-release.keystore`
- [ ] Build release APK: `./gradlew assembleRelease`
- [ ] Verify APK signature matches production certificate (SHA1: A5:49...)
- [ ] Test installation on a clean device
- [ ] Test update on device with previous production APK
- [ ] Upload to production server
- [ ] Update version code/name in `app/build.gradle`

---

## Troubleshooting

### "App not installed" Error

**Cause:** Signature mismatch between installed app and new APK

**Solution:** User must uninstall old app completely:
1. Settings → Apps → Samrat Satta → Uninstall
2. Reboot device (optional but recommended)
3. Install new APK

### Keystore Not Found During Build

**Error:** `Execution failed for task ':app:packageRelease'`

**Solution:**
```bash
cd SM
base64 -d samrat-release.keystore.b64 > app/samrat-release.keystore
```

### Wrong Password

**Error:** `keystore password was incorrect`

**Solution:** The correct password is `samrat777secure` for both store and key.

---

**Last Updated:** June 6, 2026
