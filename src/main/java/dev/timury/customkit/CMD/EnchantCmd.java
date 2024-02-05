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

import java.util.*;

public class EnchantCmd implements CommandExecutor, TabCompleter {

    private final CustomKit instance = CustomKit.getInstance();

    private static final String[] compl = {"FIRE_PROTECTION", "SHARPNESS", "PROTECTION", "FEATHER_FALLING", "BLAST_PROTECTION",
            "PROJECTILE_PROTECTION", "RESPIRATION", "AQUA_AFFINITY", "THORNS", "DEPTH_STRIDER",
            "FROST_WALKER", "SMITE", "BANE_OF_ARTHROPODS", "KNOCKBACK", "FIRE_ASPECT",
            "LOOTING", "EFFICIENCY", "SILK_TOUCH", "UNBREAKING", "FORTUNE", "POWER",
            "PUNCH", "FLAME", "INFINITY", "LUCK", "LURE", "MENDING"};

    private static Enchantment translateEnchantment(String inputEnchantment) {
        return switch (inputEnchantment.toLowerCase()) {
            case "sharpness" -> Enchantment.DAMAGE_ALL;
            case "protection" -> Enchantment.PROTECTION_ENVIRONMENTAL;
            case "fire_protection" -> Enchantment.PROTECTION_FIRE;
            case "feather_falling" -> Enchantment.PROTECTION_FALL;
            case "blast_protection" -> Enchantment.PROTECTION_EXPLOSIONS;
            case "projectile_protection" -> Enchantment.PROTECTION_PROJECTILE;
            case "respiration" -> Enchantment.OXYGEN;
            case "aqua_affinity" -> Enchantment.WATER_WORKER;
            case "thorns" -> Enchantment.THORNS;
            case "depth_strider" -> Enchantment.DEPTH_STRIDER;
            case "frost_walker" -> Enchantment.FROST_WALKER;
            case "smite" -> Enchantment.DAMAGE_UNDEAD;
            case "bane_of_arthropods" -> Enchantment.DAMAGE_ARTHROPODS;
            case "knockback" -> Enchantment.KNOCKBACK;
            case "fire_aspect" -> Enchantment.FIRE_ASPECT;
            case "looting" -> Enchantment.LOOT_BONUS_MOBS;
            case "efficiency" -> Enchantment.DIG_SPEED;
            case "silk_touch" -> Enchantment.SILK_TOUCH;
            case "unbreaking" -> Enchantment.DURABILITY;
            case "fortune" -> Enchantment.LOOT_BONUS_BLOCKS;
            case "power" -> Enchantment.ARROW_DAMAGE;
            case "punch" -> Enchantment.ARROW_KNOCKBACK;
            case "flame" -> Enchantment.ARROW_FIRE;
            case "infinity" -> Enchantment.ARROW_INFINITE;
            case "luck_of_the_sea" -> Enchantment.LUCK;
            case "lure" -> Enchantment.LURE;
            case "mending" -> Enchantment.MENDING;
            default -> null;
        };
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (instance.isIneditroom.get(player.getUniqueId()) != null && instance.isIneditroom.get(player.getUniqueId()).equals(true)) {
            if (player.hasPermission("CKA.customkit")) {
                if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
                    Enchantment enchantment = translateEnchantment(args[1]);
                    int enchantmentLvl = Integer.parseInt(args[2]);
                    if (enchantment != null && enchantmentLvl <= enchantment.getMaxLevel()) {
                        ItemStack itemInUse = player.getItemInHand();
                        if (itemInUse != null && enchantment.canEnchantItem(itemInUse)) {
                            itemInUse.addEnchantment(enchantment, enchantmentLvl);
                            player.sendMessage(HexColours.translate("#78ff85"+ args[1] +" has been added"));

                        }else{
                            player.sendMessage(HexColours.translate("#ff4747"+ args[1] +" hasn't been added"));
                        }
                    }else {
                        player.sendMessage(HexColours.translate("#ff4747"+ args[1] +" cannot be applied for this item OR the level of the enchantment is too big"));
                    }
                }
                if (args.length >= 1 && args[0].equalsIgnoreCase("remove")) {
                    Enchantment enchantment = translateEnchantment(args[1]);
                    if (enchantment != null) {
                        ItemStack itemInUse = player.getItemInHand();
                        if (itemInUse != null && enchantment.canEnchantItem(itemInUse)) {
                            itemInUse.removeEnchantment(enchantment);
                            player.sendMessage(HexColours.translate("#78ff85"+ args[1] +" has been removed"));
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
        if (args.length == 1) {
            // If there's only one argument, provide completion for the command itself
            return Arrays.asList("add", "remove");
        } else if (args.length == 2) {
            // If there are two arguments, provide completion based on the second argument
            String secondArg = args[1].toLowerCase();
            List<String> completions = new ArrayList<>();

            for (String enchantment : compl) {
                if (enchantment.toLowerCase().startsWith(secondArg)) {
                    completions.add(enchantment);
                }
            }

            return completions;
        } else {
            // No completions for other argument lengths
            return Collections.emptyList();
        }
    }
}
