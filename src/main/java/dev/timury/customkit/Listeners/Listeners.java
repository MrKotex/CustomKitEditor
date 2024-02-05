package dev.timury.customkit.Listeners;

import dev.timury.customkit.CustomKit;
import dev.timury.customkit.util.HexColours;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class Listeners implements Listener {

    private final CustomKit instance = CustomKit.getInstance();

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        instance.isIneditroom.remove(player.getUniqueId());
        player.setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler
    public void HorseDrop(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Horse) {
            event.getDrops().clear();
        }
    }

    @EventHandler
    public void DropItems(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        if(instance.isIneditroom.get(player.getUniqueId()) != null && instance.isIneditroom.get(player.getUniqueId()).equals(true) || player.getWorld().getName().equals("world")){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onCraft(CraftItemEvent event){
        Player player = (Player) event.getWhoClicked();
        if(instance.isIneditroom.get(player.getUniqueId()) != null && instance.isIneditroom.get(player.getUniqueId()).equals(true) || player.getWorld().getName().equals("world")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlaceBlock(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if(player.getWorld().getName().equals(instance.editRoomWorld())){
            if(!player.hasPermission("StrikePractice.staff")){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if(event.getEntity() instanceof Player player){
            if(instance.isIneditroom.get(player.getUniqueId()) != null && instance.isIneditroom.get(player.getUniqueId()).equals(true)){
                player.setCanPickupItems(false);
            }
        }
    }
    @EventHandler
    public void DestroyBlock(BlockBreakEvent event){
        Player player = event.getPlayer();
        if(player.getWorld().getName().equals(instance.editRoomWorld())){
            if(!player.hasPermission("StrikePractice.staff")){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if(instance.isIneditroom.get(player.getUniqueId()) != null && instance.isIneditroom.get(player.getUniqueId()).equals(true)){
            String used = event.getMessage().substring(1);
            List<String> allowed = instance.getConfig().getStringList("access");
            boolean isAllowed = false;
            for (String command : allowed) {
                StringBuilder current = new StringBuilder();
                for (int i = 1; i <= used.split(" ").length; i++) {
                    current.append(used.split(" ")[i - 1]).append(" ");
                    if (command.toLowerCase().trim().equals(current.toString().toLowerCase().trim())) {
                        isAllowed = true;
                        break;
                    }
                }
            }
            if (!isAllowed) {
                String message = HexColours.translate(instance.getConfig().getString("access-mess"));
                event.getPlayer().sendMessage(message);
                event.setCancelled(true);
            }
        }
    }

}
