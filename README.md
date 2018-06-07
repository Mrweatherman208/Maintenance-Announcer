# Maintenance Announcer
Maintenance Announcer is a lightweight Bukkit Minecraft server plugin that makes it easy to tell everyone on your server that Mainteance to the server is happening.

## Features
- Lightweight
- Easy to Use
- Works with PermissionsEx

## Requirements

 - Java 7+
 - Minecraft Server that supports Bukkit Plugins

## Installation

Go to your "plugins" folder on your Bukkit Minecraft Server, then add the "Maintenance Announcer 4.x.jar" to the folder.

## Usage

### Broadcast to Everyone

Telling everyone that maintenance to the server has started is easy. Simply:
```java
/maintenance start
```
[![Maintenance has started.](screenshots/MaintenanceStarted.png)]

Telling everyone that maintenance to the server has ended is easy. Simply:
```java
/maintenance stop
```
[![Maintenance has ended.](screenshots/MaintenanceEnded.png)]

### Broadcast to Only Admins

Tell admins that maintenance to the server has started:
```java
/maintenance admin start
```

Tell admins that maintenance to the server has ended:
```java
/maintenance admin stop
```

### Misc.

To reload Maintenance Announcer:
```java
/maintenance reload
```
[![Maintenance Announcer was reloaded.](screenshots/MaintenanceReload.png)]

## License

Maintenance Announcer is available under the MIT license. See the LICENSE file for more info.
