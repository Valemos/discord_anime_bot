package bot.commands.arguments;

public class ArgumentContent {
    public String content;
    public int start;
    public int end;

    public ArgumentContent(String content, int start, int end) {
        this.content = content;
        this.start = start;
        this.end = end;
    }

    public boolean exists() {
        return content != null;
    }
}
