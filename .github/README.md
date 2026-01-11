# GitHub Actions Setup für Enhanced Timer HUD

## 🤖 Automatisierte Workflows

Dieses Repository nutzt GitHub Actions für automatisches Building, Testing und Releasing:

### 1. **Build and Test** (`build-test.yml`)
**Trigger**: Bei jedem Push zu `main`/`develop` und Pull Requests
- ✅ Kompiliert das Plugin mit Java 21
- ✅ Führt alle Unit-Tests aus 
- ✅ Uploaded Build-Artefakte für Review
- ✅ Cached Gradle-Dependencies für schnellere Builds

### 2. **Build and Release** (`build-release.yml`)
**Trigger**: Bei Git-Tags (z.B. `v0.2.0`)
- 🚀 Baut das Plugin automatisch
- 🚀 Erstellt GitHub Release mit JAR-Datei
- 🚀 Generiert professionelle Release-Notes
- 🚀 Markiert Dev-Versionen automatisch als "Pre-release"

### 3. **Publish to Mod Platforms** (`publish-platforms.yml`)
**Trigger**: Bei veröffentlichten Releases
- 📦 Uploaded automatisch zu CurseForge
- 📦 Uploaded automatisch zu Modrinth
- 📦 Nur für stabile Releases (keine Pre-releases)

## 🎯 Workflow Usage

### Für Development:
1. **Push zu main** → Build & Test läuft automatisch
2. **Pull Request** → Build & Test validiert Changes

### Für Releases:
1. **Create Tag**: `git tag v0.2.0 && git push origin v0.2.0`
2. **Automatisch**: Build → JAR erstellen → GitHub Release
3. **Automatisch**: Upload zu Mod-Plattformen (wenn konfiguriert)

## ⚙️ Konfiguration

### Secrets benötigt für Mod-Platform Upload:
- `CURSEFORGE_TOKEN`: Dein CurseForge API Token
- `MODRINTH_TOKEN`: Dein Modrinth API Token

### Project IDs anpassen:
In `publish-platforms.yml`:
- `YOUR_CURSEFORGE_PROJECT_ID`: Deine CurseForge Project ID
- `YOUR_MODRINTH_PROJECT_ID`: Deine Modrinth Project Slug

## 🔧 Features der Actions:

### Build-Features:
- **Java 21**: Moderne Java-Version für Minecraft 1.21.1
- **Gradle Caching**: Schnellere Builds durch Dependency-Cache
- **Multi-Platform**: Läuft auf Ubuntu (kann auf Windows/macOS erweitert werden)
- **Artifact Upload**: Downloadbare JARs für jede Build

### Release-Features:
- **Automatische Release-Notes**: Professionell formatiert
- **Semantic Versioning**: Erkennt Dev/Alpha/Beta automatisch
- **JAR-Attachment**: Fertige JAR-Datei direkt im Release
- **Changelog Integration**: Nutzt Git-Tag-Messages

### Publishing-Features:
- **Multi-Platform**: CurseForge + Modrinth gleichzeitig
- **Dependency Management**: Fabric API automatisch als Required
- **Version Sync**: Gleiche Version überall
- **Conditional Publishing**: Nur stabile Releases auf Plattformen

## 📋 Beispiel Release-Prozess:

```bash
# 1. Entwicklung abgeschlossen, Tests laufen
git add .
git commit -m "feat: New awesome feature"
git push

# 2. Release erstellen
git tag -a v0.2.0 -m "Release v0.2.0 with new features"
git push origin v0.2.0

# 3. GitHub Actions machen automatisch:
# - Build JAR
# - Create GitHub Release  
# - Upload to CurseForge & Modrinth (wenn konfiguriert)
```

## 🎉 Vorteile:

- **🔄 Automatisierung**: Keine manuellen Release-Steps
- **🛡️ Qualität**: Jeder Release ist getestet
- **📦 Konsistenz**: Gleicher Build-Prozess für alle
- **⚡ Geschwindigkeit**: Sofortige Releases bei Tag-Push
- **🌐 Verfügbarkeit**: Automatisch auf allen Plattformen

---
**Status**: ✅ Bereit für automatisierte Releases!