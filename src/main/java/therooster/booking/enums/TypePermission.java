package therooster.booking.enums;


import lombok.Getter;

@Getter
public enum TypePermission {

    ADMIN_CREATE,
    ADMIN_UPDATE,
    ADMIN_DELETE,
    ADMIN_READ,


    EMPLOYEE_CREATE,
    EMPLOYEE_UPDATE,
    EMPLOYEE_READ,
    EMPLOYEE_DELETE_BOOKING,


    CLIENT_CREATE_BOOKING,
    EMPLOYEE_DELETE;


    private final String libelle;


    TypePermission() {
        this.libelle = this.name();
    }
}