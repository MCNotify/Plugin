# ZoneX

ZoneX is a Spigot plugin that allows players to create and protect various areas as well as subscribe to events that occur within their area. Is your furnace done? Are your hoppers full? Are your crops grown? ZoneX has support for many different events that will notify the player when the event has triggered within a specified area, allowing the player to focus on the most important issues at hand.

# For Server Owners:
Installing ZoneX is easy. Simply download the .jar file and place it in the plugins forlder. 
After running your server once, ZoneX will create a folder within the plugins folder that will need to be configured beofre ZoneX can run.
MCNofity allows flat file storage, however it is highly recommended to configure a database connection to store the data.
Once these configuration options have been configured, ZoneX will create your database tables (or flat file) automatically!


This plugin is currently free, however as development progresses, a paid version will be released in the future.
Currently all of the premium features are avaliable, but the plan is to split functionality as follows:

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

**NOTE:** The `<zoneName>` command parameter is optional if you are standing inside of an area you own. You only need to specify the `<zoneName>` parameter if you are not inside a zone or want to manage a different zone.

ZoneX offers the following basic commands which provide information:

| Command Name <img width=800/><br/>Permission Node| Description | Parameters |
|---|---|---|
| `/zx help <page>`<br/>`zx.*` | Shows a help page which lists the ZoneX commands and usage | **page:** The page of 'help' you would like to view. |
| `/zx verify`<br/>`zx.*` | Shows your minecraft verification code to link your mobile device with your minecraft account to get push notificaions. | None |

# Area commands
To get started with ZoneX, you need to create an area. The following commands help with creating an area. NOTE: Areas are created with infinite vertical height. Only allowed players can break and place blocks within your area.

| Command Name <img width=800/><br/>Permission Node| Description | Parameters |
|---|---|---|
| `/zone add <zoneName>`<br/>`zx.member.zone.add` | Starts the area creation process. The player is able to select points to define their area. | **zoneName:** The desired name for the area. Optional if you are standing inside of a zone. |
| `/zone list`<br/>`zx.member.zone.list` | Lists all of the areas that you have defined. | None. |
| `/zone remove <zoneName>`<br/>`zx.member.zone.remove` | Removes the specified area. | **zoneName:** The name of the area to remove. Optional if you are standing inside of a zone. |
| `/zone view <zoneName>`<br/>`zx.member.zone.view` | Allows you to view the boundary of the area specified. | **zoneName:** The name of the area to view. Optional if you are standing inside of a zone. |
| `/zone info <zoneName>`<br/>`zx.member.zone.info` | Provides information about the specified area. | **zoneName:** The name of the area to view. Optional if you are standing inside of a zone. |

# Allowing access
Once an area is created, accesss to the area can be limited to specific players. The following commands can be used to add or remove specific players from an area.

| Command Name <img width=800/><br/>Permission Node| Description | Parameters |
|---|---|---|
| `/zone allow <playerName> <zoneName>`<br/>`zx.members.zone.allow` | Allows the specified player to break and place blocks within the area. | **playerName:** The player to add to the area. <br/> **zoneName:**The name of the area to allow the player to. Optional if you are standing inside of a zone. |
| `/zone deny <playerName> <zoneName>`<br/>`zx.members.zone.deny` | Prevents the specified player from breaking and placing blocks within the area. | **playerName:** The player to add to the area. <br/> **zoneName:**The name of the area to allow the player to. Optional if you are standing inside of a zone. |
| `/zone deny all <zoneName>`<br/>`zx.members.zone.denyall` | Removes all players from being allowed in the area except for the owner of the area. | **zoneName:**The name of the area to deny players from. Optional if you are standing inside of a zone. |

# Configuring your area

When an area is created, a number of different protections get applied to the area by default. The default protections can be configured in the config file. Any protection which is enabled by default is able to be toggled by the player if they want to allow more interactions within their area manually.

| Command Name <img width=800/><br/>Permission Node| Description | Parameters |
|---|---|---|
| `/zone protect <zoneName>`<br/>`zx.member.protect.protect` | Prevents blocks from being placed or broken within the area by unallowed players. | **zoneName:** The area to protect. |
| `/zone mobProtect <zoneName>`<br/>`zx.member.protect.mobProtect` | Protects peaceful animals from being harmed by unallowed players. | **zoneName:** The area to mobProtect. |
| `/zone stopLiquid <zoneName>`<br/>`zx.member.protect.stopLiquid` | Stops lava and water from flowing into the area. | **zoneName:** The area to stop liquids. |
| `/zone chestLock <zoneName>`<br/>`zx.member.protect.chestLock` | Prevents chests from being opened by unallowed players. | **zoneName:** The area to block chests. |
| `/zone noRedstone <zoneName>`<br/>`zx.member.protect.noRedstone` | Prevents any restone signals from being activated by unallowed players. | **zoneName:** The area to stop redstone. |
| `/zone noFire <zoneName>`<br/>`zx.member.protect.noFire` | Prevents fire spread in the area. | **zoneName:** The area to stop redstone. |
| `/zone doorLock <zoneName>`<br/>`zx.member.protect.doorLock` | Prevents doors from being opened in the area by unallowed players. | **zoneName:** The area to stop redstone. |
| `/zone noEnter <zoneName>`<br/>`zx.member.protect.noEnter` | Prevents unallowed players from entering the area. | **zoneName:** The area to stop redstone. |
| `/zone noPvp <zoneName>`<br/>`zx.member.protect.noPvp` | Prevents players within the area from being attacked by another player. | **zoneName:** The area to protect from pvp. |
| `/zone free <zoneName>`<br/>`zx.member.protect.free` | Resets the area back to the default protection. | **zoneName:** The area to free. |

