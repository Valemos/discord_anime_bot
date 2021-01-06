package bot.commands;

public class CommandInfo {
    public static final String commandChar = "#";

    public final String name;
    public final String alias;

    public CommandInfo(String name) {
        this(name, null);
    }

    public CommandInfo(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String[] getCommandNames(){
        if (alias != null){
            return new String[]{name, alias};
        }else{
            return new String[]{name};
        }
    }
}
