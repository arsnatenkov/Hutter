package application.exceptions;

public class UserNotFoundException extends SocialNetworkException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
