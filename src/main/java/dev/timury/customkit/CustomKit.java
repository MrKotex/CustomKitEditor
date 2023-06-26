package dev.timury.customkit;

import dev.timury.customkit.CMD.CustomKitCmd;
import dev.timury.customkit.Gui.ListenerAnvil;
import dev.timury.customkit.Listeners.Listeners;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class CustomKit extends JavaPlugin {

    @Getter
    private static CustomKit instance;

    public static ArrayList<Player> isInvisible = new ArrayList<>();

    public final HashMap<UUID, Boolean> isIneditroom = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin has been enabled");
        instance = this;
        Bukkit.getPluginCommand("ck").setExecutor(new CustomKitCmd());
        saveDefaultConfig();
        registerEvents();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin has been disabled");
        instance = null;
    }

    public void registerEvents(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ListenerAnvil(), this);
        pm.registerEvents(new Listeners(), this);
    }
}
