package dev.timury.customkit.Gui;

import dev.timury.customkit.util.HexColours;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import ga.strikepractice.StrikePractice;
import ga.strikepractice.api.StrikePracticeAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ListenerAnvil implements Listener {

    StrikePracticeAPI api = StrikePractice.getAPI();
    @EventHandler
    public void onAnvilClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        Action action = e.getAction();

        if (action == Action.RIGHT_CLICK_BLOCK && ((Block) block).getType() == Material.ANVIL) {
            e.setCancelled(true);
            ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemStack golden = new ItemStack(Material.GOLD_BLOCK);

            ItemStack on_build = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemMeta on_build_meta = on_build.getItemMeta();
            on_build_meta.setDisplayName(HexColours.translate("#ff9447Build:" + "#78ff85 ON"));
            on_build.setItemMeta(on_build_meta);

            ItemStack off_build = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta off_build_meta = off_build.getItemMeta();
            off_build_meta.setDisplayName(HexColours.translate("#ff9447Build:" + "#ff4747 OFF"));
            off_build.setItemMeta(off_build_meta);

            ItemStack on_horse = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemMeta on_horse_meta = on_horse.getItemMeta();
            on_horse_meta.setDisplayName(HexColours.translate("#ff9447Horse:" + "#78ff85 ON"));
            on_horse.setItemMeta(on_horse_meta);

            ItemStack off_horse = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta off_horse_meta = off_horse.getItemMeta();
            off_horse_meta.setDisplayName(HexColours.translate("#ff9447Horse:" + "#78ff85 OFF"));
            off_horse.setItemMeta(off_horse_meta);

            Gui gui = new Gui(3, HexColours.translate("#00fa19CustomKit Settings"));

            GuiItem glassgui = new GuiItem(glass, event -> {
               event.setCancelled(true);
            });

            GuiItem apple = new GuiItem(golden, event -> {
                event.setCancelled(true);
                player.sendMessage("wrodks");
                player.closeInventory();
            });
            GuiItem onbuild = new GuiItem(on_build, event -> {
                event.setCancelled(true);
                api.getPlayerKits(player).getCustomKit().setBuild(false);
                player.sendMessage("wrodks");
                gui.close(player);
            });
            GuiItem offbuild = new GuiItem(off_build, event -> {
                event.setCancelled(true);
                api.getPlayerKits(player).getCustomKit().setBuild(true);
                player.sendMessage("wrodks");
                gui.close(player);
            });

            GuiItem onhorse = new GuiItem(on_horse, event -> {
                event.setCancelled(true);
                api.getPlayerKits(player).getCustomKit().setBuild(true);
                player.sendMessage("wrodks");
                gui.close(player);
            });

            GuiItem offhorse = new GuiItem(off_horse, event -> {
                event.setCancelled(true);
                api.getPlayerKits(player).getCustomKit().setBuild(true);
                player.sendMessage("wrodks");
                gui.close(player);
            });
            gui.getFiller().fillBetweenPoints(0,0,8,18, glassgui);

            if(api.getPlayerKits(e.getPlayer()).getCustomKit().isBuild()){
                gui.setItem(10, onbuild);
            }else {
                gui.setItem(10, offbuild);
            }

            if(api.getPlayerKits(e.getPlayer()).getCustomKit().isHorse()){
                gui.setItem(12, onhorse);
            }else{
                gui.setItem(10, offhorse);
            }

            gui.setItem(12, apple);
            gui.setItem(14, apple);
            gui.setItem(16, apple);


            gui.open(player);
        }
    }

}
