# GitHub Actions CI/CD Pipeline

This directory contains automated workflows for the Enhanced Timer HUD Minecraft mod.

## 🔧 **Workflows Overview**

### 1. `build-test.yml` - Continuous Integration
- **Triggers**: Push to `main`/`develop`, Pull Requests to `main`
- **Purpose**: Validates code changes, runs tests, builds JARs
- **Environment**: Ubuntu Latest, Java 21 (Temurin), Gradle 8.6
- **Outputs**: Test results, development JARs

### 2. `build-release.yml` - Release Automation
- **Triggers**: Git tags (`v*`), Manual dispatch
- **Purpose**: Creates GitHub releases with built JARs
- **Features**: 
  - Auto-detects prerelease based on tag (`-dev`, `-alpha`, `-beta`)
  - Generates comprehensive release notes
  - Attaches both main and sources JARs
- **Outputs**: GitHub release, release artifacts

### 3. `publish-platforms.yml` - Platform Publishing
- **Triggers**: GitHub release published
- **Purpose**: Publishes to CurseForge and Modrinth (requires setup)
- **Notes**: Template ready, requires project IDs and API tokens

### 4. `test-ci.yml` - Manual Testing
- **Triggers**: Manual workflow dispatch
- **Purpose**: Test CI pipeline without creating releases
- **Features**: Debug mode, build validation, JAR integrity testing

## 🚀 **Environment Configuration**

Our GitHub Actions are configured to match the local development environment:

```yaml
Environment:
  OS: ubuntu-latest
  Java: 21 (Temurin Distribution) 
  Gradle: 8.6 (matches local wrapper)
  Minecraft: 1.21.1
  Fabric Loader: 0.18.4
  Fabric API: 0.116.7+1.21.1
```

## 📋 **Build Commands**

All workflows use the same commands as local development:

```bash
# Test execution
./gradlew test --no-daemon --info

# Build with all artifacts
./gradlew build --no-daemon --info
```

## 🏷️ **Release Process**

### Automated Release (Recommended):
1. **Update version** in `gradle.properties`:
   ```properties
   mod_version = 0.2.7-dev  # or 0.3.0 for stable
   ```

2. **Commit and tag**:
   ```bash
   git add gradle.properties
   git commit -m "bump version to 0.2.7-dev"
   git tag v0.2.7-dev
   git push origin main --tags
   ```

3. **Automatic actions**:
   - `build-test.yml` validates the build
   - `build-release.yml` creates GitHub release
   - `publish-platforms.yml` publishes to mod platforms (if configured)

### Manual Release:
1. Go to **Actions** → **Build and Release**
2. Click **Run workflow**
3. Specify branch/tag to release

## 🔍 **Monitoring & Debugging**

### View Build Status:
- **Main builds**: Check the "Actions" tab in GitHub
- **Test failures**: Download test artifacts for detailed reports
- **JAR validation**: All workflows verify JAR integrity

### Debug Failed Builds:
1. **Use test-ci.yml** with debug mode enabled
2. **Check logs** for specific error details:
   - Java/Gradle version mismatches
   - Dependency resolution issues
   - Test failures
3. **Download artifacts** to inspect generated files

### Common Issues:

| Issue | Solution |
|-------|----------|
| "Gradle wrapper not executable" | Fixed by wrapper-validation action |
| "Java version mismatch" | Using explicit Java 21 setup |
| "Tests fail on CI but pass locally" | Using `--info` flag for detailed output |
| "JAR missing fabric.mod.json" | Build verification catches this |

## 🔧 **Platform Publishing Setup**

To enable automatic publishing to CurseForge and Modrinth:

### 1. CurseForge Setup:
```bash
# Add secrets in GitHub Settings → Secrets and variables → Actions:
CURSEFORGE_TOKEN=your_api_token_here
```

Edit `publish-platforms.yml`:
```yaml
curseforge-id: YOUR_PROJECT_ID  # Replace with actual project ID
```

### 2. Modrinth Setup:
```bash
# Add secrets:
MODRINTH_TOKEN=your_api_token_here
```

Edit `publish-platforms.yml`:
```yaml
modrinth-id: your-project-slug  # Replace with actual project slug
```

## 📊 **Artifact Retention**

- **Test results**: 7 days
- **Development JARs**: 7 days  
- **Release JARs**: 30 days
- **GitHub releases**: Permanent

## 🔒 **Security**

- **No secrets in code**: All API tokens use GitHub Secrets
- **Wrapper validation**: Prevents malicious wrapper execution
- **Read-only tokens**: Workflows use minimal required permissions
- **Artifact scanning**: JAR integrity validation on every build

## 🎯 **Performance Optimizations**

- **Gradle setup action**: Better caching than manual cache setup
- **Parallel jobs**: Test and release jobs run efficiently
- **Conditional execution**: Platform publishing only on stable releases
- **Artifact retention**: Automatic cleanup prevents storage bloat

---

**Need Help?** Check the Actions tab for build logs, or run the test-ci.yml workflow with debug mode enabled.