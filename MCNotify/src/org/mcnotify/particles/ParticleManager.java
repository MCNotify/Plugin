package org.mcnotify.particles;

import net.minecraft.server.v1_14_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_14_R1.Particles;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.mcnotify.areas.Area;
import org.mcnotify.areas.Polygon;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ParticleManager {

    HashMap<UUID, Thread> particleThreads = new HashMap<>();

    public ParticleManager(){
    }

    public void startAreaVeiwParticleThread(Player player, Polygon poly){
        Thread particleThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(250);
                } catch (InterruptedException e) {
                    // Supresss stack trace when event is cancelled.
                    Thread.currentThread().interrupt();
                }
                for (int i = 0; i < poly.getLength() - 1; i++) {
                    // Get the next point
                    Point nextPoint = poly.getPoints().get(i + 1);

                    // Stop the thread if the player is too far away.
                    if(player.getLocation().distance(new Location(player.getWorld(), (float)nextPoint.x, (float)player.getLocation().getBlockY(), (float)nextPoint.y)) > 150){
                        Thread.currentThread().interrupt();
                    }

                    for (int j = 0; j < 15; j++) {

                        if(!player.isOnline()){
                            Thread.currentThread().interrupt();
                        }

                        Point interp = interpolate(poly.getPoints().get(i), poly.getPoints().get(i + 1), (float) j / 15);
                        PacketPlayOutWorldParticles packet1 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x + 0.5f, (float) player.getLocation().getY(), (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                        PacketPlayOutWorldParticles packet2 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x + 0.5f, (float) player.getLocation().getY() + 2, (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                        PacketPlayOutWorldParticles packet3 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x + 0.5f, (float) player.getLocation().getY() + 4, (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                        PacketPlayOutWorldParticles packet4 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x + 0.5f, (float) player.getLocation().getY() - 2, (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                        ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet1);
                        ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet2);
                        ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet3);
                        ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet4);
                    }
                }
                // Link the last and first points.
                for(int j = 0; j < 15; j++){

                    if(!player.isOnline()){
                        Thread.currentThread().interrupt();
                    }

                    Point interp = interpolate(poly.getPoints().get(0), poly.getPoints().get(poly.getLength() - 1), (float) j / 15);
                    PacketPlayOutWorldParticles packet1 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x+ 0.5f, (float) player.getLocation().getY(), (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                    PacketPlayOutWorldParticles packet2 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x+ 0.5f, (float) player.getLocation().getY() + 2, (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                    PacketPlayOutWorldParticles packet3 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x+ 0.5f, (float) player.getLocation().getY() + 4, (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                    PacketPlayOutWorldParticles packet4 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x+ 0.5f, (float) player.getLocation().getY() - 2, (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                    ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet1);
                    ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet2);
                    ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet3);
                    ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet4);
                }
            }
        });

        particleThread.start();
        particleThreads.put(player.getUniqueId(), particleThread);
    }

    public void stopAreaViewParticleThread(Player player){
        Thread thread = particleThreads.get(player.getUniqueId());
        thread.interrupt();
        particleThreads.remove(player.getUniqueId());
    }

    public boolean isViewingArea(Player player){
        return this.particleThreads.get(player.getUniqueId()) != null;
    }

    private Point interpolate(Point p1, Point p2, float scale){
        Point interp = new Point();
        interp.x = (int)(p1.x + (scale)*(p2.x - p1.x));
        interp.y = (int)(p1.y + (scale)*(p2.y - p1.y));
        return interp;
    }



}
