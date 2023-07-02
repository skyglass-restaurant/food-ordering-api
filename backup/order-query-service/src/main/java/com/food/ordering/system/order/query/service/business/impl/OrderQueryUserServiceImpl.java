package com.food.ordering.system.order.query.service.business.impl;

import com.food.ordering.system.order.query.service.dataaccess.entity.UserPermission;
import com.food.ordering.system.order.query.service.dataaccess.repository.UserPermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderQueryUserServiceImpl implements com.food.ordering.system.order.query.service.business.OrderQueryUserService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderQueryUserServiceImpl.class);


    private final UserPermissionRepository userPermissionRepository;

    public OrderQueryUserServiceImpl(UserPermissionRepository permissionRepository) {
        this.userPermissionRepository = permissionRepository;
    }

    @Override
    public Optional<List<UserPermission>> findAllPermissionsByUsername(String username) {
        LOG.info("Finding permissions by username {}", username);
        return userPermissionRepository.findPermissionsByUsername(username);
    }
}
