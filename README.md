# Minecraft Timer - Enhanced Timer HUD

An enhanced version of the original Timer HUD mod with notification alerts for Minecraft 1.21.1.

## 🎯 Features

### Original Timer HUD Features
- ✅ Display current playtime on screen (HH:MM:SS format)
- ✅ Persistent timer storage (survives client restarts)
- ✅ Per-server and per-world tracking
- ✅ Commands: `/playtimer help`, `/playtimer reset`, `/playtimer sync`, `/playtimer toggle`

### 🆕 New Features (v0.2.0)
- 🎨 **Visual Notifications**: Prominent overlay alerts when timer reaches set duration
- ⏰ **Configurable Alerts**: Set custom notification times (e.g., "2h30m", "90m", "45s")
- 💾 **Persistent Settings**: Notification preferences saved per server/world
- 🎭 **Smooth Animations**: Fade-in/fade-out effects for notifications
- 🛠️ **Enhanced Commands**: Extended `/playtimer` command system

## 📋 Commands

| Command | Description | Examples |
|---------|-------------|----------|
| `/playtimer notify <duration>` | Set timer notification | `/playtimer notify 2h30m` |
| `/playtimer notify off` | Disable notifications | `/playtimer notify off` |
| `/playtimer notify status` | Show current settings | `/playtimer notify status` |
| `/playtimer help` | Show all commands | `/playtimer help` |
| `/playtimer reset` | Reset current timer | `/playtimer reset` |
| `/playtimer sync` | Sync with server stats | `/playtimer sync` |
| `/playtimer toggle` | Toggle timer display | `/playtimer toggle` |

### Duration Formats Supported
- Hours: `2h`, `1h30m`
- Minutes: `30m`, `90m` 
- Seconds: `45s`, `120s`
- Combined: `2h15m30s`

## 🎨 Notification System

When your set playtime is reached, you'll see:
- **Prominent visual overlay** in the upper screen area
- **Animated appearance** with smooth fade-in/fade-out
- **Clear message** showing how long you've been playing
- **Non-disruptive** - doesn't block gameplay

## 📦 Installation

1. **Download** the latest `.jar` file from [Releases](https://github.com/mrsimpson/Timer-HUD/releases)
2. **Install** Fabric Loader for Minecraft 1.21.1
3. **Place** the `.jar` file in your `mods` folder
4. **Start** Minecraft with Fabric profile

### Requirements
- ✅ Minecraft 1.21.1
- ✅ Fabric Loader 0.18.4+
- ✅ Fabric API 0.116.7+1.21.1

## 🔧 Development

Built with Test-Driven Development (TDD) approach:
- **JUnit 5** for unit testing
- **Mockito** for mocking dependencies
- **Fabric Test Framework** for integration tests

### Build from Source
```bash
git clone https://github.com/mrsimpson/Timer-HUD.git
cd Timer-HUD
./gradlew build
```

The compiled `.jar` will be in `build/libs/`.

## 📝 Changelog

### v0.2.0-dev (Current)
- 🆕 Added visual notification system
- 🆕 Configurable timer alerts  
- 🆕 Enhanced command system with `/playtimer notify`
- 🆕 Persistent notification settings
- 🆕 Updated to Minecraft 1.21.1
- 🆕 Modern Fabric API compatibility

### v0.1-a (Original)
- ✅ Basic timer display functionality
- ✅ Minecraft 1.20.1 support

## 🤝 Contributing

This is an enhanced fork of the original Timer HUD. Contributions are welcome!
- Test coverage for new features
- Bug reports and feature requests via Issues
- Pull requests for improvements

## 📄 License

Licensed under the same terms as the original Timer HUD project.

## 🎮 Original Credits

Based on the original Timer HUD mod. Enhanced with notification features and updated for modern Minecraft versions.

---
*Enhanced with ❤️ for the Minecraft community*