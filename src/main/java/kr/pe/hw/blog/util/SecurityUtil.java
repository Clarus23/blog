package kr.pe.hw.blog.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static String getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getName() == null) {
            throw new RuntimeException("No authentication found");
        }

        return authentication.getName();
    }

    public static String getCurrentUserRole() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getAuthorities().iterator().next().getAuthority() == null) {
            throw new RuntimeException("No authentication found");
        }

        return authentication.getAuthorities().iterator().next().getAuthority();
    }
}
