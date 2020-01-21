package pl.edu.pk.biblioteka.data;

public class Permissions {
    public static final int LIBRARIAN_ACCESS = 1;
    public static final int COMMON_READER = 2;
    public static final int BLOCKED_ACCOUNT = 3;

    private int permissionId;
    private int permission;

    public Permissions(int permissionId, int permission) {
        this.permissionId = permissionId;
        this.permission = permission;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
