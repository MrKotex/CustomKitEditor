package dev.timury.customkit.Listeners;

import dev.timury.customkit.CustomKit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {

    private final CustomKit instance = CustomKit.getInstance();

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        instance.isIneditroom.remove(player.getUniqueId());
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
        if(player.getWorld().getName().equals("world")){
            if(!player.hasPermission("StrikePractice.staff")){
                event.setCancelled(true);
            }
        }
    }

}
