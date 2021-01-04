package game;

import java.util.ArrayList;
import java.util.List;

public class AnimeCardsGame {
    GlobalCollection globalCollection;
    private List<User> users = new ArrayList<>();


    public User getUserById(String userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst().orElse(null);
    }

    public void addUser(User user) {
        if (!users.contains(user)){
            users.add(user);
        }
    }
}

