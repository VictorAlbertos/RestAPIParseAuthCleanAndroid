package presenters.subscribers;

import views.CredentialsView;

public abstract class BaseCredentialsSubscriber<View extends CredentialsView> extends BaseUseCaseSubscriber<String> {
    private final View credentialsView;

    public BaseCredentialsSubscriber(View credentialsView) {
        this.credentialsView = credentialsView;
    }

    @Override public void onStart() {
        credentialsView.showProgress();
    }

    @Override public void onError(Throwable e) {
        credentialsView.hideProgress();
        credentialsView.onError(e.getMessage());
    }

    @Override public void onNext(String response) {
        credentialsView.hideProgress();
        credentialsView.onSuccess(response);
        credentialsView.goToProfile();
    }
}