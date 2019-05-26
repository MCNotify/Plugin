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

| Command Name <img width=800/>| Description | Parameters |
|---|---|---|
| `/mcnotify help <page>` | Shows a help page which lists the MCNotify commands and usage | **page:** The page of 'help' you would like to view. |
| `/mcnotify about` | Shows information about MCnotify, authors, and links | None |
| `/mcnotify verify` | Shows your minecraft verification code to link your mobile device with your minecraft account to get push notificaions. | None |

# Area commands
To get started with MCNotify, you need to create an area. The following commands help with creating an area. NOTE: Areas are created with infinite vertical height. Only allowed players can break and place blocks within your area.

| Command Name <img width=800/>| Description | Parameters |
|---|---|---|
| `/mcnotify area add <areaName>` | Starts the area creation process. The player is able to select points to define their area. | **areaName:** The desired name for the area. |
| `/mcnotify area list` | Lists all of the areas that you have defined. | None. |
| `/mcnotify area remove <areaName>` | Removes the specified area. | **areaName:** The name of the area to remove. |
| `/mcnotify area view <areaName>` | Allows you to view the boundary of the area specified. | **areaName:** The name of the area to view. |
| `/mcnotify area info <areaName>` | Provides information about the specified area. | **areaName:** The name of the area to view. |

# Setting permissions
Once an area is created, accesss to the area can be limited to specific players. For moderators/admins of the server, players can be completely stopped from entering a specific area if not allowed access. The following commands can be used to set the permissions of a specific area.

| Command Name <img width=800/>| Description | Parameters |
|---|---|---|
| `/mcnotify area allow <playerName> <areaName>` | Allows the specified player to break and place blocks within the area. | **playerName:** The player to add to the area. <br/> **areaName:**The name of the area to allow the player to. |
| `/mcnotify area deny <playerName> <areaName>` | Prevents the specified player from breaking and placing blocks within the area. | **playerName:** The player to add to the area. <br/> **areaName:**The name of the area to allow the player to. |

Admins have additional access to protection commands and may be able to set specific protections on an area using the following commands:

| Command Name <img width=800/>| Description | Parameters |
|---|---|---|
| `/mcnotify area protect <areaName>` | Prevents blocks from being placed or broken within the area by unallowed players. | **areaName:** The area to protect. |
| `/mcnotify area mobProtect <areaName>` | Protects peaceful animals from being harmed by unallowed players. | **areaName:** The area to mobProtect. |
| `/mcnotify area stopLiquid <areaName>` | Stops lava and water from flowing into the area. | **areaName:** The area to stop liquids. |
| `/mcnotify area chestLock <areaName>` | Prevents chests from being opened by unallowed players. | **areaName:** The area to block chests. |
| `/mcnotify area noRS <areaName>` | Prevents any restone signals from being activated by unallowed players. | **areaName:** The area to stop redstone. |
| `/mcnotify area noFire <areaName>` | Prevents fire spread in the area. | **areaName:** The area to stop redstone. |
| `/mcnotify area doorLock <areaName>` | Prevents doors from being opened in the area by unallowed players. | **areaName:** The area to stop redstone. |
| `/mcnotify area noEnter <areaName>` | Prevents unallowed players from entering the area. | **areaName:** The area to stop redstone. |
| `/mcnotify area free <areaName>` | Resets the area back to the default protection. | **areaName:** The area to free. |

By default, areas that are created by a player have the following protections:
- protect (against players not on the allow list)
- mobProtect (against players not on the allow list)
- noFire
- stopLiquid

The default protections can be changed to allow for more or less protection in your world through the configuration files.

# Getting Notifications!
After you have created an area, it's time to start getting notifications! The following commands help you subscribe to various events.

| Command Name <img width=800/>| Description | Parameters |
|---|---|---|
| `/mcnotify watch login <player>` | Notifies you when the specified player logs in. | **player:** The player name. Must be logged in. |
| `/mcnotify watch playerEnter <area>` | Notifies you when a player who is not your friend enters your area. | **area:** The name of the area to watch. |
| `/mcnotify watch explostion <area>` | Notifies you when an explosion occurs within an area. | **area:** The name of the area to watch. |
| /`mcnotify watch blockbreak <area>` | Notifies you when a block is broken within an area. | **area:** The name of the area to watch. |
| `/mcnotify watch redstone <area>` | Notifies you when redstone is powered within an area. | **area:** The name of the area to watch. |
| `/mcnotify watch hopper` | Notifies you when the selected hopper(s) are full | None |
| `/mcnotify watch crop <area>` | Notifies you when crop is fully grown within an area. | **area:** The name of the area to watch. |
| `/mcnotify watch mobCap <limit> <area>` | Notifies you if the mobcap has been reacted within an area. | **area:** The name of the area to watch. |
| `/mcnotify watch enterNether <player>` | Notifies you if the specified player enters the nether. | **player:** The name of the player. Must be online. |
| `/mcnotify watch enterEnd <player>` | Notifies you if the specified player enters the end. | **player:** The name of the player. Must be online. |
| `/mcnotify watch enterWorld <player>` | Notifies you if the specified player enters the Overworld. | **player**: The name of the player. Must be online. |
| `/mcnotify watch list` | Lists all of the events you are currently watching. | None. |
| `/mcnotify unwatch <eventId>` | Stops you from watching the specific event. Because multiple events can be watched for different areas, the eventId is required. Get the eventId by using `/mcnotify watch list`. | **eventId:** The Id of the event you would like to stop watching. |

