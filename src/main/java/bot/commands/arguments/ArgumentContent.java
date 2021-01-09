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

    public ArgumentContent(String content, int start) {
        this(content, start, content.length() + start);
    }

    public boolean exists() {
        return content != null;
    }
}
