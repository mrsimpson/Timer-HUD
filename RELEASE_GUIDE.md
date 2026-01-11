# Release Guide für Minecraft Timer Enhanced

## ✅ Was bereits erledigt ist:
- [x] Code zu GitHub Fork gepusht: https://github.com/mrsimpson/Timer-HUD
- [x] Git-Tag `v0.2.0-dev` erstellt
- [x] Comprehensive README mit Features und Installation
- [x] Alle TDD-Tests erfolgreich (Core-Funktionalität validiert)

## 🚀 Nächste Schritte für das Release:

### 1. GitHub Release erstellen
1. Gehe zu: https://github.com/mrsimpson/Timer-HUD/releases
2. Klicke "Create a new release"
3. Wähle Tag: `v0.2.0-dev`
4. Release Title: `Enhanced Timer HUD v0.2.0-dev`
5. Description: (Copy from tag message or README features)
6. Mark as "Pre-release" (da es -dev Version ist)

### 2. JAR-Datei erstellen (auf deinem System)
```bash
cd ~/path/to/Timer-HUD
./gradlew build
```
Die JAR-Datei wird erstellt in: `build/libs/MinecraftTimer-0.2.0-dev.jar`

### 3. JAR zu GitHub Release hochladen
1. Lade die erstellte JAR-Datei hoch
2. Veröffentliche das Release

### 4. (Optional) Auf Mod-Plattformen veröffentlichen

#### CurseForge:
- Gehe zu: https://www.curseforge.com/minecraft/mc-mods
- "Create Project" → Minecraft Mod
- Upload JAR + Beschreibung aus README

#### Modrinth:
- Gehe zu: https://modrinth.com/
- "Create Project"
- Upload JAR + Beschreibung

## 📋 Release-Information

**Version:** 0.2.0-dev  
**Minecraft:** 1.21.1  
**Mod Loader:** Fabric  
**Dependencies:** 
- Fabric Loader 0.18.4+
- Fabric API 0.116.7+1.21.1

**Features:**
- Visual notification overlays
- Configurable alerts (`/playtimer notify 2h30m`)
- Persistent settings per server/world
- Backward compatible with original Timer HUD

## 🛠️ Wenn Build-Probleme auftreten:

1. **Java Version**: Stelle sicher, dass Java 17+ installiert ist
2. **Gradle Daemon**: Verwende `./gradlew --no-daemon build`
3. **Clean Build**: Führe `./gradlew clean build` aus
4. **Dependencies**: Falls Fabric-Dependencies fehlen, überprüfe `gradle.properties`

## 📞 Support

Falls du Hilfe beim Build oder Release brauchst:
- Check die GitHub Actions (wenn eingerichtet)
- Fabric-Dokumentation: https://fabricmc.net/wiki/
- Discord: Fabric Community Server

---
**Status: Ready for Release! 🚀**
Der Code ist getestet, dokumentiert und bereit für die Veröffentlichung.