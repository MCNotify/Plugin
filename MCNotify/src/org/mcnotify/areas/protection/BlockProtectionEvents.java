package org.mcnotify.areas.protection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent breakEvent){
        Area areaAtLocation = MCNotify.areaManager.getAreaAtLocation(breakEvent.getBlock().getLocation());
        if(areaAtLocation != null){
            if(!areaAtLocation.isAllowed(breakEvent.getPlayer())){
                breakEvent.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent placeEvent){
        Area areaAtLocation = MCNotify.areaManager.getAreaAtLocation(placeEvent.getBlock().getLocation());
        if(areaAtLocation != null){
            if(!areaAtLocation.isAllowed(placeEvent.getPlayer())){
                placeEvent.setCancelled(true);
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
        Area areaAtLocation = MCNotify.areaManager.getAreaAtLocation(interactEvent.getClickedBlock().getLocation());

        if(areaAtLocation == null){
            return;
        }
        if(areaAtLocation.isAllowed(interactEvent.getPlayer())){
            return;
        }


        Material interactedBlock = interactEvent.getClickedBlock().getType();

        if(interactedBlock == Material.CHEST || interactedBlock == Material.SHULKER_BOX){
            if(areaAtLocation.hasProtection(Protection.CHEST_LOCK)){
                interactEvent.setCancelled(true);
            }
        }

        if(INTERACTABLES.contains(interactedBlock) || BEDS.contains(interactedBlock)) {
            if(areaAtLocation.hasProtection(Protection.NO_INTERACT)){
                interactEvent.setCancelled(true);
            }
        }

        if(interactedBlock.toString().contains("DOOR") || interactedBlock.toString().contains("FENCE")){
            if(areaAtLocation.hasProtection(Protection.DOOR_LOCK)){
                interactEvent.setCancelled(true);
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
        }
    }
}
