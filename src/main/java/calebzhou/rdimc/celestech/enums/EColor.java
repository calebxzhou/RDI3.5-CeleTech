package calebzhou.rdimc.celestech.enums;

public enum EColor {
    BLACK("§0"),
    DARK_BLUE("§1"),
    DARK_GREEN("§2"),
    DARK_AQUA("§3"),
    DARK_RED("§4"),
    PURPLE("§5"),
    ORANGE("§6"),
    GRAY("§7"),
    DARK_GRAY("§8"),
    INDIGO("§9"),
    BRIGHT_GREEN("§a"),
    AQUA("§b"),
    RED("§c"),
    PINK("§d"),
    YELLOW("§e"),
    GOLD("§e"),
    WHITE("§f"),
    MAGIC("§k"),
    BOLD("§l"),
    STRIKE("§m"),
    UNDERLINE("§n"),
    ITALIC("§o"),
    RESET("§r");

    public final String code;

    EColor(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
