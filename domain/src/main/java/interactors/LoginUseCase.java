package interactors;

import javax.inject.Inject;

import schedulers.ObserveOn;
import schedulers.SubscribeOn;
import respositories.SessionRepository;
import rx.Observable;

public class LoginUseCase extends SessionUseCase<String> {
    private String username, password;

    @Inject public LoginUseCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(sessionRepository, subscribeOn, observeOn);
    }

    public void doLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override protected Observable<String>  buildUseCaseObservable() {
        return sessionRepository.askForLogin(username, password);
    }
}
