package dev.timury.customkit.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class HexColours {

    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F\\d]{6}");

    private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(24);
    private static final int INT_VER = Integer.parseInt(VERSION.split("_")[1]);
    private static final boolean SUPPORTS_HEXCOLORS = INT_VER > 15;

    public static final String BLACK = ChatColor.BLACK.toString(); // 0
    public static final String D_BLUE = ChatColor.DARK_BLUE.toString(); // 1
    public static final String D_GREEN = ChatColor.DARK_GREEN.toString(); // 2
    public static final String D_AQUA = ChatColor.DARK_AQUA.toString(); // 3
    public static final String D_RED = ChatColor.DARK_RED.toString(); // 4
    public static final String D_PURPLE = ChatColor.DARK_PURPLE.toString(); // 5
    public static final String GOLD = ChatColor.GOLD.toString(); // 6
    public static final String GRAY = ChatColor.GRAY.toString(); // 7
    public static final String D_GRAY = ChatColor.DARK_GRAY.toString(); // 8
    public static final String BLUE = ChatColor.BLUE.toString(); // 9
    public static final String GREEN = ChatColor.GREEN.toString(); // a
    public static final String AQUA = ChatColor.AQUA.toString(); // b
    public static final String RED = ChatColor.RED.toString(); // c
    public static final String PURPLE = ChatColor.LIGHT_PURPLE.toString(); // d
    public static final String YELLOW = ChatColor.YELLOW.toString(); // e
    public static final String WHITE = ChatColor.WHITE.toString(); // f
    public static final String B = ChatColor.BOLD.toString(); // l
    public static final String I = ChatColor.ITALIC.toString(); // o
    public static final String S = ChatColor.STRIKETHROUGH.toString(); // m
    public static final String U = ChatColor.UNDERLINE.toString(); // n
    public static final String O = ChatColor.MAGIC.toString(); // k
    public static final String R = ChatColor.RESET.toString(); // r

    public String translate(String message) {
        if (SUPPORTS_HEXCOLORS) {
            Matcher matcher = HEX_PATTERN.matcher(message);
            while (matcher.find()) {
                final String hexCode = message.substring(matcher.start(), matcher.end());
                final String replaceSharp = hexCode.replace('#', 'x');
                final char[] ch = replaceSharp.toCharArray();
                final StringBuilder builder = new StringBuilder();
                for (final char c : ch) {
                    builder.append("&").append(c);
                }
                message = message.replace(hexCode, builder.toString());
                matcher = HEX_PATTERN.matcher(message);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public List<String> translate(List<String> messages) {
        return messages.stream().map(HexColours::translate).collect(Collectors.toList());
    }
}
