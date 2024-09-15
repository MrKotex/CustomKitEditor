package dev.timury.customkit.listeners;

import dev.timury.customkit.CustomKit;
import dev.timury.customkit.util.HexColours;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

//TODO disable operator items
import java.util.List;

public class Listeners implements Listener {

    private final CustomKit instance = CustomKit.instance;

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        instance.isIneditroom.remove(player.getUniqueId());
        player.setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler
    public void DropItems(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        if(instance.isIneditroom.get(player.getUniqueId()) != null && instance.isIneditroom.get(player.getUniqueId()).equals(true) || player.getWorld().getName().equals("world")){
            event.setCancelled(true);
        }
    }

    public static void listMonsterEggs() {
        for (Material material : Material.values()) {
            if (material.name().endsWith("_SPAWN_EGG")) {
                System.out.println(material.name());
            }
        }
    }

    @EventHandler
    public void itemuse(PlayerInteractEvent e) {
        if (instance.isIneditroom.get(e.getPlayer().getUniqueId()) != null && instance.isIneditroom.get(e.getPlayer().getUniqueId()).equals(true) || e.getPlayer().getWorld().getName().equals("world")){
            Material itemType = e.getItem() != null ? e.getItem().getType() : null;
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && itemType.name().endsWith("_SPAWN_EGG")) {
                e.setCancelled(true);
            }
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block clickedBlock = e.getClickedBlock();
                // Check if the clicked block is not null and if it is an anvil
                if (clickedBlock != null && (clickedBlock.getType() == Material.ANVIL || clickedBlock.getType() == Material.CHIPPED_ANVIL || clickedBlock.getType() == Material.DAMAGED_ANVIL)) {
                    if(e.getPlayer().hasPermission("CKA.customkit")){
                        e.getPlayer().performCommand("ck save");
                    }
                }
            }
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
        if(player.getWorld().getName().equals(instance.editRoomWorld()) || instance.isIneditroom.containsKey(player.getUniqueId())){
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
                String message = HexColours.translate(instance.getConfig().getString("access-mess").replace("[", "").replace("]", ""));
                event.getPlayer().sendMessage(message);
                event.setCancelled(true);
            }
        }
    }

}
