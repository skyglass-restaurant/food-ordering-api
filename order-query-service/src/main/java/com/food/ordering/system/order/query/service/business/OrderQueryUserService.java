package com.food.ordering.system.order.query.service.business;

import com.food.ordering.system.order.query.service.dataaccess.entity.UserPermission;

import java.util.List;
import java.util.Optional;

public interface OrderQueryUserService {

    Optional<List<UserPermission>> findAllPermissionsByUsername(String username);
}
