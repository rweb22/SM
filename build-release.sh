#!/bin/bash
# Build script for Samrat Satta Android App
# This script ensures the production keystore is in place before building

set -e  # Exit on error

echo "============================================"
echo "Samrat Satta - Release APK Build Script"
echo "============================================"
echo ""

# Check if we're in the SM directory
if [ ! -f "build.gradle" ]; then
    echo "ERROR: build.gradle not found. Please run this script from the SM directory."
    exit 1
fi

# Step 1: Verify/Decode the keystore
echo "[1/5] Checking production keystore..."
if [ ! -f "app/samrat-release.keystore" ]; then
    if [ -f "samrat-release.keystore.b64" ]; then
        echo "  → Decoding keystore from base64..."
        base64 -d samrat-release.keystore.b64 > app/samrat-release.keystore
        echo "  ✓ Keystore decoded successfully"
    else
        echo "  ERROR: samrat-release.keystore.b64 not found!"
        exit 1
    fi
else
    echo "  ✓ Keystore already exists"
fi

# Step 2: Verify keystore
echo ""
echo "[2/5] Verifying keystore..."
keytool -list -keystore app/samrat-release.keystore \
    -storepass samrat777secure \
    -alias samrat777-key 2>&1 | grep -q "samrat777-key" && \
    echo "  ✓ Keystore verified (alias: samrat777-key)" || \
    (echo "  ERROR: Keystore verification failed!" && exit 1)

# Step 3: Check Java version
echo ""
echo "[3/5] Checking Java version..."
JAVA_VERSION=$(java -version 2>&1 | head -1 | awk -F '"' '{print $2}' | cut -d'.' -f1)
echo "  → Detected Java version: $JAVA_VERSION"

if [ "$JAVA_VERSION" -gt "17" ]; then
    echo "  ⚠️  WARNING: Gradle 7.2 is not compatible with Java $JAVA_VERSION"
    echo "  ℹ️  Recommendation: Use Java 11 or 17"
    echo ""
    echo "  To install Java 17:"
    echo "    sudo apt install openjdk-17-jdk"
    echo ""
    echo "  To switch to Java 17 (if already installed):"
    echo "    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64"
    echo "    export PATH=\$JAVA_HOME/bin:\$PATH"
    echo ""
    read -p "Continue anyway? (y/N) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# Step 4: Clean build
echo ""
echo "[4/5] Cleaning previous builds..."
./gradlew clean || (echo "  ERROR: Clean failed!" && exit 1)
echo "  ✓ Clean successful"

# Step 5: Build release APK
echo ""
echo "[5/5] Building release APK..."
./gradlew assembleRelease --stacktrace
BUILD_STATUS=$?

if [ $BUILD_STATUS -eq 0 ]; then
    echo ""
    echo "============================================"
    echo "✅ BUILD SUCCESSFUL!"
    echo "============================================"
    echo ""
    
    # Find the APK
    APK_PATH=$(find app/build/outputs/apk/release -name "*.apk" | head -1)
    
    if [ -n "$APK_PATH" ]; then
        echo "📦 Release APK created:"
        echo "   $APK_PATH"
        echo ""
        
        # Show APK details
        echo "APK Details:"
        ls -lh "$APK_PATH"
        echo ""
        
        # Verify signature
        echo "Verifying signature..."
        unzip -p "$APK_PATH" META-INF/CERT.RSA 2>/dev/null | keytool -printcert 2>&1 | grep -A2 "SHA1:" | head -3
        echo ""
        
        echo "Expected SHA1: A5:49:F6:38:E9:0B:EF:78:4A:CE:7A:D0:FC:70:1E:C5:3C:93:6D:AD"
        echo ""
        echo "Next steps:"
        echo "  1. Test the APK on a device"
        echo "  2. Upload to production server:"
        echo "     scp $APK_PATH root@139.59.58.67:/root/Matka/static/download/samrat.apk"
    else
        echo "  ⚠️  Warning: APK file not found in expected location"
    fi
else
    echo ""
    echo "============================================"
    echo "❌ BUILD FAILED!"
    echo "============================================"
    echo ""
    echo "Common issues:"
    echo "  • Java version incompatibility (need Java 11 or 17 for Gradle 7.2)"
    echo "  • Missing Android SDK components"
    echo "  • Keystore not found or incorrect password"
    echo ""
    exit $BUILD_STATUS
fi
