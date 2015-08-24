package presenters;

import javax.inject.Inject;

import entities.User;
import interactors.UpdateUserCase;
import presenters.subscribers.BaseProfileSubscriber;
import views.UpdateUserView;

public class UpdateUserPresenter implements Presenter<UpdateUserView> {
    private final UpdateUserCase updateUserCase;
    private UpdateUserView updateUserView;

    @Inject public UpdateUserPresenter(UpdateUserCase updateUserCase) {
        this.updateUserCase = updateUserCase;
    }

    @Override public void attachView(UpdateUserView updateUserView) {
        this.updateUserView = updateUserView;
    }

    public void doUpdateUser(User user) {
        updateUserCase.setUserToUpdate(user);
        updateUserCase.execute(new BaseProfileSubscriber<UpdateUserView, String>(updateUserView) {
            @Override public void onNext(String message) {
                profileView.hideProgress();
                profileView.onSuccess(message);
            }
        });
    }

    @Override public void destroy() {
        updateUserCase.unsubscribe();
    }
}
