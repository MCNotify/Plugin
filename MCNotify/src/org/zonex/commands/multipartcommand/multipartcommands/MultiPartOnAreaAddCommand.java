package org.zonex.commands.multipartcommand.multipartcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.zonex.ZoneX;
import org.zonex.areas.Area;
import org.zonex.commands.RegisterCommands;
import org.zonex.commands.multipartcommand.MultiPartCommand;
import org.zonex.areas.Polygon;

import java.awt.*;
import java.util.ArrayList;

/**
 * A multi-part command for creating an area.
 */
public class MultiPartOnAreaAddCommand extends MultiPartCommand {

    /**
     * The player's current polygon
     */
    Polygon poly = new Polygon();

    /**
     * An array of selected locations.
     */
    ArrayList<Location> selectedLocations = new ArrayList<>();

    /**
     * The name of the player's area.
     */
    String areaName;

    public MultiPartOnAreaAddCommand(Player player, String areaName) {
        super(player);
        this.areaName = areaName;
    }

    /**
     * The finish event for the area creation mutlipart command.
     * @param event the itemDrop event when the player drops the area creation tool.
     */
    @Override
    public void onFinish(Event event) {
        // Ensure that a shape with area was created.
        if(this.poly.getLength() >= 3){

            if(ZoneX.areaManager.addNewArea(new Area(player, this.poly, this.areaName, player.getWorld().getName()))){
                this.player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Area created successfully.");
            } else {
                this.player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Error creating area.");
            }

        } else {
            this.player.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Polygon needs at least 3 points. Failed to create area.");
        }
        this.cleanup();
    }

    /**
     * Event that is triggered when a new point is selected in the player's area.
     * @param event the PlaceBlock event that triggered the new point in the area
     */
    @Override
    public void onUpdateEvent(Event event) {
        // Event will be a PlayerInteractEvent type
        BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;
        Location location = placeEvent.getBlockPlaced().getLocation();
        this.poly.addPoint(new Point(location.getBlockX(), location.getBlockZ()));

        // If the polygon is big enough, start to show particles around the selected area.
        if(poly.getLength() == 2) {
            RegisterCommands.particleManager.startAreaVeiwParticleThread(((BlockPlaceEvent) event).getPlayer(), poly);
        }

        // Message the user the selected point
        ((BlockPlaceEvent) event).getPlayer().sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Selected point: " + location.getBlockX() + ", " + location.getBlockZ());

        // If the area is big enough, tell the user how to quit the creation of the area.
        if(poly.getLength() >= 3){
            if(poly.getLength() == 3) {
                ((BlockPlaceEvent) event).getPlayer().sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Drop the barrier when finished.");
            }
        } else {
            // If the polygon is not big enough, tell the player how many more points they need to select.
            ((BlockPlaceEvent) event).getPlayer().sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Polygon needs " + (3 - poly.getLength()) + " more points.");
        }

        // Cancel placing the barrier item.
        ((BlockPlaceEvent) event).setCancelled(true);
    }

    /**
     * Checks if the specified event is an event that would update the multi-part command.
     * This event checks for block place events with the iron block item that has the curse of binding.
     * @param event the event to check
     * @return if the event should progress the multi-part command.
     */
    @Override
    public boolean checkUpdateEvent(Event event) {

        // Cast to the player interact event
        if(!(event instanceof BlockPlaceEvent)){
            return false;
        }

        BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;

        if(!placeEvent.getItemInHand().getEnchantments().containsKey(Enchantment.BINDING_CURSE)){
            return false;
        }

        Block block = placeEvent.getBlockPlaced();

        if(block.getType() != Material.IRON_BLOCK){
            return false;
        }

        return true;
    }

    /**
     * Checks if the specified event is an event that would finish the multipart command.
     * For this command, the finish event is dropping the iron block.
     * @param event the event to check
     * @return if the event is an event that would finish the multipart command
     */
    @Override
    public boolean checkFinishEvent(Event event) {
        if(!(event instanceof PlayerDropItemEvent)){
            return false;
        }

        PlayerDropItemEvent dropItemEvent = (PlayerDropItemEvent) event;

        Item item = dropItemEvent.getItemDrop();
        ItemStack itemstack = dropItemEvent.getItemDrop().getItemStack();

        if(itemstack.getType() != Material.IRON_BLOCK){
            return false;
        }

        if(!itemstack.getEnchantments().containsKey(Enchantment.BINDING_CURSE)){
            return false;
        }

        // Ensure the barrier is deleted when dropped.
        ((PlayerDropItemEvent) event).getItemDrop().remove();

        return true;
    }

    /**
     * Stops the multi-part command and cleans up any started processes and data
     */
    @Override
    public void cleanup() {
        RegisterCommands.particleManager.stopAreaViewParticleThread(player);
        // Get the player's barrier item and destroy it.
        for(ItemStack itemstack : this.player.getInventory().getContents()){
            if(itemstack == null){
                continue;
            }
            if(itemstack.getType() == Material.IRON_BLOCK){
                if(itemstack.getEnchantments().containsKey(Enchantment.BINDING_CURSE)){
                    itemstack.setAmount(0);
                }
            }
        }

        // Remove their polygon.
        this.poly = null;
        // Remove all blocks they placed.
        for(Location l : selectedLocations){
            this.player.sendBlockChange(l, Material.AIR, (byte)0);
        }
    }
}
