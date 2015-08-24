package presenters;

import javax.inject.Inject;

import entities.User;
import interactors.GetUserUseCase;
import presenters.subscribers.BaseUseCaseSubscriber;
import views.LaunchView;

public class LaunchPresenter implements Presenter<LaunchView> {
    private final GetUserUseCase getUserUseCase;

    @Inject public LaunchPresenter(GetUserUseCase getUserUseCase) {
        this.getUserUseCase = getUserUseCase;
    }

    @Override public void attachView(LaunchView launchView) {
        getUserUseCase.execute(new LaunchSubscriber(launchView));
    }

    @Override public void destroy() {
        getUserUseCase.unsubscribe();
    }

    private static class LaunchSubscriber extends BaseUseCaseSubscriber<User> {
        private final LaunchView launchView;

        public LaunchSubscriber(LaunchView launchView) {
            this.launchView = launchView;
        }

        @Override public void onError(Throwable e) {
            launchView.goToCredentials();
        }

        @Override public void onNext(User user) {
            launchView.goToProfile();
        }
    }
}
