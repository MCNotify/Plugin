package org.zonex.commands.zone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zonex.ZoneX;
import org.zonex.areas.Area;
import org.zonex.areas.protection.Protection;
import org.zonex.commands.AbstractCommand;
import org.zonex.commands.RegisterCommands;
import org.zonex.commands.HelpFactory;
import org.zonex.commands.autocomplete.AutoCompleter;
import org.zonex.commands.autocomplete.CommandAutoCompleteNode;
import org.zonex.commands.autocomplete.PlayerNameAutoCompleteNode;
import org.zonex.commands.autocomplete.ZoneNameAutoCompleteNode;
import org.zonex.commands.multipartcommand.multipartcommands.MultiPartOnAreaAddCommand;
import org.zonex.config.Configuration;
import org.zonex.config.Permission;

import java.util.ArrayList;

public class ZoneHandler extends AbstractCommand {
    
    public ZoneHandler(){
        super("zone", false);

        AutoCompleter autoCompleter = new AutoCompleter();
        autoCompleter.addNode(new CommandAutoCompleteNode("list").requiresPermissionNode(Permission.ZONE_LIST).addChild(new PlayerNameAutoCompleteNode().requiresPermissionNode(Permission.ZONE_ADMIN_LIST_ANY)));
        autoCompleter.addNode(new CommandAutoCompleteNode("add").requiresPermissionNode(Permission.ZONE_ADD).addChild(new ZoneNameAutoCompleteNode().addChild(new PlayerNameAutoCompleteNode().requiresPermissionNode(Permission.ZONE_ADMIN_ADD_ANY))));
        autoCompleter.addNode(new CommandAutoCompleteNode("remove").requiresPermissionNode(Permission.ZONE_REMOVE).addChild(new ZoneNameAutoCompleteNode().addChild(new PlayerNameAutoCompleteNode().requiresPermissionNode(Permission.ZONE_ADMIN_REMOVE_ANY))));
        autoCompleter.addNode(new CommandAutoCompleteNode("deny").requiresPermissionNode(Permission.ZONE_DENY_PLAYERS).addChild(new PlayerNameAutoCompleteNode().addChild(new PlayerNameAutoCompleteNode().requiresPermissionNode(Permission.ZONE_ADMIN_DENY_ANY))));
        autoCompleter.addNode(new CommandAutoCompleteNode("allow").requiresPermissionNode(Permission.ZONE_ALLOW_PLAYERS).addChild(new PlayerNameAutoCompleteNode().addChild(new PlayerNameAutoCompleteNode().requiresPermissionNode(Permission.ZONE_ADMIN_ALLOW_ANY))));
        autoCompleter.addNode(new CommandAutoCompleteNode("help"));


        // Are Zone list auto-completes.
        autoCompleter.addNode(new CommandAutoCompleteNode("view").requiresPermissionNode(Permission.ZONE_VIEW).addChild(new ZoneNameAutoCompleteNode().addChild(new PlayerNameAutoCompleteNode().requiresPermissionNode(Permission.ZONE_ADMIN_VIEW_ANY))));
        autoCompleter.addNode(new CommandAutoCompleteNode("info").requiresPermissionNode(Permission.ZONE_INFO).addChild(new ZoneNameAutoCompleteNode().addChild(new PlayerNameAutoCompleteNode().requiresPermissionNode(Permission.ZONE_ADMIN_INFO_ANY))));


        for(Protection protection : Protection.values()){
            autoCompleter.addNode(new CommandAutoCompleteNode(protection.getCommand()).requiresPermissionNode(protection.getPermissionNode()).addChild(new ZoneNameAutoCompleteNode().addChild(new PlayerNameAutoCompleteNode().requiresPermissionNode(Permission.ZONE_ADMIN_PROTECT_ANY))));
        }

        this.setAutoCompleter(autoCompleter);
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        switch(args[0].toLowerCase()) {
            case "add": {
                if(!Permission.ZONE_ADD.hasPermission(player)) {
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Permission denied.");
                    return;
                }

                if(args.length == 3){
                    if(Permission.ZONE_ADMIN_ADD_ANY.hasPermission(player)) {
                        player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Coming soon!");
                        return;
                    }
                }

                if (args.length != 2) {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + "You must specify a name for the area you would like to create.");
                    return;
                }

                if(args[1].toLowerCase().equals("help")){
                    player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " This command creates a new area. Usage: /zone add <areaName>");
                    return;
                }

                if(ZoneX.areaManager.getAreas(player.getUniqueId()).size() >= Integer.valueOf(Configuration.AREA_LIMIT.getValue())){
                    player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " You can only have " + Configuration.AREA_LIMIT.getValue() + " areas.");
                    return;
                }

