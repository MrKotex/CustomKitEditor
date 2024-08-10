package dev.timury.customkit.CMD;

import dev.timury.customkit.CustomKit;
import dev.timury.customkit.util.HexColours;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cryptomorin.xseries.XEnchantment;

import java.util.*;

public class EnchantCmd implements CommandExecutor, TabCompleter {

    private final CustomKit instance = CustomKit.instance;


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (instance.isIneditroom.get(player.getUniqueId()) != null && instance.isIneditroom.get(player.getUniqueId()).equals(true)) {
            if (player.hasPermission("CKA.customkit")) {
                if (args.length >= 1 && args[0].equalsIgnoreCase("add")) {
                    sender.sendMessage(args[1]);
                    try {
                        Enchantment enchantment = translateEnchantment(args[1]);
                        // Check if args[2] is not null and not an empty string
                        if (args.length >= 3 && args[2] != null && !args[2].isEmpty()) {
                            int enchantmentLvl = Integer.parseInt(args[2]);

                            if (enchantment != null && enchantmentLvl <= enchantment.getMaxLevel()) {
                                ItemStack itemInUse = player.getItemInHand();

                                if (itemInUse != null && enchantment.canEnchantItem(itemInUse)) {
                                    itemInUse.addEnchantment(enchantment, enchantmentLvl);
                                    player.sendMessage(HexColours.translate(Objects.requireNonNull(Objects.requireNonNull(instance.getConfig().getString("enchantment-added")).replace("<enchantment>", args[1]).replace("[", "").replace("]", ""))));
                                } else {
                                    player.sendMessage(HexColours.translate(Objects.requireNonNull(Objects.requireNonNull(instance.getConfig().getString("enchantment-cannot-be-added")).replace("<enchantment>", args[1]).replace("[", "").replace("]", ""))));
                                }
                            } else {
                                player.sendMessage(HexColours.translate(Objects.requireNonNull(Objects.requireNonNull(instance.getConfig().getString("enchantment-too-big-or-small-or-cannot-be-added-on-this-item")).replace("<enchantment>", args[1]).replace("[", "").replace("]", ""))));
                            }
                        } else {
                            player.sendMessage(HexColours.translate(Objects.requireNonNull(Objects.requireNonNull(instance.getConfig().getString("enchantment-missing-level")).replace("<enchantment>", args[1]).replace("[", "").replace("]", ""))));
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage(HexColours.translate(Objects.requireNonNull(Objects.requireNonNull(instance.getConfig().getString("enchantment-invalid")).replace("<enchantment>", args[1]).replace("[", "").replace("]", ""))));
                    }
                }
                if (args.length >= 1 && args[0].equalsIgnoreCase("remove")) {
                    Enchantment enchantment = Enchantment.getByName(args[1]);
                    if (enchantment != null) {
                        ItemStack itemInUse = player.getItemInHand();
                        if (itemInUse != null && enchantment.canEnchantItem(itemInUse)) {
                            itemInUse.removeEnchantment(enchantment);
                            player.sendMessage(HexColours.translate(HexColours.translate(Objects.requireNonNull(Objects.requireNonNull(instance.getConfig().getString("enchantment-removed")).replace("<enchantment>", args[1]).replace("[", "").replace("]", "")))));
                        }
                    }
                }
                if(args[0].equalsIgnoreCase("test")){
                    listEnchantments(player);
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            // If there's only one argument, provide completion for the command itself
            return Arrays.asList("add", "remove");
        } else if (args.length == 2) {
            // If there are two arguments, provide completion based on the second argument
            String secondArg = args[1].toLowerCase();
            List<String> completions = new ArrayList<>();
            for (XEnchantment xEnchantment : XEnchantment.values()) {
                Enchantment bukkitEnchantment = xEnchantment.getEnchant();
                if (bukkitEnchantment != null) { // Check if bukkitEnchantment is not null
                    String enchantmentName = bukkitEnchantment.getKey().getKey();
                    if (enchantmentName.toLowerCase().startsWith(secondArg.toLowerCase())) {
                        completions.add(enchantmentName);
                    }
                }
            }
            return completions;
        } else {
            // No completions for other argument lengths
            return Collections.emptyList();
        }
    }

    public void listEnchantments(CommandSender sender) {
        for (XEnchantment xEnchantment : XEnchantment.values()) {
            Enchantment bukkitEnchantment = xEnchantment.getEnchant();
            if (bukkitEnchantment != null) {
                String enchantmentName = bukkitEnchantment.getKey().getKey();
                sender.sendMessage(enchantmentName);
            }
        }
    }

    public static Enchantment translateEnchantment(String enchantmentName) {
        XEnchantment xEnchantment = matchXEnchantment(enchantmentName);
        if (xEnchantment != null) {
            return xEnchantment.getEnchant();
        }
        return null;
    }

    public static XEnchantment matchXEnchantment(String enchantmentName) {
        return XEnchantment.matchXEnchantment(enchantmentName).orElse(null);
    }

}


