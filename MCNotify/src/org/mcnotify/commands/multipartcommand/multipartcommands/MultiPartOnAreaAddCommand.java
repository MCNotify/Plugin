package org.mcnotify.commands.multipartcommand.multipartcommands;

import net.minecraft.server.v1_14_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_14_R1.Particles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.mcnotify.MCNotify;
import org.mcnotify.areas.Area;
import org.mcnotify.commands.multipartcommand.MultiPartCommand;
import org.mcnotify.areas.Polygon;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MultiPartOnAreaAddCommand extends MultiPartCommand {

    Polygon poly = new Polygon();
    ArrayList<Location> selectedLocations = new ArrayList<>();
    String areaName;
    Thread particleThread;

    public MultiPartOnAreaAddCommand(Player player, String areaName) {
        super(player);
        this.areaName = areaName;
    }

    @Override
    public void onFinish(Event event) {
        this.particleThread.interrupt();
        if(this.poly.getLength() >= 3){

            if(MCNotify.areaManager.addArea(new Area(player, this.poly, this.areaName, player.getWorld().getName()))){
                this.player.sendMessage(ChatColor.GREEN + "[MCNotify] " + ChatColor.GRAY + "Area created successfully.");
            } else {
                this.player.sendMessage(ChatColor.GREEN + "[MCNotify] " + ChatColor.GRAY + "Error creating area.");
            }

        } else {
            this.player.sendMessage(ChatColor.GREEN + "[MCNotify] " + ChatColor.GRAY + "Polygon needs at least 3 points. Failed to create area.");
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
        if(poly.getLength() == 2){

            particleThread = new Thread(() -> {
                while(true) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(75);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < poly.getLength() - 1; i++) {
                        // Get the next point
                        Point nextPoint = poly.getPoints().get(i + 1);

                        for (int j = 0; j < 15; j++) {
                            Point interp = interpolate(poly.getPoints().get(i), poly.getPoints().get(i + 1), (float) j / 15);
                            PacketPlayOutWorldParticles packet1 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x, (float) player.getLocation().getY(), (float) interp.y, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                            PacketPlayOutWorldParticles packet2 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x, (float) player.getLocation().getY() + 2, (float) interp.y, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                            PacketPlayOutWorldParticles packet3 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x, (float) player.getLocation().getY() + 4, (float) interp.y, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                            PacketPlayOutWorldParticles packet4 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x, (float) player.getLocation().getY() - 2, (float) interp.y, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                            ((CraftPlayer) ((BlockPlaceEvent) event).getPlayer()).getHandle().playerConnection.sendPacket(packet1);
                            ((CraftPlayer) ((BlockPlaceEvent) event).getPlayer()).getHandle().playerConnection.sendPacket(packet2);
                            ((CraftPlayer) ((BlockPlaceEvent) event).getPlayer()).getHandle().playerConnection.sendPacket(packet3);
                            ((CraftPlayer) ((BlockPlaceEvent) event).getPlayer()).getHandle().playerConnection.sendPacket(packet4);
                        }
                    }
                }
            });

            particleThread.start();
        }


        ((BlockPlaceEvent) event).getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify] " + ChatColor.GRAY + "Selected point: " + location.getBlockX() + ", " + location.getBlockZ());

        if(poly.getLength() >= 3){
            if(poly.getLength() == 3) {
                ((BlockPlaceEvent) event).getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify] " + ChatColor.GRAY + "Drop the barrier when finished.");
            }
        } else {
            ((BlockPlaceEvent) event).getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify] " + ChatColor.GRAY + "Polygon needs " + (3 - poly.getLength()) + " more points.");
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

        if(block.getType() != Material.BARRIER){
            return false;
        }

        return true;
    }

    private Point interpolate(Point p1, Point p2, float scale){
        Point interp = new Point();
        interp.x = (int)(p1.x + (scale)*(p2.x - p1.x));
        interp.y = (int)(p1.y + (scale)*(p2.y - p1.y));
        return interp;
    }

    @Override
    public boolean checkFinishEvent(Event event) {
        if(!(event instanceof PlayerDropItemEvent)){
            return false;
        }

        PlayerDropItemEvent dropItemEvent = (PlayerDropItemEvent) event;

        Item item = dropItemEvent.getItemDrop();
        ItemStack itemstack = dropItemEvent.getItemDrop().getItemStack();

        if(itemstack.getType() != Material.BARRIER){
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
        // Get the player's barrier item and destroy it.
        for(ItemStack itemstack : this.player.getInventory().getContents()){
            if(itemstack == null){
                continue;
            }
            if(itemstack.getType() == Material.BARRIER){
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