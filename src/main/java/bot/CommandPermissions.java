package bot;

import net.dv8tion.jda.api.Permission;

public enum CommandPermissions {
    USER(),
    ADMIN(Permission.ADMINISTRATOR),
    CREATOR(Permission.values());

    private final Permission[] permissions;

    CommandPermissions(Permission... permissions) {
        this.permissions = permissions;
    }

    public Permission[] getRequiredPermissions() {
        return permissions;
    }
}
