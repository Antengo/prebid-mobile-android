# Sellwild Prebid Mobile Android Fork — Maintenance Guide

## Why This Fork Exists

Prebid Mobile SDK uses a singleton pattern (`PrebidMobile.shared`) that's global to the entire app. When a partner app (like WeatherBug) already uses Prebid Mobile for their own ads with a different Prebid Server endpoint, integrating the Sellwild SDK would overwrite their configuration — breaking their ads.

This fork applies **namespace shading**: we rename the package from `org.prebid.mobile` to `com.sellwild.prebid` and the main class from `PrebidMobile` to `SellwildPrebid`. This allows both singletons to coexist independently:

- **Partner's Prebid**: `org.prebid.mobile.PrebidMobile.shared` → their server
- **Sellwild's Prebid**: `com.sellwild.prebid.SellwildPrebid.shared` → `prebid.sellwild.com`

## Current State

| Item | Value |
|------|-------|
| Upstream | `prebid/prebid-mobile-android` |
| Fork | `Antengo/sellwild-prebid-mobile-android` |
| Base version | 3.3.2 (upstream tag) |
| Shaded package | `com.sellwild.prebid` |
| Main class | `SellwildPrebid` |
| GMA version | Pinned to 23.6.0 (Kotlin 2.1 compatible) |

## What Was Changed

1. **Package namespace**: `org.prebid.mobile.*` → `com.sellwild.prebid.*`
2. **Main class**: `PrebidMobile` → `SellwildPrebid`
3. **Module namespaces**: Updated in all `build.gradle` files
4. **Maven coordinates**: `groupId: com.sellwild` (was `org.prebid`)
5. **GMA version**: Pinned to 23.6.0 in `libs.versions.toml` (was `+`)
6. **Maven publishing**: Added `publishing {}` block to `android.gradle` with POM fixups

## How to Use in SellwildSDK

### Local Development (Maven Local)

1. Build and publish to local Maven:
   ```bash
   cd sellwild-prebid-mobile-android
   ./gradlew publishToMavenLocal
   ```

2. Add `mavenLocal()` to your `settings.gradle.kts`:
   ```kotlin
   repositories {
       google()
       mavenCentral()
       mavenLocal()  // Add this
   }
   ```

3. Depend on the shaded artifacts:
   ```kotlin
   implementation("com.sellwild:PrebidMobile-core:3.3.2")
   implementation("com.sellwild:PrebidMobile-gamEventHandlers:3.3.2")
   ```

### Production (JitPack)

Once the fork is public or JitPack has access:

```kotlin
// settings.gradle.kts
maven { url = uri("https://jitpack.io") }

// build.gradle.kts
implementation("com.github.Antengo.prebid-mobile-android:PrebidMobile-core:master-SNAPSHOT")
implementation("com.github.Antengo.prebid-mobile-android:PrebidMobile-gamEventHandlers:master-SNAPSHOT")
```

## Updating to a New Upstream Version

When Prebid Mobile releases a new version:

### 1. Add upstream remote (first time only)
```bash
cd sellwild-prebid-mobile-android
git remote add upstream https://github.com/prebid/prebid-mobile-android.git
```

### 2. Fetch and merge upstream
```bash
git fetch upstream
git checkout master
git merge upstream/master  # or specific tag like v3.4.0
```

### 3. Re-apply namespace shading

The rename script handles this:
```bash
./scripts/rename-to-sellwild.sh
```

Or manually run these sed commands across the codebase:
```bash
# Package references in Java/Kotlin
find . -type f \( -name "*.java" -o -name "*.kt" \) -exec sed -i '' \
  -e 's/org\.prebid\.mobile/com.sellwild.prebid/g' \
  -e 's/PrebidMobile/SellwildPrebid/g' {} +

# Gradle files
find . -type f -name "*.gradle*" -exec sed -i '' \
  -e 's/org\.prebid\.mobile/com.sellwild.prebid/g' \
  -e 's/org\.prebid/com.sellwild/g' {} +

# XML files (AndroidManifest, resources)
find . -type f -name "*.xml" -exec sed -i '' \
  -e 's/org\.prebid\.mobile/com.sellwild.prebid/g' {} +

# Move source directories
# (may need manual adjustment for new files)
```

### 4. Verify GMA version is still pinned
```bash
grep "google-play-services-ads" gradle/libs.versions.toml
# Should show "23.6.0", not "+"
```

### 5. Build and test
```bash
./gradlew clean assembleRelease
./gradlew publishToMavenLocal

# Test in SellwildSDK
cd ../sellwild-sdk/android
./gradlew assembleRelease

# Test in demo app
cd ../samples/feed-demo-android
./gradlew assembleDebug
```

### 6. Commit and push
```bash
git add -A
git commit -m "chore: update to Prebid Mobile X.Y.Z with namespace shading"
git push origin master
```

## Troubleshooting

### Build fails with "unresolved reference: SellwildPrebid"
The rename script missed some files. Search for remaining `PrebidMobile` references:
```bash
grep -r "PrebidMobile" --include="*.java" --include="*.kt" .
```

### Maven dependency resolution fails
Check the POM files have correct coordinates:
```bash
cat ~/.m2/repository/com/sellwild/PrebidMobile-core/3.3.2/PrebidMobile-core-3.3.2.pom
```

The `groupId` should be `com.sellwild`, not `sellwild-prebid-mobile-android`.

### "Module was compiled with incompatible Kotlin version"
GMA version is pulling in a newer version compiled with Kotlin 2.3. Ensure:
1. `libs.versions.toml` has `google-play-services-ads = "23.6.0"` (not `+`)
2. Republish to Maven Local after changing

### omsdk-android dependency not found
Publish it separately:
```bash
./gradlew :omsdk-android:publishToMavenLocal
```

## Files Modified from Upstream

Key files that have Sellwild-specific changes:

- `PrebidMobile/android.gradle` — Maven publishing config with POM fixups
- `PrebidMobile/omsdk-android/build.gradle` — Maven publishing for vendored AAR
- `gradle/libs.versions.toml` — GMA version pinned to 23.6.0
- `build.gradle` — `artifactGroupId = "com.sellwild"`
- All source files — package/class renames

## Testing Checklist

Before releasing an update:

- [ ] `./gradlew assembleRelease` succeeds with 0 errors
- [ ] `./gradlew publishToMavenLocal` publishes all artifacts
- [ ] SellwildSDK Android builds with `./gradlew assembleRelease`
- [ ] Demo app builds with `./gradlew assembleDebug`
- [ ] APK contains `com/sellwild/prebid/SellwildPrebid` (not `org/prebid/mobile`)
- [ ] APK does NOT contain `org/prebid/mobile/PrebidMobile`
- [ ] Demo app runs on emulator without crashes
