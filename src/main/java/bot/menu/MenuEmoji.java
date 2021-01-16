package bot.menu;

public enum MenuEmoji {

    CONFIRM("\u2705"),
    DISCARD("\u274C"),
    SHOW_MORE("\u2139"),
    ONE("\u0031\uFE0F\u20E3"),
    TWO("\u0032\uFE0F\u20E3"),
    THREE("\u0033\uFE0F\u20E3");

    private final String emoji;

    MenuEmoji(String emoji) {

        this.emoji = emoji;
    }

    public String getEmoji() {
        return emoji;
    }
}
