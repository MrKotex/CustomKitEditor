package dev.timury.customkit.CMD;

import dev.timury.customkit.CustomKit;
import dev.timury.customkit.util.HexColours;
import ga.strikepractice.StrikePractice;
import ga.strikepractice.api.StrikePracticeAPI;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class CustomKitCmd implements CommandExecutor, TabCompleter {

    private final CustomKit instance = CustomKit.getInstance();

    private static final String[] compl = { "edit", "save", "settings"};

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        StrikePracticeAPI api = StrikePractice.getAPI();
        Location location = player.getLocation();
        if(sender.hasPermission("CKA.customkit")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("save")) {
                    List<ItemStack> inventoryItems1 = new ArrayList<>();
                    for (ItemStack item : player.getInventory().getContents()) {
                        if (item != null) {
                            inventoryItems1.add(item.clone()); // Clone the item to avoid modifying the original stack
                        } else {
                            inventoryItems1.add(new ItemStack(Material.AIR)); // Add AIR to represent an empty slot
                        }
                    }
                    api.getPlayerKits(player).getCustomKit().setInventory(inventoryItems1);
                    api.getPlayerKits(player).getCustomKit().setHelmet(player.getInventory().getHelmet());
                    api.getPlayerKits(player).getCustomKit().setChestplate(player.getInventory().getChestplate());
                    api.getPlayerKits(player).getCustomKit().setLeggings(player.getInventory().getLeggings());
                    api.getPlayerKits(player).getCustomKit().setBoots(player.getInventory().getBoots());
                    player.teleport(api.getSpawnLocation());
                    player.setGameMode(GameMode.SURVIVAL);
                    if (instance.isIneditroom.get(player.getUniqueId()) != null && instance.isIneditroom.get(player.getUniqueId()).equals(false)) {
                        return false;
                    }
                    if (instance.isIneditroom.get(player.getUniqueId()) != null && instance.isIneditroom.get(player.getUniqueId()).equals(true)) {
                        player.setInvisible(false);
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            player.showPlayer(players);
                        }
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawnitem give " + player.getName());
                    api.getPlayerKits(player).getCustomKit().saveForStrikePractice();
                    api.getPlayerStats(player).save();
                    api.getPlayerSettings(player).save();
                    api.getPlayerKits(player).savePlayerKitsToFile();
                    instance.isIneditroom.remove(player.getUniqueId());

                }
                if (args[0].equalsIgnoreCase("get")) {
                    if (sender.hasPermission("StrikePractice.staff")) {
                        api.loadPlayerKits(player.getUniqueId()).getCustomKit().giveKit(player);
                    }
                }
                if (args[0].equalsIgnoreCase("edit")) {
                    if (instance.isIneditroom.containsKey(player.getUniqueId())) {
                        player.sendMessage(ChatColor.RED + "You are already in Custom Kit Editor");
                        return false;
                    } else if (api.isInQueue(player)) {
                        player.sendMessage(ChatColor.RED + "You have to left the queue to edit customKit");
                        return false;
                    } else if (api.isSpectator(player)) {
                        player.sendMessage(ChatColor.RED + "You have to left the spectator mode to edit customKit");
                        return false;
                    }
                    instance.isIneditroom.put(player.getUniqueId(), true);
                    CustomKit.isInvisible.add(player);
                    player.teleport(Objects.requireNonNull(instance.getConfig().getLocation("custom-kit-room")));
                    api.loadPlayerKits(player.getUniqueId()).getCustomKit().giveKit(player);
                    player.setGameMode(GameMode.CREATIVE);
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        player.hidePlayer(players);
                    }
                }
                if (args[0].equalsIgnoreCase("room")) {
                    if (sender.hasPermission("StrikePractice.staff")) {

                        instance.getConfig().set("custom-kit-room", location);
                        instance.saveConfig();
                        sender.sendMessage(HexColours.translate("#8aff9dCustom Kit Edit Room set successfully!"));
                    }
                }
                if (args[0].equalsIgnoreCase("settings")) {
                    if (sender.hasPermission("CKA.customkit")) {
                        instance.GuiSettings(player);
                    }
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> modules = new ArrayList<String>(Arrays.asList(compl));
        Set<String> compl = new HashSet<>();
        compl.add("edit");
        compl.add("save");

        StringUtil.copyPartialMatches(args[0], compl, modules);

        Collections.sort(modules);

        return modules;
    }
}

