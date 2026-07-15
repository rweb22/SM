# Keystore Migration Summary

**Date:** June 6, 2026  
**Issue:** Users unable to install/update the app - "App not installed" / "Package conflicts"

---

## Root Cause Analysis

### The Problem

Users who installed earlier versions of the app (May 2024 - April 2026) cannot install newer versions, even after uninstalling.

### Investigation Findings

1. **Old APKs (May 2024 - Apr 2026):**
   - Signed with: **Android Debug Certificate**
   - SHA1: `BA:73:1C:CA:F0:2A:97:18:C4:C0:5B:C4:2C:2A:E7:E7:C4:66:70:9E`
   - Owner: `C=US, O=Android, CN=Android Debug`
   - Created: May 01, 2024
   - **This keystore is LOST** (not in repository, not on server)

2. **Current Production APK (May 31, 2026):**
   - Signed with: **Production Release Certificate**
   - SHA1: `A5:49:F6:38:E9:0B:EF:78:4A:CE:7A:D0:FC:70:1E:C5:3C:93:6D:AD`
   - Owner: `CN=Samrat777, OU=Development, O=Samrat777`
   - Valid: Apr 15, 2026 → Aug 31, 2053

3. **Why Users Can't Install:**
   - Android **REFUSES** to install an APK with a different signature over an existing app
   - Even after uninstall, Android caches signing information
   - The two certificates are completely different → Installation blocked

---

## Actions Taken

### 1. ✅ Production Keystore Setup

**Location:** `SM/app/samrat-release.keystore`  
**Source:** Decoded from `samrat-release.keystore.b64`  
**Configuration:** Already correct in `app/build.gradle`

```gradle
signingConfigs {
    release {
        storeFile file('samrat-release.keystore')
        storePassword 'samrat777secure'
        keyAlias 'samrat777-key'
        keyPassword 'samrat777secure'
    }
}
```

### 2. ✅ Updated `.gitignore`

Added rules to prevent committing raw keystore files:
```
# Keystore files (keep base64 version only)
*.keystore
!samrat-release.keystore.b64
```

### 3. ✅ Created Documentation

- **`KEYSTORE_SETUP.md`** - Complete keystore setup guide
- **`build-release.sh`** - Automated build script with verification
- **`KEYSTORE_MIGRATION_SUMMARY.md`** - This file

### 4. ✅ Verified Keystore

```bash
$ keytool -list -v -keystore app/samrat-release.keystore \
    -storepass samrat777secure -alias samrat777-key

Alias name: samrat777-key
Valid from: Wed Apr 15 08:13:25 IST 2026 until: Sun Aug 31 08:13:25 IST 2053
Certificate fingerprints:
   SHA1: A5:49:F6:38:E9:0B:EF:78:4A:CE:7A:D0:FC:70:1E:C5:3C:93:6D:AD
   SHA256: B8:71:87:AC:91:62:34:5C:92:5F:0E:4B:E9:ED:27:33:8B:BA:92:1C:C0:A5:39:33:B3:A4:27:1A:7C:E6:60:BF
```

✅ **This matches the current production APK signature!**

---

## Going Forward

### For New Builds

All future APKs **MUST** be signed with the production keystore to ensure users can update smoothly.

#### Method 1: GitHub Actions (Recommended)

Push to the `v2` or `main` branch - the workflow automatically:
1. Decodes the keystore from GitHub secrets
2. Builds with Java 17 (compatible with Gradle 7.2)
3. Signs with production certificate
4. Uploads APK as artifact

#### Method 2: Local Build

```bash
cd SM
./build-release.sh
```

**Requirements:**
- Java 17 (Gradle 7.2 doesn't support Java 21)
- Android SDK with API 31 and build-tools 30.0.3

**Install Java 17:**
```bash
sudo apt install openjdk-17-jdk
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

### For Affected Users

Users who have the debug-signed APK installed **MUST** do a clean install:

#### User Instructions

**Option A: Manual Uninstall (Recommended)**

1. Open **Settings** → **Apps**
2. Find and tap **Samrat Satta**  
3. Tap **Uninstall**
4. Wait for confirmation
5. **Reboot device** (optional but recommended)
6. Download and install the new APK

**Option B: ADB Uninstall (Advanced)**

```bash
adb shell pm uninstall com.samrat.satta
adb install samrat.apk
```

#### Communication Plan

1. **Website Notice:**
   - Add prominent banner on samrat-satta.com
   - Explain the update process clearly

2. **WhatsApp Broadcast:**
   - Send message to user groups with step-by-step instructions
   - Include screenshots if possible

3. **In-App Notification** (if feasible):
   - Show alert on app launch (if old version still works)
   - Direct users to uninstall and reinstall

---

## Prevention Checklist

To avoid this issue in the future:

- [ ] **ALWAYS** use the production keystore for releases
- [ ] **NEVER** lose the `samrat-release.keystore.b64` file
- [ ] Backup keystore in multiple secure locations:
  - [ ] Git repository (as base64)
  - [ ] Production server
  - [ ] Secure cloud storage
  - [ ] Encrypted USB drive
- [ ] Verify APK signature before deployment
- [ ] Test update process before releasing to users

---

## Files Modified/Created

### Modified
- `SM/.gitignore` - Added keystore exclusion rules

### Created
- `SM/app/samrat-release.keystore` - Production keystore (decoded from base64)
- `SM/KEYSTORE_SETUP.md` - Keystore setup documentation
- `SM/build-release.sh` - Automated build script
- `SM/KEYSTORE_MIGRATION_SUMMARY.md` - This summary

### Unchanged (already correct)
- `SM/app/build.gradle` - Signing configuration
- `SM/samrat-release.keystore.b64` - Base64-encoded production keystore
- `SM/.github/workflows/build-apk.yml` - GitHub Actions workflow

---

## Next Steps

1. **Immediate:**
   - [ ] Test build with Java 17 or via GitHub Actions
   - [ ] Verify APK signature matches production certificate
   - [ ] Prepare user communication materials

2. **Short-term:**
   - [ ] Update website with installation instructions
   - [ ] Send WhatsApp broadcast to users
   - [ ] Monitor user feedback and support requests

3. **Long-term:**
   - [ ] Document the standard release process
   - [ ] Set up automated testing for signature verification
   - [ ] Consider Play Store distribution to avoid manual APK issues

---

**Status:** ✅ **Keystore setup complete and verified**  
**Risk:** ⚠️ **Some users will need to manually uninstall before updating**  
**Mitigation:** Clear communication and support for affected users

