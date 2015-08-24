package interactors;

import javax.inject.Inject;

import entities.User;
import schedulers.ObserveOn;
import schedulers.SubscribeOn;
import respositories.SessionRepository;
import rx.Observable;

public class UpdateUserCase extends SessionUseCase<String> {
    private User user;

    @Inject public UpdateUserCase(SessionRepository sessionRepository, SubscribeOn subscribeOn, ObserveOn observeOn) {
        super(sessionRepository, subscribeOn, observeOn);
    }

    public void setUserToUpdate(User user) {
        this.user = user;
    }

    @Override protected Observable<String> buildUseCaseObservable() {
        return sessionRepository.askForUpdateLoggedUser(user);
    }
}
