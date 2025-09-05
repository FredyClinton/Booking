package therooster.booking.exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String message) {
        super(message);

    }

    public TokenNotFoundException() {
        super("Token not found");

    }
}
