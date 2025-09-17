package therooster.booking.enums;


import lombok.Getter;

@Getter
public enum TypePermission {
    CREATE_BOOKING,
    UPDATE_BOOKING,
    PATH_BOOKING,
    UPDATE_MY_BOOKING,
    PATH_MY_BOOKING,
    DELETE_BOOKING,
    DELETE_MY_BOOKING,
    READ_ALL_BOOKING,
    CREATE_SERVICE,
    READ_SERVICE,
    UPDATE_SERVICE,
    PATH_SERVICE,
    DELETE_SERVICE,
    READ_ALL_SERVICE,
    READ_MY_BOOKING,
    READ_BOOKING,
    CREATE_SCHEDULE,
    DELETE_SCHEDULE,
    READ_SCHEDULE;


    private final String libelle;


    TypePermission() {
        this.libelle = this.name();
    }
}