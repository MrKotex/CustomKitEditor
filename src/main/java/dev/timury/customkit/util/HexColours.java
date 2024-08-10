package dev.timury.customkit.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class for handling hex colors in Minecraft messages.
 */
public class HexColours {

    private static final String VERSION;
    private static final int INT_VER;
    private static final boolean SUPPORTS_HEXCOLORS;
    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F\\d]{6}");

    static {
        String version = "Unknown";
        int intVer = 0;
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().substring(24);
            intVer = Integer.parseInt(version.split("_")[1]);
        } catch (Exception e) {
            // Log error or handle as needed
            Bukkit.getLogger().severe("Error retrieving or parsing server version: " + e.getMessage());
        }
        VERSION = version;
        INT_VER = intVer;
        SUPPORTS_HEXCOLORS = INT_VER > 15;
    }

    private HexColours() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Translates hex color codes to Minecraft color codes in the given message.
     *
     * @param message The message containing hex color codes.
     * @return The translated message.
     */
    public static String translate(String message) {
        if (message == null) {
            return "";
        }
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
            // Continue matching within the updated message
            matcher = HEX_PATTERN.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Translates hex color codes to Minecraft color codes in the given list of messages.
     *
     * @param messages The list of messages containing hex color codes.
     * @return The list of translated messages.
     */
    public static List<String> translate(List<String> messages) {
        return messages == null ? List.of() : messages.stream().map(HexColours::translate).collect(Collectors.toList());
    }

    /**
     * Checks if the server supports hex colors.
     *
     * @return True if hex colors are supported, false otherwise.
     */
    public static boolean supportsHexColors() {
        return SUPPORTS_HEXCOLORS;
    }
}
