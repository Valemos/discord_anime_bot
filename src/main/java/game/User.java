package game;

import bot.AccessLevel;

public class User {
    private final String userId;

    private AccessLevel accessLevel;

    private PersonalCollection collection;

    private MaterialInventory materialInventory;
    public User(String userId, AccessLevel accessLevel) {
        this.userId = userId;
        this.accessLevel = accessLevel;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User){
            return userId.equals(((User) obj).userId);
        }
        return false;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getId() {
        return userId;
    }
}
