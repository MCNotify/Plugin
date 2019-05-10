package org.mcnotify.areas.protection;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.mcnotify.MCNotify;
import org.mcnotify.areas.Area;

import java.util.EnumSet;
import java.util.Set;

public class BlockProtectionEvents implements Listener {

    private static final Set<Material> BEDS = EnumSet.of(
            Material.BLACK_BED,
            Material.BLUE_BED,
            Material.BROWN_BED,
            Material.CYAN_BED,
            Material.GRAY_BED,
            Material.GREEN_BED,
            Material.LIGHT_BLUE_BED,
            Material.LIGHT_GRAY_BED,
            Material.LIME_BED,
            Material.MAGENTA_BED,
            Material.ORANGE_BED,
            Material.PINK_BED,
            Material.PURPLE_BED,
            Material.RED_BED,
            Material.WHITE_BED,
            Material.YELLOW_BED
    );

    private static final Set<Material> INTERACTABLES = EnumSet.of(
            Material.BREWING_STAND,
            Material.FURNACE,
            Material.CRAFTING_TABLE,
            Material.LOOM,
            Material.ENCHANTING_TABLE,
            Material.STONECUTTER,
            Material.LECTERN,
            Material.BLAST_FURNACE,
            Material.CARTOGRAPHY_TABLE,
            Material.ANVIL,
            Material.GRINDSTONE,
            Material.BELL,
            Material.CAULDRON,
            Material.COMMAND_BLOCK,
            Material.NOTE_BLOCK,
            Material.JUKEBOX,
            Material.PUMPKIN);

    private static final Set<Material> REDSTONE_INTERACTABLES = EnumSet.of(
            Material.BIRCH_BUTTON,
            Material.ACACIA_BUTTON,
            Material.DARK_OAK_BUTTON,
            Material.JUNGLE_BUTTON,
            Material.OAK_BUTTON,
            Material.SPRUCE_BUTTON,
            Material.STONE_BUTTON,
            Material.LEVER,
            Material.ACACIA_PRESSURE_PLATE,
            Material.BIRCH_PRESSURE_PLATE,
            Material.DARK_OAK_PRESSURE_PLATE,
            Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
            Material.JUNGLE_PRESSURE_PLATE,
            Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
            Material.OAK_PRESSURE_PLATE,
            Material.SPRUCE_PRESSURE_PLATE,
            Material.STONE_PRESSURE_PLATE,
            Material.HOPPER,
            Material.MINECART,
            Material.TRAPPED_CHEST,
            Material.COMPARATOR,
            Material.REPEATER
    );

    @EventHandler
    public void onBlockBreak(BlockBreakEvent breakEvent){
        Area areaAtLocation = MCNotify.areaManager.getAreaAtLocation(breakEvent.getBlock().getLocation());
        if(areaAtLocation != null){
            if(!areaAtLocation.isAllowed(breakEvent.getPlayer())){
                breakEvent.setCancelled(true);
                breakEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " This area is protected.");
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent placeEvent){
        Area areaAtLocation = MCNotify.areaManager.getAreaAtLocation(placeEvent.getBlock().getLocation());
        if(areaAtLocation != null){
            if(!areaAtLocation.isAllowed(placeEvent.getPlayer())){
                placeEvent.setCancelled(true);
                placeEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " This area is protected.");
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent explodeEvent){
        if(explodeEvent.getEntityType() == EntityType.CREEPER || explodeEvent.getEntityType() == EntityType.GHAST || explodeEvent.getEntityType() == EntityType.WITHER || explodeEvent.getEntityType() == EntityType.FIREBALL || explodeEvent.getEntityType() == EntityType.MINECART_TNT || explodeEvent.getEntityType() == EntityType.PRIMED_TNT) {
            Area areaAtLocation = MCNotify.areaManager.getAreaAtLocation(explodeEvent.getLocation());
            if (areaAtLocation != null) {
                if (areaAtLocation.hasProtection(Protection.MOB_PROTECT)) {
                    explodeEvent.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent interactEvent){

        if(interactEvent.getClickedBlock() == null){
            return;
        }

        Area areaAtLocation = MCNotify.areaManager.getAreaAtLocation(interactEvent.getClickedBlock().getLocation());

        if(areaAtLocation == null){
            return;
        }
        if(areaAtLocation.isAllowed(interactEvent.getPlayer())){
            return;
        }


        Material interactedBlock = interactEvent.getClickedBlock().getType();

        if(     interactedBlock == Material.CHEST ||
                interactedBlock.toString().contains("SHULKER") ||
                interactedBlock == Material.HOPPER ||
                interactedBlock == Material.HOPPER_MINECART ||
                interactedBlock == Material.CHEST_MINECART ||
                interactedBlock == Material.ENDER_CHEST){

            if(areaAtLocation.hasProtection(Protection.CHEST_LOCK)){
                interactEvent.setCancelled(true);
                interactEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " This area is protected.");
                return;
            }
        }

        if(INTERACTABLES.contains(interactedBlock) || BEDS.contains(interactedBlock)) {
            if(areaAtLocation.hasProtection(Protection.NO_INTERACT)){
                interactEvent.setCancelled(true);
                interactEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " This area is protected.");
                return;
            }
        }

        if(interactedBlock.toString().contains("DOOR") || interactedBlock.toString().contains("FENCE")){
            if(areaAtLocation.hasProtection(Protection.DOOR_LOCK)){
                interactEvent.setCancelled(true);
                interactEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " This area is protected.");
                return;
            }
        }

        if(REDSTONE_INTERACTABLES.contains(interactedBlock)){
            if(areaAtLocation.hasProtection(Protection.NO_REDSTONE)){
                interactEvent.setCancelled(true);
                interactEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " This area is protected.");
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent playerMoveEvent){

        Area areaAtDestination = MCNotify.areaManager.getAreaAtLocation(playerMoveEvent.getTo());
        if(areaAtDestination == null){
            return;
        }
        if(areaAtDestination.isAllowed(playerMoveEvent.getPlayer())){
            return;
        }
        if(areaAtDestination.hasProtection(Protection.NO_ENTER)){
            playerMoveEvent.setCancelled(true);
            playerMoveEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " This area is protected.");
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent blockIgniteEvent){

        Area areaAtDestination = MCNotify.areaManager.getAreaAtLocation(blockIgniteEvent.getIgnitingBlock().getLocation());
        if(areaAtDestination == null){
            return;
        }
        if(areaAtDestination.isAllowed(blockIgniteEvent.getPlayer())){
            return;
        }
        if(areaAtDestination.hasProtection(Protection.NO_FIRE)){
            blockIgniteEvent.setCancelled(true);
            if(blockIgniteEvent.getPlayer() != null) {
                blockIgniteEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " This area is protected.");
            }
        }
    }

    @EventHandler
    public void onBucketEvent(PlayerBucketEmptyEvent emptyBucketEvent){
        Area areaAtDestination = MCNotify.areaManager.getAreaAtLocation(emptyBucketEvent.getBlockClicked().getLocation());
        if(areaAtDestination == null){
            emptyBucketEvent.setCancelled(true);
            emptyBucketEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You must be in a claimed area to empty a bucket.");
            return;
        }

        if(!areaAtDestination.isAllowed(emptyBucketEvent.getPlayer())){
            emptyBucketEvent.setCancelled(true);
            emptyBucketEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " This area is protected.");
        }

    }
}
