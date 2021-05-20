package application.exceptions;

/**
 * Класс-исключение
 */
public class SocialNetworkException extends RuntimeException {
    public SocialNetworkException(String message) {
        super(message);
    }
}
