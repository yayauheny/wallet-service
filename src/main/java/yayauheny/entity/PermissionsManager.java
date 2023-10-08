package yayauheny.entity;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static yayauheny.entity.Permission.*;

@UtilityClass
public class PermissionsManager {

    public static final Map<PlayerRole, List<Permission>> permissions = Map.ofEntries(
            Map.entry(PlayerRole.ADMIN, Arrays.asList(Permission.values())),
            Map.entry(PlayerRole.USER, Arrays.asList(BALANCE_OPERATIONS, READ_TRANSACTIONS))
    );

    public List<Permission> getPermissions(PlayerRole role){
        return permissions.get(role);
    }
}
