package com.dizertatie.backend.user.util;

import com.dizertatie.backend.user.security.CustomUserDetails;
import com.dizertatie.backend.user.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.stream.Collectors;

public class SecurityUtil {

    public static Collection<String> currentRoles() {
        return ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUser()
                .getRoles()
                .stream()
                .map(r -> r.getType())
                .collect(Collectors.toList());
    }

    public static boolean hasRole(String role) {
        return currentRoles().contains(role);
    }

    public static User currentUser() {
        return ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUser();
    }

}
