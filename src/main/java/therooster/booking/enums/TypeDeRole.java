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
            Set.of(CREATE_BOOKING,
                    READ_MY_BOOKING,
                    UPDATE_MY_BOOKING,
                    PATH_MY_BOOKING,
                    DELETE_MY_BOOKING
            )
    ),

    EMPLOYEE(
            Set.of(
                    CREATE_BOOKING,
                    UPDATE_BOOKING,
                    PATH_BOOKING,
                    READ_BOOKING,
                    READ_SCHEDULE,
                    CREATE_SCHEDULE,
                    READ_SERVICE,
                    DELETE_SCHEDULE
            )
    ),

    ADMIN(
            Set.of(
                    CREATE_BOOKING,
                    UPDATE_BOOKING,
                    PATH_BOOKING,
                    UPDATE_MY_BOOKING,
                    PATH_MY_BOOKING,
                    DELETE_BOOKING,
                    DELETE_MY_BOOKING,
                    READ_ALL_BOOKING,
                    CREATE_SERVICE,
                    UPDATE_SERVICE,
                    PATH_SERVICE,
                    DELETE_SERVICE,
                    READ_ALL_SERVICE,
                    READ_MY_BOOKING,
                    READ_BOOKING,
                    READ_SCHEDULE,
                    READ_SERVICE

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