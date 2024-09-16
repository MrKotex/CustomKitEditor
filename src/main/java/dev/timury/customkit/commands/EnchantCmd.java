package dev.timury.customkit.commands;

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

    private static final List<String> ENCHANTMENTS;

    static {
        ENCHANTMENTS = Arrays.stream(XEnchantment.values())
                .filter(enchantment -> enchantment.getEnchant() != null)
                .map(Enum::name).toList();
    }

    private final CustomKit plugin;

    public EnchantCmd(final CustomKit plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String label,
            final @NotNull String[] args
    ) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!plugin.isIneditroom.get(player.getUniqueId())) {
            return true;
        }

        if (!player.hasPermission("CKA.customkit")) {
            return true;
        }

        if (args.length == 0) {
            return true;
        }

        final String firstArg = args[0].toLowerCase();
        switch (firstArg) {
            case "allenchants": {
                player.sendMessage(
                        String.join(", ", ENCHANTMENTS)
                );
                return true;
            }

            case "add": {
                try {
                    Enchantment enchantment = findEnchantment(args[1]);
                    if (args.length < 3) {
                        player.sendMessage(
                                HexColours.translate(
                                        Objects.requireNonNull(
                                                plugin.getConfig().getString("enchantment-missing-level")
                                        ).replace("<enchantment>", args[1]).replace("[", "").replace("]", "")
                                )
                        );
                        return true;
                    }

                    final int enchantmentLvl = Integer.parseInt(args[2]);
                    if (enchantment == null || enchantmentLvl > enchantment.getMaxLevel()) {
                        player.sendMessage(
                                HexColours.translate(
                                        Objects.requireNonNull(
                                                plugin.getConfig().getString("enchantment-too-big-or-small-or-cannot-be-added-on-this-item")
                                        ).replace("<enchantment>", args[1]).replace("[", "").replace("]", "")
                                )
                        );
                        return true;
                    }

                    final ItemStack itemInUse = player.getInventory().getItemInMainHand();
                    if (enchantment.canEnchantItem(itemInUse)) {
                        itemInUse.addEnchantment(enchantment, enchantmentLvl);
                        player.sendMessage(
                                HexColours.translate(
                                        Objects.requireNonNull(
                                                plugin.getConfig().getString("enchantment-added")
                                        ).replace("<enchantment>", args[1]).replace("[", "").replace("]", "")
                                )
                        );
                    } else {
                        player.sendMessage(
                                HexColours.translate(
                                        Objects.requireNonNull(
                                                plugin.getConfig().getString("enchantment-cannot-be-added")
                                        ).replace("<enchantment>", args[1]).replace("[", "").replace("]", "")
                                )
                        );
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(
                            HexColours.translate(
                                    Objects.requireNonNull(
                                            plugin.getConfig().getString("enchantment-invalid")
                                    ).replace("<enchantment>", args[1]).replace("[", "").replace("]", "")
                            )
                    );
                }
            }

            case "remove": {
                final Enchantment enchantment = Enchantment.getByName(args[1]);
                if (enchantment == null) {
                    return true;
                }

                final ItemStack itemInUse = player.getInventory().getItemInMainHand();
                if (enchantment.canEnchantItem(itemInUse)) {
                    itemInUse.removeEnchantment(enchantment);
                    player.sendMessage(
                            HexColours.translate(
                                    Objects.requireNonNull(
                                            plugin.getConfig().getString("enchantment-removed")
                                    ).replace("<enchantment>", args[1]).replace("[", "").replace("]", "")
                            )
                    );
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String alias,
            final String[] args
    ) {
        if (args.length == 1) {
            return List.of("add", "remove");
        } else if (args.length == 2) {
            final String secondArg = args[1].toLowerCase();
            if (secondArg.isEmpty() || secondArg.isBlank()) {
                return ENCHANTMENTS;
            }
            return ENCHANTMENTS.stream().filter(name -> name.toLowerCase().startsWith(secondArg)).toList();
        } else {
            return Collections.emptyList();
        }
    }

    private static Enchantment findEnchantment(String enchantmentName) {
        final XEnchantment xEnchantment = XEnchantment.matchXEnchantment(enchantmentName).orElse(null);
        if (xEnchantment != null) {
            return xEnchantment.getEnchant();
        }
        return null;
    }

}


