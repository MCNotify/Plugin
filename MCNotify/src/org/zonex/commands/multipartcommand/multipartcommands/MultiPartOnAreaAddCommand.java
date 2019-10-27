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

public class MultiPartOnAreaAddCommand extends MultiPartCommand {

    Polygon poly = new Polygon();
    ArrayList<Location> selectedLocations = new ArrayList<>();
    String areaName;

    public MultiPartOnAreaAddCommand(Player player, String areaName) {
        super(player);
        this.areaName = areaName;
    }

    @Override
    public void onFinish(Event event) {
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

    @Override
    public void onUpdateEvent(Event event) {
        // Event will be a PlayerInteractEvent type
        BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;
        Location location = placeEvent.getBlockPlaced().getLocation();
        this.poly.addPoint(new Point(location.getBlockX(), location.getBlockZ()));

        // Show particles around the area that is selected.
        if(poly.getLength() == 2) {
            RegisterCommands.particleManager.startAreaVeiwParticleThread(((BlockPlaceEvent) event).getPlayer(), poly);
        }


        ((BlockPlaceEvent) event).getPlayer().sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Selected point: " + location.getBlockX() + ", " + location.getBlockZ());

        if(poly.getLength() >= 3){
            if(poly.getLength() == 3) {
                ((BlockPlaceEvent) event).getPlayer().sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Drop the barrier when finished.");
            }
        } else {
            ((BlockPlaceEvent) event).getPlayer().sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.GRAY + "Polygon needs " + (3 - poly.getLength()) + " more points.");
        }

        ((BlockPlaceEvent) event).setCancelled(true);
    }

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
