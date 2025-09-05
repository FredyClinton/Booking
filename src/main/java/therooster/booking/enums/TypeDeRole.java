package therooster.booking.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static therooster.booking.enums.TypePermission.*;


@Getter
@AllArgsConstructor
public enum TypeDeRole {
    CLIENT(
            Set.of(CLIENT_CREATE_BOOKING)
    ),

    EMPLOYEE(
            Set.of(

                    EMPLOYEE_CREATE,
                    EMPLOYEE_UPDATE,
                    EMPLOYEE_READ,
                    EMPLOYEE_DELETE_BOOKING
            )
    ),

    ADMIN(
            Set.of(
                    ADMIN_CREATE,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_READ,

                    EMPLOYEE_CREATE,
                    EMPLOYEE_UPDATE,
                    EMPLOYEE_READ,
                    EMPLOYEE_DELETE
            )
    );


    private final Set<TypePermission> permissions;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = this.getPermissions().stream().map(
                permission -> new SimpleGrantedAuthority(permission.getLibelle())
        ).collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;

    }
}