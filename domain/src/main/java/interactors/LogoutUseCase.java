package interactors;

import javax.inject.Inject;

import schedulers.ObserveOn;
import schedulers.SubscribeOn;
import respositories.SessionRepository;
import rx.Observable;

public class LogoutUseCase extends SessionUseCase<String> {

    @Inject public LogoutUseCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(sessionRepository, subscribeOn, observeOn);
    }

    @Override protected Observable<String> buildUseCaseObservable() {
        return sessionRepository.askForLogout();
    }
}
