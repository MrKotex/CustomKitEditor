package dev.timury.customkit;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomKit extends JavaPlugin {

    @Getter
    private static CustomKit instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin has been enabled");
        instance = this;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin has been disabled");
        instance = null;
    }
}
