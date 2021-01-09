package bot.commands.arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageArguments {

    List<ArgumentContent> argumentContents = new ArrayList<>();
    String errorMessage;

    public MessageArguments() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isValid() {
        return errorMessage == null;
    }

    public void setErrorMessage(String message) {
        errorMessage = message;
    }

    public void add(ArgumentContent argumentContent) {
        argumentContents.add(argumentContent);
    }

    public String get(int index) {
        if (index > 0 && index < argumentContents.size()){
            return argumentContents.get(index).content;
        }else{
            return null;
        }
    }

    public List<String> getValues() {
        return argumentContents.stream().map((arg) -> arg.content).collect(Collectors.toList());
    }
}
