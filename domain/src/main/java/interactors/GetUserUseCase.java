package interactors;

import javax.inject.Inject;

import entities.User;
import schedulers.ObserveOn;
import schedulers.SubscribeOn;
import respositories.SessionRepository;
import rx.Observable;

public class GetUserUseCase extends SessionUseCase<User> {
    @Inject public GetUserUseCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(sessionRepository, subscribeOn, observeOn);
    }

    @Override protected Observable<User> buildUseCaseObservable() {
        return sessionRepository.askForUserLogged();
    }
}
