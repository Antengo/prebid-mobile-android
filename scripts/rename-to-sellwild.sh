#!/bin/bash
# Namespace shading script for Prebid Mobile Android
# Renames org.prebid.mobile -> com.sellwild.prebid
# Renames PrebidMobile class -> SellwildPrebid

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(dirname "$SCRIPT_DIR")"
cd "$ROOT_DIR"

echo "Starting namespace shading: org.prebid.mobile -> com.sellwild.prebid"

# Step 1: Rename package declarations and imports in all source files
echo "Step 1: Renaming package declarations and imports..."
find . -type f \( -name "*.java" -o -name "*.kt" \) -exec sed -i '' \
    -e 's/org\.prebid\.mobile/com.sellwild.prebid/g' \
    {} +

# Step 2: Update path references (for resources, etc.)
echo "Step 2: Updating path references..."
find . -type f \( -name "*.java" -o -name "*.kt" -o -name "*.xml" \) -exec sed -i '' \
    -e 's|org/prebid/mobile|com/sellwild/prebid|g' \
    {} +

# Step 3: Update Gradle files (namespaces, artifact groups)
echo "Step 3: Updating Gradle configuration..."
find . -type f -name "*.gradle" -exec sed -i '' \
    -e 's/org\.prebid\.mobile/com.sellwild.prebid/g' \
    -e 's/"org\.prebid"/"com.sellwild"/g' \
    {} +

# Step 4: Update XML files (AndroidManifest, layouts, etc.)
echo "Step 4: Updating XML files..."
find . -type f -name "*.xml" -exec sed -i '' \
    -e 's/org\.prebid\.mobile/com.sellwild.prebid/g' \
    {} +

# Step 5: Update ProGuard rules
echo "Step 5: Updating ProGuard rules..."
find . -type f -name "*.pro" -exec sed -i '' \
    -e 's/org\.prebid\.mobile/com.sellwild.prebid/g' \
    {} +

# Step 6: Rename main class PrebidMobile -> SellwildPrebid in file contents
echo "Step 6: Renaming class PrebidMobile -> SellwildPrebid in source..."
find . -type f \( -name "*.java" -o -name "*.kt" \) -exec sed -i '' \
    -e 's/PrebidMobile/SellwildPrebid/g' \
    {} +

# Step 7: Move source files from org/prebid/mobile to com/sellwild/prebid
echo "Step 7: Moving source directories..."
for srcdir in $(find . -type d -name "java" -path "*/src/*"); do
    if [ -d "$srcdir/org/prebid/mobile" ]; then
        mkdir -p "$srcdir/com/sellwild/prebid"
        # Move contents (not the directory itself)
        if [ "$(ls -A "$srcdir/org/prebid/mobile" 2>/dev/null)" ]; then
            cp -R "$srcdir/org/prebid/mobile/"* "$srcdir/com/sellwild/prebid/"
            rm -rf "$srcdir/org"
        fi
    fi
done

# Step 8: Rename Java/Kotlin files that have PrebidMobile in the name
echo "Step 8: Renaming files with PrebidMobile in the name..."
find . -type f \( -name "*PrebidMobile*.java" -o -name "*PrebidMobile*.kt" \) | while read f; do
    dir=$(dirname "$f")
    base=$(basename "$f")
    newbase=$(echo "$base" | sed 's/PrebidMobile/SellwildPrebid/g')
    if [ "$base" != "$newbase" ]; then
        mv "$f" "$dir/$newbase"
        echo "  Renamed: $base -> $newbase"
    fi
done

echo ""
echo "Namespace shading complete!"
echo "Files processed: $(find . -type f \( -name "*.java" -o -name "*.kt" -o -name "*.gradle" -o -name "*.xml" \) | wc -l | tr -d ' ')"
