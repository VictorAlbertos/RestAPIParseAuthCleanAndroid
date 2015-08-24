package presenters;

import javax.inject.Inject;

import interactors.LogoutUseCase;
import presenters.subscribers.BaseProfileSubscriber;
import views.LogoutView;

public class LogoutPresenter implements Presenter<LogoutView> {
    private final LogoutUseCase logoutUseCase;
    private LogoutView logoutView;

    @Inject public LogoutPresenter(LogoutUseCase logoutUseCase) {
        this.logoutUseCase = logoutUseCase;
    }

    @Override public void attachView(LogoutView logoutView) {
        this.logoutView = logoutView;
    }

    public void doLogout() {
        logoutUseCase.execute(new BaseProfileSubscriber<LogoutView, String>(logoutView) {
            @Override public void onNext(String message) {
                profileView.hideProgress();
                profileView.onSuccess(message);
                profileView.toCredentials();
            }
        });
    }

    @Override public void destroy() {
        logoutUseCase.unsubscribe();
    }
}
