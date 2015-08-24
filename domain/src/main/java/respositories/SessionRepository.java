package respositories;

import entities.User;
import rx.Observable;

public interface SessionRepository {
    Observable<String> askForLogin(String username, String password);
    Observable<String> askForSignUp(User user);
    Observable<User> askForUserLogged();
    Observable<String> askForUpdateLoggedUser(User user);
    Observable<String> askForLogout();
}