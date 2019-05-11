package org.mcnotify.commands.commands;

import net.minecraft.server.v1_14_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_14_R1.Particles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.mcnotify.MCNotify;
import org.mcnotify.areas.Area;
import org.mcnotify.areas.protection.Protection;
import org.mcnotify.commands.BaseCommandHandler;
import org.mcnotify.commands.multipartcommand.multipartcommands.MultiPartOnAreaAddCommand;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AreaCommandHandler extends CommandHandler {

    @Override
    public void onCommand(Player player, Command command, String s, String[] args) {
        switch(args[1].toLowerCase()) {
            case "add": {
                if (args.length != 3) {
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + "You must specify a name for the area you would like to create.");
                    return;
                }

                if(args[2].toLowerCase().equals("help")){
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " This command creates a new area. Usage: /mcnotify area add <areaName>");
                    return;
                }

                // Generate a new multi-part command listener
                new MultiPartOnAreaAddCommand(player, args[2]);

                // Generate an item with unique tags to identify in the event.
                ItemStack itemStack = new ItemStack(Material.BARRIER);

                // Bind it to the player until the command is finished.
                itemStack.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);

                player.getInventory().addItem(itemStack);

                player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Using the item, create your bounding area.");
                break;
            }
            case "list": {
                ArrayList<Area> playerAreas = MCNotify.areaManager.getAreas(player.getUniqueId());
                player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You have " + playerAreas.size() + " area(s).");
                if (playerAreas.size() > 0) {
                    player.sendMessage(ChatColor.GOLD + "===Area Id : Area Name===");
                }
                for (Area area : playerAreas) {
                    player.sendMessage("" + ChatColor.GRAY + area.getAreaId() + " : " + ChatColor.GREEN + area.getAreaName());
                }

                break;
            }
            case "remove": {
                if (args.length != 3) {
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You must specify the name of the area to remove.");
                }
                try {
                    String areaName = args[2];

                    if(areaName.toLowerCase().equals("help")){
                        player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " This command deletes an area. Usage: /mcnotify area remove <areaName>");
                        return;
                    }

                    if (MCNotify.areaManager.removeArea(player.getUniqueId(), areaName)) {
                        player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Area removed.");
                    } else {
                        player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Unable to remove area.");
                    }

                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You must specify the area id to remove.");
                }
                break;
            }
            case "view": {
                if (args.length != 3) {
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You must specify the name of the area to view.");
                }

                String areaName = args[2];


                if(areaName.toLowerCase().equals("help")){
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " This command shows an outline of the specified area. Usage: /mcnotify area view <areaName>");
                    return;
                }

                Area area = MCNotify.areaManager.getArea(player.getUniqueId(), areaName);

                if (area != null) {
                    if (BaseCommandHandler.particleManager.isViewingArea(player)) {
                        BaseCommandHandler.particleManager.stopAreaViewParticleThread(player);
                        player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Stopped viewing area.");
                    } else {
                        BaseCommandHandler.particleManager.startAreaVeiwParticleThread(player, area.getPolygon());
                        player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Started viewing area. Type the command again to hide the area.");
                    }
                }
                break;
            }
            case "allow": {
                if (args.length != 4) {
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " The proper syntax is /mcnotify area allow <playerName> <areaName>");
                }

                String playerName = args[2];
                String areaName = args[3];


                if(playerName.toLowerCase().equals("help")){
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " This command allows a player to bypass any protection on your area. Usage: /mcnotify area allow <playerName> <areaName>");
                    return;
                }

                Area area = MCNotify.areaManager.getArea(player.getUniqueId(), areaName);
                OfflinePlayer allowedPlayer = Bukkit.getOfflinePlayer(playerName);

                if(area.addWhitelist(allowedPlayer)){
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + allowedPlayer.getName() + " is now allowed in your area.");
                } else {
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + allowedPlayer.getName() + " is now allowed in your area.");
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.RED + "SAVE ERROR: " + ChatColor.GRAY + " This information was unable to be saved. Be aware that when the server is restarted these changes will NOT persist. Contact your server admins.");
                }
                break;
            }
            case "deny": {

                if (args.length != 4) {
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " The proper syntax is /mcnotify area deny <playerName> <areaName>");
                }

                String playerName = args[2];
                String areaName = args[3];


                if(playerName.toLowerCase().equals("help")){
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " This command restricts a player to the protections on the area. Usage: /mcnotify area deny <playerName> <areaName>");
                    return;
                }


                Area area = MCNotify.areaManager.getArea(player.getUniqueId(), areaName);
                OfflinePlayer allowedPlayer = Bukkit.getOfflinePlayer(playerName);

                if(area.removeWhitelist(allowedPlayer)){
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + allowedPlayer.getName() + " is now denied from your area.");
                } else {
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + allowedPlayer.getName() + " is now denied from your area.");
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.RED + "SAVE ERROR: " + ChatColor.GRAY + " This information was unable to be saved. Be aware that when the server is restarted these changes will NOT persist. Contact your server admins.");
                }

                break;
            }
            case "help": {
                player.sendMessage(ChatColor.GOLD + "========" + ChatColor.GREEN + "   [MCNotify] /mcnotify area   " + ChatColor.GOLD + "========");
                player.sendMessage(ChatColor.GOLD + "add <areaName>: " + ChatColor.GRAY + "Creates a new area.");
                player.sendMessage(ChatColor.GOLD + "remove <areaName>: " + ChatColor.GRAY + "Deletes an area you own.");
                player.sendMessage(ChatColor.GOLD + "list: " + ChatColor.GRAY + "Shows a list of all areas you own.");
                player.sendMessage(ChatColor.GOLD + "allow <playerName> <areaName>: " + ChatColor.GRAY + "Allows a player to bypass all protections in an area.");
                player.sendMessage(ChatColor.GOLD + "deny <playerName> <areaName>: " + ChatColor.GRAY + "Blocks a player based on the protections of the area.");
                player.sendMessage(ChatColor.GOLD + "<protectionType> <areaName>: " + ChatColor.GRAY + "Enables/disables the protectionType on the area. See /mcnotify protection help.");
                player.sendMessage(ChatColor.GOLD + "help: " + ChatColor.GRAY + "This information page.");
            }

            default: {
                if (args.length != 3) {
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " The proper syntax is /mcnotify area <protectionType> <areaName>");
                }

                String areaName = args[3];
                Area area = MCNotify.areaManager.getArea(player.getUniqueId(), areaName);

                Protection protection = Protection.fromCommand(args[2]);

                if(protection != null){
                    if(area.toggleProtection(protection)){
                        if(area.hasProtection(protection)) {
                            player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + protection.getEnabledMessage() + " in " + area.getAreaName());
                        } else {
                            player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + protection.getDisabledMessage() + " in " + area.getAreaName());
                        }
                    } else {
                        if(area.hasProtection(protection)) {
                            player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + protection.getEnabledMessage() + " in " + area.getAreaName());
                        } else {
                            player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + protection.getDisabledMessage() + " in " + area.getAreaName());
                        }
                        player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.RED + "SAVE ERROR: " + ChatColor.GRAY + " This information was unable to be saved. Be aware that when the server is restarted these changes will NOT persist. Contact your server admins.");
                    }
                } else {
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " That is not a valid protection type. See " + ChatColor.GREEN + " /mcnotify protection help " + ChatColor.GRAY + " for a list of protection types.");
                }

                break;
            }
        }
    }
}
