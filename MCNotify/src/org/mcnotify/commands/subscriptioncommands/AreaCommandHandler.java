package org.mcnotify.commands.subscriptioncommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.parser.ParseException;
import org.mcnotify.MCNotify;
import org.mcnotify.areas.Area;
import org.mcnotify.events.multipartcommandevents.multipartcommands.MultiPartOnAreaAddCommand;

import java.util.ArrayList;

public class AreaCommandHandler extends CommandHandler {

    @Override
    public void onCommand(Player player, Command command, String s, String[] args) {
        switch(args[1].toLowerCase()) {
            case "add":
                if(args.length != 4){
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
                ArrayList<Area> playerAreas = MCNotify.areaManager.getAreas(player);
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
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You must specify the area id to remove.");
                }
                try {
                    int areaId = Integer.valueOf(args[2]);

                    if(MCNotify.areaManager.removeAreaId(areaId)){
                        player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Area removed.");
                    } else {
                        player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Unable to remove area.");
                    }

                } catch (NumberFormatException e){
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You must specify the area id to remove.");
                }
                break;
            default:
                break;
        }
    }
}
