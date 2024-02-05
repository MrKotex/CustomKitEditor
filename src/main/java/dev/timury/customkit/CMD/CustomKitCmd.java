package dev.timury.customkit.CMD;

import dev.timury.customkit.CustomKit;
import ga.strikepractice.StrikePractice;
import ga.strikepractice.api.StrikePracticeAPI;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CustomKitCmd implements CommandExecutor, TabCompleter {

    private final CustomKit instance = CustomKit.getInstance();

    private static final String[] compl = { "edit", "save", "settings"};

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if(sender.hasPermission("CKA.customkit")) {
            StrikePracticeAPI api = StrikePractice.getAPI();
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("save")) {
                    if(instance.isIneditroom.get(player.getUniqueId()) != null && instance.isIneditroom.get(player.getUniqueId()).equals(true)) {
                        List<ItemStack> inventoryItems1 = new ArrayList<>();
                        for (ItemStack item : player.getInventory().getContents()) {
                            if (item != null) {
                                inventoryItems1.add(item.clone()); // Clone the item to avoid modifying the original stack
                            } else {
                                inventoryItems1.add(new ItemStack(Material.AIR));
                            }
                        }
                        api.getPlayerKits(player).getCustomKit().setInventory(inventoryItems1);
                        api.getPlayerKits(player).getCustomKit().setHelmet(player.getInventory().getHelmet());
                        api.getPlayerKits(player).getCustomKit().setChestplate(player.getInventory().getChestplate());
                        api.getPlayerKits(player).getCustomKit().setLeggings(player.getInventory().getLeggings());
                        api.getPlayerKits(player).getCustomKit().setBoots(player.getInventory().getBoots());
                        player.teleport(api.getSpawnLocation());
                        player.setGameMode(GameMode.SURVIVAL);
                        if (instance.hasHorse.get(player.getUniqueId()) != null && instance.hasHorse.get(player.getUniqueId()).equals(true)) {
                            for (Entity entity : Bukkit.getWorld(instance.editRoomWorld()).getEntities()) {
                                if (entity instanceof Horse) {
                                    Horse horse = (Horse) entity;
                                    if (horse.getOwner() instanceof Player && horse.getOwner().equals(player)) {
                                        horse.setHealth(0);
                                        instance.hasHorse.remove(player.getUniqueId());
                                    }
                                }
                            }
                        }

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
                }
                if (args[0].equalsIgnoreCase("get")) {
                    if (sender.hasPermission("StrikePractice.staff")) {
                        api.loadPlayerKits(player.getUniqueId()).getCustomKit().giveKit(player);
                    }
                }
                if (args[0].equalsIgnoreCase("edit")) {
                    if(api.isInFight(player) || api.isInQueue(player)){
                        return false;
                    }else{
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
                        player.teleport(instance.editRoomLocation());
                        api.loadPlayerKits(player.getUniqueId()).getCustomKit().giveKit(player);
                        if(api.loadPlayerKits(player.getUniqueId()).getCustomKit().isHorse()){
                            instance.hasHorse.put(player.getUniqueId(), true);
                        }else {
                            instance.hasHorse.put(player.getUniqueId(), false);
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
                        if(instance.isIneditroom.get(player.getUniqueId()) != null && instance.isIneditroom.get(player.getUniqueId()).equals(true)){
                            instance.GuiSettings(player);
                        }
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

