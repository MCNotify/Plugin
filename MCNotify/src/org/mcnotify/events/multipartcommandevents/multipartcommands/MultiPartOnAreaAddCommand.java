package org.mcnotify.events.multipartcommandevents.multipartcommands;

import net.minecraft.server.v1_14_R1.DataWatcher;
import net.minecraft.server.v1_14_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_14_R1.Particle;
import net.minecraft.server.v1_14_R1.Particles;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.mcnotify.MCNotify;
import org.mcnotify.areas.Area;
import org.mcnotify.events.multipartcommandevents.MultiPartCommand;
import org.mcnotify.areas.Polygon;
import org.mcnotify.authenticator.Response;

import java.awt.*;
import java.io.IOException;
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
            JSONObject areaJson = new JSONObject();
            areaJson.put("uuid", this.player.getUniqueId().toString());
            areaJson.put("polygon", poly.getJson().toJSONString());
            areaJson.put("area_name", this.areaName);

            try {

                Response response = MCNotify.requestManager.sendRequest("POST", "areas.php", areaJson.toJSONString());

                if(response.getResponseCode() == 200){

                    try {
                        JSONObject areaResponse = response.getResponseBody();
                        int areaId = Math.toIntExact((Long)areaResponse.get("area_id"));

                        MCNotify.areaManager.addArea(new Area(areaId, player, this.poly, this.areaName, player.getWorld().getName()));
                        this.player.sendMessage(ChatColor.GREEN + "[MCNotify] " + ChatColor.GRAY + "Area created successfully.");
                        this.player.sendMessage(poly.getJson().toJSONString());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    this.player.sendMessage(ChatColor.GREEN + "[MCNotify] " + ChatColor.GRAY + "Error sending request.");
                }

            } catch (IOException e) {
                e.printStackTrace();
                this.cleanup();
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
                        TimeUnit.MILLISECONDS.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < poly.getLength() - 1; i++) {
                        // Get the next point
                        Point nextPoint = poly.getPoints().get(i + 1);

                        for (int j = 0; j < 15; j++) {
                            Point interp = interpolate(poly.getPoints().get(i), poly.getPoints().get(i + 1), (float) j / 15);
                            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x, (float) player.getLocation().getY(), (float) interp.y, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                            ((CraftPlayer) ((BlockPlaceEvent) event).getPlayer()).getHandle().playerConnection.sendPacket(packet);
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
