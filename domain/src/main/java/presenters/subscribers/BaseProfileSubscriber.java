package presenters.subscribers;

import views.ProfileView;

public abstract class BaseProfileSubscriber<View extends ProfileView, T> extends BaseUseCaseSubscriber<T> {
    protected final View profileView;

    public BaseProfileSubscriber(View profileView) {
        this.profileView = profileView;
    }

    @Override public void onStart() {
        profileView.showProgress();
    }

    @Override public void onError(Throwable e) {
        profileView.hideProgress();
        profileView.onError(e.getMessage());
    }
}