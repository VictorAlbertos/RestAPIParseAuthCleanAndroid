package interactors;

import javax.inject.Inject;

import entities.User;
import schedulers.ObserveOn;
import schedulers.SubscribeOn;
import respositories.SessionRepository;
import rx.Observable;

public class SignUpUseCase extends SessionUseCase<String> {
    private User user;

    @Inject public SignUpUseCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(sessionRepository, subscribeOn, observeOn);
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override protected Observable<String> buildUseCaseObservable() {
        return sessionRepository.askForSignUp(user);
    }
}
