package dev.timury.customkit;

import org.bukkit.plugin.java.JavaPlugin;

public final class CustomKit extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin has been enabled");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin has been disabled");
    }
}