                // Generate a new multi-part command listener
                new MultiPartOnAreaAddCommand(player, args[1]);

                // Generate an item with unique tags to identify in the event.
                ItemStack itemStack = new ItemStack(Material.IRON_BLOCK);

                // Bind it to the player until the command is finished.
                itemStack.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);

                player.getInventory().addItem(itemStack);

                player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Use the iron to create your area.");
                break;
            }
            case "list": {
                if(!Permission.ZONE_LIST.hasPermission(player)) {
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Permission denied.");
                    return;
                }


                if (args.length == 2) {
                    // Admin command to view another player's areas.
                    if(Permission.ZONE_ADMIN_LIST_ANY.hasPermission((Player)sender)){
                        // Try to get the player they want to view
                        OfflinePlayer searchForPlayer = Bukkit.getOfflinePlayer(args[1]);
                        if(searchForPlayer != null){
                            ArrayList<Area> playerAreas = ZoneX.areaManager.getAreas(searchForPlayer.getUniqueId());
                            player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + searchForPlayer.getName() + " has " + playerAreas.size() + " area(s).");
                            if (playerAreas.size() > 0) {
                                player.sendMessage(ChatColor.GOLD + "===Area Id : Area Name===");
                            }
                            for (Area area : playerAreas) {
                                player.sendMessage("" + ChatColor.GRAY + area.getAreaId() + " : " + ChatColor.GREEN + area.getAreaName());
                            }
                        } else {
                            player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "That player does not exist." );
                        }
                    return;
                    }
                }

                ArrayList<Area> playerAreas = ZoneX.areaManager.getAreas(player.getUniqueId());
                player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " You have " + playerAreas.size() + " area(s).");
                if (playerAreas.size() > 0) {
                    player.sendMessage(ChatColor.GOLD + "===Area Id : Area Name===");
                }
                for (Area area : playerAreas) {
                    player.sendMessage("" + ChatColor.GRAY + area.getAreaId() + " : " + ChatColor.GREEN + area.getAreaName());
                }

                break;
            }
            case "remove": {
                if(!Permission.ZONE_REMOVE.hasPermission(player)) {
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Permission denied.");
                    return;
                }

                if(args.length == 3){
                    // Admin command to remove another player's area.
                    if(Permission.ZONE_ADMIN_REMOVE_ANY.hasPermission(player)){
                        // Determine if the player can be found
                        OfflinePlayer searchForPlayer = Bukkit.getOfflinePlayer(args[2]);
                        String areaName = args[1];
                        if(searchForPlayer != null) {
                            if (ZoneX.areaManager.removeArea(searchForPlayer.getUniqueId(), areaName)) {
                                player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Area removed.");
                            } else {
                                player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Unable to remove area.");
                            }
                        } else {
                            player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "That player does not exist." );
                        }
                    return;
                    }
                }

                if (args.length != 2) {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "You must specify the name of the area to remove.");
                }

                String areaName = args[1];

                if(areaName.toLowerCase().equals("help")){
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "This command deletes an area. Usage: /zone remove <areaName>");
                    return;
                }

                if (ZoneX.areaManager.removeArea(player.getUniqueId(), areaName)) {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Area removed.");
                } else {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Unable to remove area.");
                }
                break;
            }
            case "view": {
                if(!Permission.ZONE_VIEW.hasPermission(player)) {
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Permission denied.");
                    return;
                }

                if(args.length == 3){
                    // Admin command to view another player's area.
                    if(Permission.ZONE_ADMIN_VIEW_ANY.hasPermission(player)){
                        // Determine if the player can be found
                        OfflinePlayer searchForPlayer = Bukkit.getOfflinePlayer(args[2]);
                        String areaName = args[1];
                        if(searchForPlayer != null) {
                            Area area = ZoneX.areaManager.getArea(searchForPlayer.getUniqueId(), areaName);
                            if (area != null) {
                                if (RegisterCommands.particleManager.isViewingArea(player)) {
                                    RegisterCommands.particleManager.stopAreaViewParticleThread(player);
                                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Stopped viewing area.");
                                } else {
                                    RegisterCommands.particleManager.startAreaVeiwParticleThread(player, area.getPolygon());
                                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Started viewing area. Type the command again to hide the area.");
                                }
                            } else {
                                player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "That area does not exist." );
                            }
                        } else {
                            player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "That player does not exist." );
                        }
                    return;
                    }
                }

                if (args.length != 2) {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "You must specify the name of the area to view.");
                }

                String areaName = args[1];


                if(areaName.toLowerCase().equals("help")){
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "This command shows an outline of the specified area. Usage: /zone view <areaName>");
                    return;
                }

                Area area = ZoneX.areaManager.getArea(player.getUniqueId(), areaName);

                if (area != null) {
                    if (RegisterCommands.particleManager.isViewingArea(player)) {
                        RegisterCommands.particleManager.stopAreaViewParticleThread(player);
                        player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Stopped viewing area.");
                    } else {
                        RegisterCommands.particleManager.startAreaVeiwParticleThread(player, area.getPolygon());
                        player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Started viewing area. Type the command again to hide the area.");
                    }
                } else {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "That area does not exist." );
                }
                break;
            }
            case "allow": {
                if(!Permission.ZONE_ALLOW_PLAYERS.hasPermission(player)) {
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Permission denied.");
                    return;
                }

                if(args.length == 4){
                    // Admin command to allow anyone
                    if(Permission.ZONE_ADMIN_ALLOW_ANY.hasPermission(player)){
                        // Determine if the player can be found
                        OfflinePlayer allowedPlayer = Bukkit.getOfflinePlayer(args[1]);
                        String areaName = args[2];
                        OfflinePlayer searchForPlayer = Bukkit.getOfflinePlayer(args[3]);
                        if(searchForPlayer != null) {
                            Area area = ZoneX.areaManager.getArea(searchForPlayer.getUniqueId(), areaName);
                            if(area.addWhitelist(allowedPlayer)){
                                player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + allowedPlayer.getName() + " is now allowed to the area.");
                            } else {
                                player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + allowedPlayer.getName() + " is now allowed to the area.");
                                player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.RED + "SAVE ERROR: " + ChatColor.GRAY + " This information was unable to be saved. Be aware that when the server is restarted these changes will NOT persist. Contact your server admins.");
                            }
                        } else {
                            player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "That player does not exist." );
                        }
                        return;
                    }
                }

                if (args.length != 3) {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "The proper syntax is /zone allow <playerName> <areaName>");
                }

                String playerName = args[1];
                String areaName = args[2];


                if(playerName.toLowerCase().equals("help")){
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "This command allows a player to bypass any protection on your area. Usage: /zone allow <playerName> <areaName>");
                    return;
                }

                Area area = ZoneX.areaManager.getArea(player.getUniqueId(), areaName);
                OfflinePlayer allowedPlayer = Bukkit.getOfflinePlayer(playerName);

                if(area.addWhitelist(allowedPlayer)){
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + allowedPlayer.getName() + " is now allowed in your area.");
                } else {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + allowedPlayer.getName() + " is now allowed in your area.");
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.RED + "SAVE ERROR: " + ChatColor.GRAY + " This information was unable to be saved. Be aware that when the server is restarted these changes will NOT persist. Contact your server admins.");
                }
                break;
            }
            case "deny": {
                if(!Permission.ZONE_DENY_PLAYERS.hasPermission(player)) {
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Permission denied.");
                    return;
                }

                if(args.length == 4){
                    // Admin command to deny anyone
                    if(Permission.ZONE_ADMIN_DENY_ANY.hasPermission(player)){
                        // Determine if the player can be found
                        OfflinePlayer denyPlayer = Bukkit.getOfflinePlayer(args[1]);
                        String areaName = args[2];
                        OfflinePlayer searchForPlayer = Bukkit.getOfflinePlayer(args[3]);
                        if(searchForPlayer != null) {
                            Area area = ZoneX.areaManager.getArea(searchForPlayer.getUniqueId(), areaName);
                            if(area.removeWhitelist(denyPlayer)){
                                player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + denyPlayer.getName() + " is now denied to the area.");
                            } else {
                                player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + denyPlayer.getName() + " is now denied to the area.");
                                player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.RED + "SAVE ERROR: " + ChatColor.GRAY + " This information was unable to be saved. Be aware that when the server is restarted these changes will NOT persist. Contact your server admins.");
                            }
                        } else {
                            player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "That player does not exist." );
                        }
                        return;
                    }
                }

                if (args.length != 3) {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "The proper syntax is /zone deny <playerName> <areaName>");
                }

                String playerName = args[1];
                String areaName = args[2];


                if(playerName.toLowerCase().equals("help")){
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "This command restricts a player to the protections on the area. Usage: /zone deny <playerName> <areaName>");
                    return;
                }


                Area area = ZoneX.areaManager.getArea(player.getUniqueId(), areaName);
                OfflinePlayer allowedPlayer = Bukkit.getOfflinePlayer(playerName);

                if(area.removeWhitelist(allowedPlayer)){
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + allowedPlayer.getName() + " is now denied from your area.");
                } else {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + allowedPlayer.getName() + " is now denied from your area.");
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.RED + "SAVE ERROR: " + ChatColor.GRAY + " This information was unable to be saved. Be aware that when the server is restarted these changes will NOT persist. Contact your server admins.");
                }

                break;
            }
            case "help": {
                player.sendMessage(ChatColor.GOLD + "========" + ChatColor.GREEN + "   [ZoneX] /zone" + ChatColor.GOLD + "========");
                player.sendMessage(ChatColor.GOLD + "add <areaName>: " + ChatColor.GRAY + "Creates a new area.");
                player.sendMessage(ChatColor.GOLD + "remove <areaName>: " + ChatColor.GRAY + "Deletes an area you own.");
                player.sendMessage(ChatColor.GOLD + "list: " + ChatColor.GRAY + "Shows a list of all areas you own.");
                player.sendMessage(ChatColor.GOLD + "info <areaName>: " + ChatColor.GRAY + "Provides detailed information about the specified area.");
                player.sendMessage(ChatColor.GOLD + "allow <playerName> <areaName>: " + ChatColor.GRAY + "Allows a player to bypass all protections in an area.");
                player.sendMessage(ChatColor.GOLD + "deny <playerName> <areaName>: " + ChatColor.GRAY + "Blocks a player based on the protections of the area.");
                player.sendMessage(ChatColor.GOLD + "<protectionType> <areaName>: " + ChatColor.GRAY + "Enables/disables the protectionType on the area. See /zonex protection help.");
                player.sendMessage(ChatColor.GOLD + "help: " + ChatColor.GRAY + "This information page.");
                break;
            }
            case "info":{
                if(!Permission.ZONE_INFO.hasPermission(player)) {
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Permission denied.");
                    return;
                }

                if(args.length == 3){
                    if(Permission.ZONE_ADMIN_INFO_ANY.hasPermission(player)) {
                        // Admin command to view info for anyone's area
                        OfflinePlayer searchForPlayer = Bukkit.getOfflinePlayer(args[2]);
                        String areaName = args[1];
                        if (searchForPlayer != null) {
                            Area area = ZoneX.areaManager.getArea(searchForPlayer.getUniqueId(), areaName);

                            if (area != null) {

                                player.sendMessage(ChatColor.GOLD + "========" + ChatColor.GREEN + "   " + area.getAreaName() + "   " + ChatColor.GOLD + "========");
                                player.sendMessage(ChatColor.GREEN + "Owner: " + ChatColor.WHITE + area.getOwner().getName());
                                player.sendMessage(ChatColor.GREEN + "Protections: " + ChatColor.WHITE + area.getPlayerFriendlyProtectionString());
                                player.sendMessage(ChatColor.GREEN + "Allowed Players: " + ChatColor.WHITE + area.getPlayerFriendlyWhitelistString());
                                player.sendMessage(ChatColor.GREEN + "World: " + ChatColor.WHITE + area.getWorld());
                                player.sendMessage(ChatColor.GREEN + "Boundary: " + ChatColor.WHITE + area.getPolygon().getPlayerFriendlyString());
                                player.sendMessage(ChatColor.GREEN + "Area Size (blocks): " + ChatColor.WHITE + area.getPolygon().getArea());
                                player.sendMessage(ChatColor.GOLD + "================================");
                            } else {
                                player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Area does not exist");
                            }
                        }
                        return;
                    }
                }

                if(args.length != 2){
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "The proper syntax is /zone info <areaName>");
                }

                String areaName = args[1];

                if(areaName.toLowerCase().equals("help")){
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "This command provides detailed information about a specific area. The proper syntax is /zone <protectionType> <areaName>");
                }

                Area area = ZoneX.areaManager.getArea(player.getUniqueId(), areaName);

                if(area != null) {

                    player.sendMessage(ChatColor.GOLD + "========" + ChatColor.GREEN + "   " + area.getAreaName() + "   " + ChatColor.GOLD + "========");
                    player.sendMessage(ChatColor.GREEN + "Owner: " + ChatColor.WHITE + area.getOwner().getName());
                    player.sendMessage(ChatColor.GREEN + "Protections: " + ChatColor.WHITE + area.getPlayerFriendlyProtectionString());
                    player.sendMessage(ChatColor.GREEN + "Allowed Players: " + ChatColor.WHITE + area.getPlayerFriendlyWhitelistString());
                    player.sendMessage(ChatColor.GREEN + "World: " + ChatColor.WHITE + area.getWorld());
                    player.sendMessage(ChatColor.GREEN + "Boundary: " + ChatColor.WHITE + area.getPolygon().getPlayerFriendlyString());
                    player.sendMessage(ChatColor.GREEN + "Area Size (blocks): " + ChatColor.WHITE + area.getPolygon().getArea());
                    player.sendMessage(ChatColor.GOLD + "================================");
                } else {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "You do not have an area with that name");
                }

                break;
            }
            default: {
                if(args.length == 3){
                    // Admin command to toggle protection
                    if(Permission.ZONE_ADMIN_PROTECT_ANY.hasPermission(player)){
                        player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Coming soon!");
                        return;
                    }
                }

                if (args.length != 2) {
                    new HelpFactory().sendProtectionList(player);
                    return;
                }

                String areaName = args[1];

                Area area = ZoneX.areaManager.getArea(player.getUniqueId(), areaName);

                Protection protection = Protection.fromCommand(args[0]);

                if(protection != null){
                    if(protection.getPermissionNode().hasPermission(player)) {
                        if (area.toggleProtection(protection)) {
                            if (area.hasProtection(protection)) {
                                player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + protection.getEnabledMessage() + " in " + area.getAreaName());
                            } else {
                                player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + protection.getDisabledMessage() + " in " + area.getAreaName());
                            }
                        } else {
                            if (area.hasProtection(protection)) {
                                player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + protection.getEnabledMessage() + " in " + area.getAreaName());
                            } else {
                                player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + protection.getDisabledMessage() + " in " + area.getAreaName());
                            }
                            player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.RED + "SAVE ERROR: " + ChatColor.GRAY + " This information was unable to be saved. Be aware that when the server is restarted these changes will NOT persist. Contact your server admins.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Permission denied.");
                        return;
                    }
                } else {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + " That is not a valid protection type.");
                    new HelpFactory().sendProtectionList(player);
                    return;
                }

                break;
            }
        }
    }
}
