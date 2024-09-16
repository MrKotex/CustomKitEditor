package dev.timury.customkit.commands;

import dev.timury.customkit.CustomKit;
import dev.timury.customkit.util.HexColours;
import ga.strikepractice.StrikePractice;
import ga.strikepractice.api.StrikePracticeAPI;
import ga.strikepractice.playerkits.PlayerKits;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class CustomKitCmd implements CommandExecutor, TabCompleter {

    private final CustomKit plugin;

    public CustomKitCmd(final CustomKit plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (sender.hasPermission("CKA.customkit")) {
            StrikePracticeAPI api = StrikePractice.getAPI();
            PlayerKits playerCustomKit = api.getPlayerKits(player);
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("save")) {
                    if (plugin.isIneditroom.get(player.getUniqueId()) != null && plugin.isIneditroom.get(player.getUniqueId()).equals(true)) {
                        List<ItemStack> inventoryItems1 = new ArrayList<>();
                        for (ItemStack item : player.getInventory().getContents()) {
                            if (item != null) {
                                inventoryItems1.add(item.clone()); // Clone the item to avoid modifying the original stack
                            } else {
                                inventoryItems1.add(new ItemStack(Material.AIR));
                            }
                        }
                        playerCustomKit.getCustomKit().setInventory(inventoryItems1);
                        playerCustomKit.getCustomKit().setHelmet(player.getInventory().getHelmet());
                        playerCustomKit.getCustomKit().setChestplate(player.getInventory().getChestplate());
                        playerCustomKit.getCustomKit().setLeggings(player.getInventory().getLeggings());
                        playerCustomKit.getCustomKit().setBoots(player.getInventory().getBoots());
                        player.teleport(api.getSpawnLocation());
                        player.setGameMode(GameMode.SURVIVAL);

                        if (plugin.isIneditroom.get(player.getUniqueId()) != null && plugin.isIneditroom.get(player.getUniqueId()).equals(false)) {
                            return false;
                        }
                        if (plugin.isIneditroom.get(player.getUniqueId()) != null && plugin.isIneditroom.get(player.getUniqueId()).equals(true)) {
                            player.setInvisible(false);
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                player.showPlayer(players);
                            }
                        }
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawnitem give " + player.getName());
                        playerCustomKit.savePlayerKitsToFile();
                        plugin.isIneditroom.remove(player.getUniqueId());
                        String message = HexColours.translate(Objects.requireNonNull(plugin.getConfig().getString("save-mess")).replace("[", "").replace("]", ""));
                        player.sendMessage(message);
                    }
                }
                if (args[0].equalsIgnoreCase("get")) {
                    if (sender.hasPermission("StrikePractice.staff")) {
                        api.loadPlayerKits(player.getUniqueId()).getCustomKit().giveKit(player);
                    }
                }
                if (args[0].equalsIgnoreCase("edit")) {
                    if (plugin.isIneditroom.containsKey(player.getUniqueId())) {
                        player.sendMessage(HexColours.translate(Objects.requireNonNull(plugin.getConfig().getString("isIneditroom").replace("[", "").replace("]", ""))));
                        return false;
                    } else if (api.isInQueue(player)) {
                        player.sendMessage(HexColours.translate(Objects.requireNonNull(plugin.getConfig().getString("isInQueue").replace("[", "").replace("]", ""))));
                        return false;
                    } else if (api.isSpectator(player)) {
                        player.sendMessage(HexColours.translate(Objects.requireNonNull(plugin.getConfig().getString("isSpectator").replace("[", "").replace("]", ""))));
                        return false;
                    } else {
                        plugin.isIneditroom.put(player.getUniqueId(), true);
                        player.sendMessage(HexColours.translate(Objects.requireNonNull(plugin.getConfig().getString("edit-mess")).replace("[", "").replace("]", "")));
                        CustomKit.isInvisible.add(player);
                        player.teleport(plugin.editRoomLocation());
                        if (api.loadPlayerKits(player.getUniqueId()).getCustomKit().isHorse()) {
                            plugin.hasHorse.put(player.getUniqueId(), true);
                            playerCustomKit.getCustomKit().setHorse(false);
                            playerCustomKit.savePlayerKitsToFile();
                            api.loadPlayerKits(player.getUniqueId()).getCustomKit().giveKit(player);

                            playerCustomKit.getCustomKit().setHorse(true);
                            playerCustomKit.savePlayerKitsToFile();

                        } else {
                            plugin.hasHorse.put(player.getUniqueId(), false);
                            api.loadPlayerKits(player.getUniqueId()).getCustomKit().giveKit(player);
                        }
                        player.setGameMode(GameMode.CREATIVE);
                        player.setFlying(false);
                        player.setAllowFlight(false);

                        for (Player players : Bukkit.getOnlinePlayers()) {
                            player.hidePlayer(players);
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("settings")) {
                    if (sender.hasPermission("CKA.customkit")) {
                        if (plugin.isIneditroom.get(player.getUniqueId()) != null && plugin.isIneditroom.get(player.getUniqueId()).equals(true)) {
                            plugin.GuiSettings(player);
                        }
                    }
                }
            } else {
                player.sendMessage("Usage: /ck [save/edit/settings]");
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String alias,
            final String[] args
    ) {
        if (args.length == 0 || args.length == 1) {
            return Stream.of("edit", "save", "settings")
                    .filter(value -> value.startsWith(args[0]))
                    .toList();
        }
        return Collections.emptyList();
    }

}

