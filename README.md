# MCNotify

MCNotify is a Spigot plugin that allows players to create and protect various areas as well as subscribe to events that occur within their area. Is your furnace done? Are your hoppers full? Are your crops grown? MCNotify has support for many different events that will notify the player when the event has triggered within a specified area, allowing the player to focus on the most important issues at hand.

# For Server Owners:
Installing MCNotify is easy. Simply download the .jar file and place it in the plugins forlder. After running your server once, MCNotify will create a folder within the plugins folder that will need to be configured beofre MCNotify can run. MCNotify requires a local database connection to save player areas and subscriptions. Once these configuration options have been configured, MCNotify will create your database tables and perform database upgrades automatically!
Note: This plugin is free, with the ability to pay for additional features.

| | Free | Premium |
|----|----|----|
| Create areas | ![#00ff00](https://placehold.it/15/00ff00/000000?text=+) | ![#00ff00](https://placehold.it/15/00ff00/000000?text=+)
| View areas | ![#ff0000](https://placehold.it/15/ff0000/000000?text=+) | ![#00ff00](https://placehold.it/15/00ff00/000000?text=+)
| Protect areas | ![#00ff00](https://placehold.it/15/00ff00/000000?text=+) | ![#00ff00](https://placehold.it/15/00ff00/000000?text=+)
| Delete areas | ![#00ff00](https://placehold.it/15/00ff00/000000?text=+) | ![#00ff00](https://placehold.it/15/00ff00/000000?text=+)
| Player area limit | 3 | Unlimited
| Allow area access | ![#00ff00](https://placehold.it/15/00ff00/000000?text=+) | ![#00ff00](https://placehold.it/15/00ff00/000000?text=+)
| Subscribe to events in areas | ![#00ff00](https://placehold.it/15/00ff00/000000?text=+) | ![#00ff00](https://placehold.it/15/00ff00/000000?text=+)
| In-Game messages when events trigger | ![#00ff00](https://placehold.it/15/00ff00/000000?text=+) | ![#00ff00](https://placehold.it/15/00ff00/000000?text=+)
| Push-Notifications to mobile device | ![#ff0000](https://placehold.it/15/ff0000/000000?text=+) | ![#00ff00](https://placehold.it/15/00ff00/000000?text=+)
| Real-time Support | ![#ff0000](https://placehold.it/15/ff0000/000000?text=+) | ![#00ff00](https://placehold.it/15/00ff00/000000?text=+)

# Basic Commands:
MCNotify offers the following basic commands:

| Command Name <img width=800/><br/>Permission Node| Description | Parameters |
|---|---|---|
| `/mcnotify help <page>`<br/>`mcnotify.*` | Shows a help page which lists the MCNotify commands and usage | **page:** The page of 'help' you would like to view. |
| `/mcnotify about`<br/>`mcnotify.*` | Shows information about MCnotify, authors, and links | None |
| `/mcnotify verify`<br/>`mcnotify.*` | Shows your minecraft verification code to link your mobile device with your minecraft account to get push notificaions. | None |

# Area commands
To get started with MCNotify, you need to create an area. The following commands help with creating an area. NOTE: Areas are created with infinite vertical height. Only allowed players can break and place blocks within your area.

| Command Name <img width=800/><br/>Permission Node| Description | Parameters |
|---|---|---|
| `/mcnotify area add <areaName>`<br/>`mcnotify.member.areas.add` | Starts the area creation process. The player is able to select points to define their area. | **areaName:** The desired name for the area. |
| `/mcnotify area list`<br/>`mcnotify.member.areas.list` | Lists all of the areas that you have defined. | None. |
| `/mcnotify area remove <areaName>`<br/>`mcnotify.member.areas.remove` | Removes the specified area. | **areaName:** The name of the area to remove. |
| `/mcnotify area view <areaName>`<br/>`mcnotify.member.areas.view` | Allows you to view the boundary of the area specified. | **areaName:** The name of the area to view. |
| `/mcnotify area info <areaName>`<br/>`mcnotify.member.areas.info` | Provides information about the specified area. | **areaName:** The name of the area to view. |

# Allowing access
Once an area is created, accesss to the area can be limited to specific players. The following commands can be used to add or remove specific players from an area.

| Command Name <img width=800/><br/>Permission Node| Description | Parameters |
|---|---|---|
| `/mcnotify area allow <playerName> <areaName>`<br/>`mcnotify.members.areas.allow` | Allows the specified player to break and place blocks within the area. | **playerName:** The player to add to the area. <br/> **areaName:**The name of the area to allow the player to. |
| `/mcnotify area deny <playerName> <areaName>`<br/>`mcnotify.members.areas.deny` | Prevents the specified player from breaking and placing blocks within the area. | **playerName:** The player to add to the area. <br/> **areaName:**The name of the area to allow the player to. |
| `/mcnotify area deny all <areaName>`<br/>`mcnotify.members.areas.denyall` | Removes all players from being allowed in the area except for the owner of the area. | **areaName:**The name of the area to deny players from. |

# Configuring your area

When an area is created, a number of different protections get applied to the area by default. The default protections can be configured in the config file. Any protection which is enabled by default is able to be toggled by the player if they want to allow more interactions within their area manually.

| Command Name <img width=800/><br/>Permission Node| Description | Parameters |
|---|---|---|
| `/mcnotify area protect <areaName>`<br/>`mcnotify.member.protect.protect` | Prevents blocks from being placed or broken within the area by unallowed players. | **areaName:** The area to protect. |
| `/mcnotify area mobProtect <areaName>`<br/>`mcnotify.member.protect.mobProtect` | Protects peaceful animals from being harmed by unallowed players. | **areaName:** The area to mobProtect. |
| `/mcnotify area stopLiquid <areaName>`<br/>`mcnotify.member.protect.stopLiquid` | Stops lava and water from flowing into the area. | **areaName:** The area to stop liquids. |
| `/mcnotify area chestLock <areaName>`<br/>`mcnotify.member.protect.chestLock` | Prevents chests from being opened by unallowed players. | **areaName:** The area to block chests. |
| `/mcnotify area noRedstone <areaName>`<br/>`mcnotify.member.protect.noRedstone` | Prevents any restone signals from being activated by unallowed players. | **areaName:** The area to stop redstone. |
| `/mcnotify area noFire <areaName>`<br/>`mcnotify.member.protect.noFire` | Prevents fire spread in the area. | **areaName:** The area to stop redstone. |
| `/mcnotify area doorLock <areaName>`<br/>`mcnotify.member.protect.doorLock` | Prevents doors from being opened in the area by unallowed players. | **areaName:** The area to stop redstone. |
| `/mcnotify area noEnter <areaName>`<br/>`mcnotify.member.protect.noEnter` | Prevents unallowed players from entering the area. | **areaName:** The area to stop redstone. |
| `/mcnotify area noPvp <areaName>`<br/>`mcnotify.member.protect.noPvp` | Prevents players within the area from being attacked by another player. | **areaName:** The area to protect from pvp. |
| `/mcnotify area free <areaName>`<br/>`mcnotify.member.protect.free` | Resets the area back to the default protection. | **areaName:** The area to free. |

By default, areas that are created by a player have the following protections:
- protect
- mobProtect
- noFire
- stopLiquid
- chestLock

The default protections can be changed to allow for more or less protection in your world through the configuration files.

# Getting Notifications!
After you have created an area, it's time to start getting notifications! The following commands help you subscribe to various events.

| Command Name <img width=800/><br/>Permission Node| Description | Parameters |
|---|---|---|
| `/mcnotify watch login <player>`<br/>`mcnotify.member.notify.login` | Notifies you when the specified player logs in. | **player:** The player name. Must be logged in. |
| `/mcnotify watch playerEnter <area>`<br/>`mcnotify.member.notify.playerEnter` | Notifies you when a player who is not your friend enters your area. | **area:** The name of the area to watch. |
| `/mcnotify watch explostion <area>`<br/>`mcnotify.member.notify.explosion` | Notifies you when an explosion occurs within an area. | **area:** The name of the area to watch. |
| /`mcnotify watch blockbreak <area>`<br/>`mcnotify.member.notify.blockbreak` | Notifies you when a block is broken within an area. | **area:** The name of the area to watch. |
| `/mcnotify watch redstone <area>`<br/>`mcnotify.member.notify.redstone` | Notifies you when redstone is powered within an area. | **area:** The name of the area to watch. |
| `/mcnotify watch hopper`<br/>`mcnotify.member.notify.hopper` | Notifies you when the selected hopper(s) are full | None |
| `/mcnotify watch crop <area>`<br/>`mcnotify.member.notify.crop` | Notifies you when crop is fully grown within an area. | **area:** The name of the area to watch. |
| `/mcnotify watch mobCap <limit> <area>`<br/>`mcnotify.member.notify.mobLimit` | Notifies you if the mobcap has been reacted within an area. | **area:** The name of the area to watch. |
| `/mcnotify watch enterNether <player>`<br/>`mcnotify.member.notify.enterNether` | Notifies you if the specified player enters the nether. | **player:** The name of the player. Must be online. |
| `/mcnotify watch enterEnd <player>`<br/>`mcnotify.member.notify.enterEnd` | Notifies you if the specified player enters the end. | **player:** The name of the player. Must be online. |
| `/mcnotify watch enterWorld <player>`<br/>`mcnotify.member.notify.enterWorld` | Notifies you if the specified player enters the Overworld. | **player**: The name of the player. Must be online. |
| `/mcnotify watch list`<br/>`mcnotify.member.notify` | Lists all of the events you are currently watching. | None. |
| `/mcnotify unwatch <eventId>`<br/>`mcnotify.member.notify` | Stops you from watching the specific event. Because multiple events can be watched for different areas, the eventId is required. Get the eventId by using `/mcnotify watch list`. | **eventId:** The Id of the event you would like to stop watching. |

# Admin controls

Have a player that needs more protection? Need to remove an area? Admin controls allow modifying other players areas to allow full control over where players are creating their areas. With a useful permission node, `mcnotify.admin.*`, all admin commands can be provided with a single permission node.

### Area Administration

| Command Name <img width=800/><br/>Permission Node| Description | Parameters |
|---|---|---|
|`/mcnotify admin area list <playerName>`<br/>`mcnotify.admin.areas.listAny`| Lists all of a player's areas. | **playerName:** The name of the player to list the areas of. |
|`/mcnotify admin area view <playerName> <areaName>`<br/>`mcnotify.admin.areas.viewAny`| Views the specified player's area. | **playerName:** The name of the player who owns the area.<br/>**areaName:** The name of the player's area to view. |
|`/mcnotify admin area info <playerName> <areaName>`<br/>`mcnotify.admin.areas.infoAny`| Views information about the specified player's area. | **playerName:** The name of the player who owns the area.<br/>**areaName:** The name of the player's area to view info about. |
|`/mcnotify admin area remove <playerName> <areaName>`<br/>`mcnotify.admin.areas.removeAny`| Removes the specified player's area. | **playerName:** The name of the player who owns the area.<br/>**areaName:** The name of the player's area to remove. |
|`/mcnotify admin area remove <playerName> <areaName>`<br/>`mcnotify.admin.areas.removeAny`| Removes the specified player's area. | **playerName:** The name of the player who owns the area.<br/>**areaName:** The name of the player's area to remove. |
|`/mcnotify admin area create <playerName> <areaName>`<br/>`mcnotify.admin.areas.createAny`| Creates an area for the specified player. | **playerName:** The name of the player to create the area for.<br/>**areaName:** The name of the area to create. |
|`/mcnotify admin area rename <playerName> <oldAreaName> <newAreaName>`<br/>`mcnotify.admin.areas.renameAny`| Renames the specified area. | **playerName:** The name of the player to create the area for.<br/>**oldAreaName:** The name of the area to rename.<br/>**newAreaName:** The new name of the area. |
|`/mcnotify admin area toggle <protectionType> <playerName> <newAreaName>`<br/>`mcnotify.admin.areas.protectAny`| Applys or removes a specified protection type from the specified player's area of the given name. | **protectionType:** The tyope of protection to apply to the area.<br/>**playerName:** The name of the player who owns the area.<br/>**areaName:** The name of the area to protect.<br/>**newAreaName:** The new name of the area. |
