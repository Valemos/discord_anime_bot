package bot.commands;

public enum MenuEmoji {

    CONFIRM("\u2705"),
    DISCARD("\u274C"),
    SHOW_MORE("\u2139");

    private final String emoji;

    MenuEmoji(String emoji) {

        this.emoji = emoji;
    }

    public String getEmoji() {
        return emoji;
    }
}
