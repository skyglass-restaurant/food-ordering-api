package com.food.ordering.system.order.query.service.transformer;

import com.food.ordering.system.order.query.service.security.PermissionType;
import com.food.ordering.system.order.query.service.security.OrderQueryUser;
import com.food.ordering.system.order.query.service.dataaccess.entity.UserPermission;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserPermissionsToUserDetailTransformer {

    public OrderQueryUser getUserDetails(List<UserPermission> userPermissions) {
        return OrderQueryUser.builder()
                .username(userPermissions.get(0).getUsername())
                .permissions(userPermissions.stream()
                        .collect(Collectors.toMap(
                                UserPermission::getDocumentId,
                                permission -> PermissionType.valueOf(permission.getPermissionType()))))
                .build();
    }
}
