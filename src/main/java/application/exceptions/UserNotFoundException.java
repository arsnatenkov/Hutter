package application.exceptions;

/**
 * Класс-исключение
 */
public class UserNotFoundException extends SocialNetworkException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
