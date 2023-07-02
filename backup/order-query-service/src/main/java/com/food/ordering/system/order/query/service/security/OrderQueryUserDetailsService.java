package com.food.ordering.system.order.query.service.security;

import com.food.ordering.system.order.query.service.business.OrderQueryUserService;
import com.food.ordering.system.order.query.service.transformer.UserPermissionsToUserDetailTransformer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OrderQueryUserDetailsService implements UserDetailsService {

    private final OrderQueryUserService queryUserService;

    private final UserPermissionsToUserDetailTransformer userPermissionsToUserDetailTransformer;

    public OrderQueryUserDetailsService(OrderQueryUserService userService,
                                        UserPermissionsToUserDetailTransformer transformer) {
        this.queryUserService = userService;
        this.userPermissionsToUserDetailTransformer = transformer;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return queryUserService
                .findAllPermissionsByUsername(username)
                .map(userPermissionsToUserDetailTransformer::getUserDetails)
                .orElseThrow(
                        () -> new UsernameNotFoundException("No user found with name " + username));
    }
}
