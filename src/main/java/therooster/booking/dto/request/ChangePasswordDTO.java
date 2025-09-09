package therooster.booking.dto.request;

public record ChangePasswordDTO(
        String code,
        String newPassword,
        String email
) {
}
