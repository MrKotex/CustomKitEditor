package dev.timury.customkit.CMD;

import dev.timury.customkit.CustomKit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EnchantCmd implements CommandExecutor {

    private final CustomKit instance = CustomKit.getInstance();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if(player.hasPermission("CKA.customkit")){
            if(instance.isIneditroom.get(player.getUniqueId())){

            }
        }
        return true;
    }
}
