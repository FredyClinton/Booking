package therooster.booking.config.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import therooster.booking.entity.UserEntity;

public class SecurityUtils {

    private SecurityUtils() {

    }

    /**
     * Retourne l'Authentication courante
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Retourne le UserEntity actuellement connecté
     */
    public static UserEntity getCurrentUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        return (UserEntity) principal;
    }

    /**
     * Retourne le username de l'utilisateur connecté
     */
    public static String getCurrentUsername() {
        UserEntity user = getCurrentUser();
        return user == null ? null : user.getUsername();
    }

    public static boolean currentUserHasRole(String role) {
        UserEntity user = getCurrentUser();
        return user != null && user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }
}

