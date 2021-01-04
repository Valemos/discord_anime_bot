package bot;

public enum AccessLevel {
    USER(0),
    ADMIN(1),
    CREATOR(2);

    public int level;

    AccessLevel(int accessLevel) {
        this.level = accessLevel;
    }
}
