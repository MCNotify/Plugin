package org.mcnotify.commands.commands;

import net.minecraft.server.v1_14_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_14_R1.Particles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.mcnotify.MCNotify;
import org.mcnotify.areas.Area;
import org.mcnotify.commands.BaseCommandHandler;
import org.mcnotify.commands.multipartcommand.multipartcommands.MultiPartOnAreaAddCommand;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AreaCommandHandler extends CommandHandler {

    @Override
    public void onCommand(Player player, Command command, String s, String[] args) {
        switch(args[1].toLowerCase()) {
            case "add":
                if(args.length != 3){
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + "You must specify a name for the area you would like to create.");
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
            case "list":
                ArrayList<Area> playerAreas = MCNotify.areaManager.getAreas(player.getUniqueId());
                player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You have " + playerAreas.size() + " area(s).");
                if(playerAreas.size() > 0){
                    player.sendMessage(ChatColor.GOLD + "===Area Id : Area Name===");
                }
                for(Area area : playerAreas){
                    player.sendMessage("" + ChatColor.GRAY + area.getAreaId() + " : " + ChatColor.GREEN + area.getAreaName());
                }

                break;
            case "remove":
                if(args.length != 3){
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You must specify the name of the area to remove.");
                }
                try {
                    String areaName = args[2];

                    if(MCNotify.areaManager.removeArea(player.getUniqueId(), areaName)){
                        player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Area removed.");
                    } else {
                        player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Unable to remove area.");
                    }

                } catch (NumberFormatException e){
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You must specify the area id to remove.");
                }
                break;
            case "view":
                if(args.length != 3){
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You must specify the name of the area to view.");
                }

                String areaName = args[2];
                Area area = MCNotify.areaManager.getArea(player.getUniqueId(), areaName);

                if(area != null) {
                    if (BaseCommandHandler.particleManager.isViewingArea(player)) {
                        BaseCommandHandler.particleManager.stopAreaViewParticleThread(player);
                        player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Stopped viewing area.");
                    } else {
                        BaseCommandHandler.particleManager.startAreaVeiwParticleThread(player, area.getPolygon());
                        player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Started viewing area. Type the command again to hide the area.");
                    }
                }
            default:
                break;
        }
    }
}
