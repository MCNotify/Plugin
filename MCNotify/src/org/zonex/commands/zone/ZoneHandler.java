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
import org.zonex.commands.multipartcommand.multipartcommands.MultiPartOnAreaAddCommand;

import java.util.ArrayList;
import java.util.List;

public class ZoneHandler extends AbstractCommand {
    
    public ZoneHandler(){
        super("zone", false);
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        switch(args[0].toLowerCase()) {
            case "add": {
                if(!player.hasPermission("zx.member.area.add")) {
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Permission denied.");
                    return;
                }

                if (args.length != 2) {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + "You must specify a name for the area you would like to create.");
                    return;
                }

                if(args[1].toLowerCase().equals("help")){
                    player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " This command creates a new area. Usage: /zone add <areaName>");
                    return;
                }

                // Generate a new multi-part command listener
                new MultiPartOnAreaAddCommand(player, args[1]);

                // Generate an item with unique tags to identify in the event.
                ItemStack itemStack = new ItemStack(Material.BARRIER);

                // Bind it to the player until the command is finished.
                itemStack.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);

                player.getInventory().addItem(itemStack);

                player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Use the barrier to create your area.");
                break;
            }
            case "list": {
                if(!player.hasPermission("zx.member.area.list")) {
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Permission denied.");
                    return;
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
                if(!player.hasPermission("zx.member.area.remove")) {
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Permission denied.");
                    return;
                }

                if (args.length != 2) {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "You must specify the name of the area to remove.");
                }
                try {
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

                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "You must specify the area to remove.");
                }
                break;
            }
            case "view": {
                if(!player.hasPermission("zx.member.area.view")) {
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Permission denied.");
                    return;
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
                }
                break;
            }
            case "allow": {
                if(!player.hasPermission("zx.member.area.allow")) {
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Permission denied.");
                    return;
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
                if(!player.hasPermission("zx.member.area.deny")) {
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Permission denied.");
                    return;
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
                if(!player.hasPermission("zx.member.area.info")) {
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Permission denied.");
                    return;
                }

                if(args.length != 2){
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "The proper syntax is /zone info <areaName>");
                }

                String areaName = args[1];

                if(areaName.toLowerCase().equals("help")){
                    player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "This command provides detailed information about a specific area. The proper syntax is /zone <protectionType> <areaName>");
                }

                Area area = ZoneX.areaManager.getArea(player.getUniqueId(), areaName);

                player.sendMessage(ChatColor.GOLD + "========" + ChatColor.GREEN + "   " + area.getAreaName() + "   " + ChatColor.GOLD + "========");
                player.sendMessage(ChatColor.GREEN + "Owner: " + ChatColor.WHITE + area.getOwner().getName());
                player.sendMessage(ChatColor.GREEN + "Protections: " + ChatColor.WHITE + area.getPlayerFriendlyProtectionString());
                player.sendMessage(ChatColor.GREEN + "Allowed Players: " + ChatColor.WHITE + area.getPlayerFriendlyWhitelistString());
                player.sendMessage(ChatColor.GREEN + "World: " + ChatColor.WHITE + area.getWorld());
                player.sendMessage(ChatColor.GREEN + "Boundary: " + ChatColor.WHITE + area.getPolygon().getPlayerFriendlyString());
                player.sendMessage(ChatColor.GREEN + "Area Size (blocks): " + ChatColor.WHITE + area.getPolygon().getArea());
                player.sendMessage(ChatColor.GOLD + "================================");

                break;
            }
            default: {
                if (args.length != 2) {
                    new HelpFactory().sendProtectionList(player);
                    return;
                }

                String areaName = args[1];

                Area area = ZoneX.areaManager.getArea(player.getUniqueId(), areaName);

                Protection protection = Protection.fromCommand(args[0]);

                if(protection != null){
                    if(player.hasPermission(protection.getPermissionString())) {
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

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
