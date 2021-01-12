package bot;

import net.dv8tion.jda.api.Permission;

public enum CommandPermissions {
    USER(),
    ADMIN(Permission.MANAGE_SERVER),
    CREATOR(Permission.ADMINISTRATOR);

    private final Permission[] permissions;

    CommandPermissions(Permission... permissions) {
        this.permissions = permissions;
    }

    public Permission[] getRequiredPermissions() {
        return permissions;
    }
}