By default, areas that are created by a player have the following protections:
- protect
- mobProtect
- noFire
- stopLiquid
- chestLock

The default protections can be changed to allow for more or less protection in your world through the configuration files.

# Getting Notifications!
After you have created an area, it's time to start getting notifications! In order to do this, your server admins need to have configured some external communication services. This plugin has the capability of sending email, sms, and discord messages (potentially more in the future). The following commands will setup a notification system to a specific protocol:

| Command Name | Description | Parameters |
|---|---|---|
| `/zx sms <phoneNumber>` | Register your phone number to receive SMS notifications. | **phoneNumber:** Format: +1XXXXXXXXX |
| `/zx email <email>` | Register your email address to receive email notifications. | **email:** Your email |
| `/zx discord` | Register your discord account to receive discord notifications. | N/A |
| `/zx sms stop` | Stop receiving text notifications | N/A |
| `/zx email stop` | Stop receiving email notifications. | N/A |
| `/zx discord stop` | Stop receiving discord notifications. | N/A |
| `/zx devices` | Shows all currently connected communication protocols | N/A |

Once you have subscribed to a communication protocol, you can watch certain events in your zones with the following commands:

| Command Name <img width=800/><br/>Permission Node| Description | Parameters |
|---|---|---|
| `/watch login <player>`<br/>`zx.member.notify.login` | Notifies you when the specified player logs in. | **player:** The player name. Must be logged in. |
| `/watch playerEnter <area>`<br/>`zx.member.notify.playerEnter` | Notifies you when a player who is not your friend enters your area. | **area:** The name of the area to watch. |
| `/watch explostion <area>`<br/>`zx.member.notify.explosion` | Notifies you when an explosion occurs within an area. | **area:** The name of the area to watch. |
| `/watch blockbreak <area>`<br/>`zx.member.notify.blockbreak` | Notifies you when a block is broken within an area. | **area:** The name of the area to watch. |
| `/watch redstone <area>`<br/>`zx.member.notify.redstone` | Notifies you when redstone is powered within an area. | **area:** The name of the area to watch. |
| `/watch hopper`<br/>`zx.member.notify.hopper` | Notifies you when the selected hopper(s) are full | None |
| `/watch crop <area>`<br/>`zx.member.notify.crop` | Notifies you when crop is fully grown within an area. | **area:** The name of the area to watch. |
| `/watch mobCap <limit> <area>`<br/>`zx.member.notify.mobLimit` | Notifies you if the mobcap has been reacted within an area. | **area:** The name of the area to watch. |
| `/watch enterNether <player>`<br/>`zx.member.notify.enterNether` | Notifies you if the specified player enters the nether. | **player:** The name of the player. Must be online. |
| `/watch enterEnd <player>`<br/>`zx.member.notify.enterEnd` | Notifies you if the specified player enters the end. | **player:** The name of the player. Must be online. |
| `/watch enterWorld <player>`<br/>`zx.member.notify.enterWorld` | Notifies you if the specified player enters the Overworld. | **player**: The name of the player. Must be online. |
| `/watch list`<br/>`zx.member.notify` | Lists all of the events you are currently watching. | None. |
| `/unwatch <eventId>`<br/>`zx.member.notify` | Stops you from watching the specific event. Because multiple events can be watched for different areas, the eventId is required. Get the eventId by using `/ZoneX watch list`. | **eventId:** The Id of the event you would like to stop watching. |

# Admin controls

Have a player that needs more protection? 
Need to remove an area? 
Admin controls allow modifying other players areas to allow full control over where players are creating their areas. 
With a useful permission node, `zx.admin.*`, all admin commands can be provided with a single permission node.

### Area Administration

| Command Name <img width=800/><br/>Permission Node| Description | Parameters |
|---|---|---|
|`/zone list <playerName>`<br/>`zx.admin.zone.listAny`| Lists all of a player's areas. | **playerName:** The name of the player to list the areas of. |
|`/zone view <playerName> <zoneName>`<br/>`zx.admin.zone.viewAny`| Views the specified player's area. | **playerName:** The name of the player who owns the area.<br/>**zoneName:** The name of the player's area to view. |
|`/zone info <playerName> <zoneName>`<br/>`zx.admin.zone.infoAny`| Views information about the specified player's area. | **playerName:** The name of the player who owns the area.<br/>**zoneName:** The name of the player's area to view info about. |
|`/zone remove <playerName> <zoneName>`<br/>`zx.admin.zone.removeAny`| Removes the specified player's area. | **playerName:** The name of the player who owns the area.<br/>**zoneName:** The name of the player's area to remove. |
|`/zone create <playerName> <zoneName>`<br/>`zx.admin.zone.createAny`| Creates an area for the specified player. | **playerName:** The name of the player to create the area for.<br/>**zoneName:** The name of the area to create. |
|`/zone <protectionType> <playerName> <newzoneName>`<br/>`zx.admin.zone.protectAny`| Applys or removes a specified protection type from the specified player's area of the given name. | **protectionType:** The tyope of protection to apply to the area.<br/>**playerName:** The name of the player who owns the area.<br/>**zoneName:** The name of the area to protect.<br/>**newzoneName:** The new name of the area. |
