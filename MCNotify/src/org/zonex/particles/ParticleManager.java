package org.zonex.particles;

import net.minecraft.server.v1_14_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_14_R1.Particles;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.zonex.areas.Polygon;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Manages displaying particles to players to ensure that only one particle system is being emitted at a time.
 */
public class ParticleManager {

    // A map of player's and threads that particles are running in
    HashMap<UUID, Thread> particleThreads = new HashMap<>();

    /**
     * Default constructor
     */
    public ParticleManager(){
    }

    /**
     * Starts showing the bounds of a polygon to a player
     * @param player the player to show the polygon to
     * @param poly the polygon to show
     */
    public void startAreaVeiwParticleThread(Player player, Polygon poly){
        // Creates a new thread to spawn particles
        Thread particleThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Show particles every 250ms
                    TimeUnit.MILLISECONDS.sleep(250);
                } catch (InterruptedException e) {
                    // Supresss stack trace when event is cancelled.
                    Thread.currentThread().interrupt();
                }

                // Loop through the points in the polygon and display a line between each point of the polygon.
                for (int i = 0; i < poly.getLength() - 1; i++) {
                    // Get the next point in the polygon
                    Point nextPoint = poly.getPoints().get(i + 1);

                    // Stop the thread if the player is too far away from the polygon.
                    if(player.getLocation().distance(new Location(player.getWorld(), (float)nextPoint.x, (float)player.getLocation().getBlockY(), (float)nextPoint.y)) > 150){
                        this.stopAreaViewParticleThread(player);
                    }

                    // Interpolate 15 points between two points of the polygon to show particles on
                    for (int j = 0; j < 15; j++) {
                        // Stop thread if the player logs off in the middle of the thread
                        if(!player.isOnline()){
                            this.stopAreaViewParticleThread(player);
                        }

                        // Get the interpolated point
                        Point interp = interpolate(poly.getPoints().get(i), poly.getPoints().get(i + 1), (float) j / 15);

                        // Show 4 different heights to 'cage' the player in the polygon to ensure it is visible and doesn't get hidden behind trees/terrain
                        PacketPlayOutWorldParticles packet1 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x + 0.5f, (float) player.getLocation().getY(), (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                        PacketPlayOutWorldParticles packet2 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x + 0.5f, (float) player.getLocation().getY() + 2, (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                        PacketPlayOutWorldParticles packet3 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x + 0.5f, (float) player.getLocation().getY() + 4, (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                        PacketPlayOutWorldParticles packet4 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x + 0.5f, (float) player.getLocation().getY() - 2, (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);

                        // Send the particle packets
                        ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet1);
                        ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet2);
                        ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet3);
                        ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet4);
                    }
                }
                // Link the last and first points of the polygon to enclose it.
                for(int j = 0; j < 15; j++){

                    if(!player.isOnline()){
                        this.stopAreaViewParticleThread(player);
                    }
                    // Interpolated point
                    Point interp = interpolate(poly.getPoints().get(0), poly.getPoints().get(poly.getLength() - 1), (float) j / 15);


                    // Show 4 different heights to 'cage' the player in the polygon to ensure it is visible and doesn't get hidden behind trees/terrain
                    PacketPlayOutWorldParticles packet1 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x+ 0.5f, (float) player.getLocation().getY(), (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                    PacketPlayOutWorldParticles packet2 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x+ 0.5f, (float) player.getLocation().getY() + 2, (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                    PacketPlayOutWorldParticles packet3 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x+ 0.5f, (float) player.getLocation().getY() + 4, (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);
                    PacketPlayOutWorldParticles packet4 = new PacketPlayOutWorldParticles(Particles.NOTE, true, (float) interp.x+ 0.5f, (float) player.getLocation().getY() - 2, (float) interp.y+ 0.5f, (float) 0, (float) 0, (float) 0, (float) 0, 1);

                    // Send the particle packets
                    ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet1);
                    ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet2);
                    ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet3);
                    ((CraftPlayer) (player)).getHandle().playerConnection.sendPacket(packet4);
                }
            }
        });

        // Start the thread and add a reference to the thread in a hash map to be able to stop the thread later.
        particleThread.start();
        particleThreads.put(player.getUniqueId(), particleThread);
    }

    /**
     * Stops a player from viewing a polygon's area
     * @param player the player to stop the particles for
     */
    public void stopAreaViewParticleThread(Player player){
        Thread thread = particleThreads.get(player.getUniqueId());
        if(thread != null) {
            thread.interrupt();
            particleThreads.remove(player.getUniqueId());
        }
    }

    /**
     * Checks if a player is currently viewing an area polygon
     * @param player the player to check is viewing an area
     * @return if the player is already viewing an area
     */
    public boolean isViewingArea(Player player){
        return this.particleThreads.get(player.getUniqueId()) != null;
    }

    /**
     * Interpolates a new point between two points at the given scale
     * @param p1 the first point to interpolate between
     * @param p2 the second point to interpolate between
     * @param scale A float from 0-1 indicating how far between the two points to interpolate.
     * @return An interpoatled point between the two points at the given percentage
     */
    private Point interpolate(Point p1, Point p2, float scale){
        Point interp = new Point();
        interp.x = (int)(p1.x + (scale)*(p2.x - p1.x));
        interp.y = (int)(p1.y + (scale)*(p2.y - p1.y));
        return interp;
    }



}
