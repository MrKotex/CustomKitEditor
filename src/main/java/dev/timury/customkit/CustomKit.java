package dev.timury.customkit;

import dev.timury.customkit.CMD.CustomKitCmd;
import dev.timury.customkit.Listeners.Listeners;
import dev.timury.customkit.util.HexColours;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import ga.strikepractice.StrikePractice;
import ga.strikepractice.api.StrikePracticeAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class CustomKit extends JavaPlugin {

    @Getter
    private static CustomKit instance;

    StrikePracticeAPI api = StrikePractice.getAPI();

    public static ArrayList<Player> isInvisible = new ArrayList<>();

    public final HashMap<UUID, Boolean> isIneditroom = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin has been enabled");
        instance = this;
        registerEvents();
        Bukkit.getPluginCommand("ck").setExecutor(new CustomKitCmd());
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin has been disabled");
        instance = null;
    }

    public void registerEvents(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new Listeners(), this);
    }

    public void GuiSettings (Player player){
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glass_meta = glass.getItemMeta();
        glass_meta.setDisplayName(HexColours.translate(" "));
        glass.setItemMeta(glass_meta);


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
        off_horse_meta.setDisplayName(HexColours.translate("#ff9447Horse:" + "#ff4747 OFF"));
        off_horse.setItemMeta(off_horse_meta);

        Gui gui = new Gui(3, HexColours.translate("#00fa19CustomKit Settings"));

        GuiItem glassgui = new GuiItem(glass, event -> {
            event.setCancelled(true);
        });

        GuiItem onbuild = new GuiItem(on_build, event -> {
            event.setCancelled(true);
            api.getPlayerKits(player).getCustomKit().setBuild(false);
            player.sendMessage(HexColours.translate("#f53838Build was disabled!"));
            gui.close(player);
        });
        GuiItem offbuild = new GuiItem(off_build, event -> {
            event.setCancelled(true);
            api.getPlayerKits(player).getCustomKit().setBuild(true);
            player.sendMessage(HexColours.translate("#f53838Build was enabled!"));
            gui.close(player);
        });

        GuiItem onhorse = new GuiItem(on_horse, event -> {
            event.setCancelled(true);
            api.getPlayerKits(player).getCustomKit().setHorse(false);
            player.sendMessage(HexColours.translate("#f53838Horese mode was disabled!"));
            gui.close(player);
        });

        GuiItem offhorse = new GuiItem(off_horse, event -> {
            event.setCancelled(true);
            api.getPlayerKits(player).getCustomKit().setHorse(true);
            player.sendMessage(HexColours.translate("#f53838Horese mode was enabled!"));
            gui.close(player);
        });
        gui.getFiller().fillBetweenPoints(0,0,8,18, glassgui);

        if(api.getPlayerKits(player).getCustomKit().isBuild()){
            gui.setItem(12, onbuild);
        }else {
            gui.setItem(12, offbuild);
        }

        if(api.getPlayerKits(player).getCustomKit().isHorse()){
            gui.setItem(14, onhorse);
        }else{
            gui.setItem(14, offhorse);
        }



        gui.open(player);
    }
}
