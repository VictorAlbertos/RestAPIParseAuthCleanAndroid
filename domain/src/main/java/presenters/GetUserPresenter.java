package presenters;

import javax.inject.Inject;

import entities.User;
import interactors.GetUserUseCase;
import presenters.subscribers.BaseProfileSubscriber;
import views.GetUserView;

public class GetUserPresenter implements Presenter<GetUserView> {
    private final GetUserUseCase getUserUseCase;

    @Inject public GetUserPresenter(GetUserUseCase getUserUseCase) {
        this.getUserUseCase = getUserUseCase;
    }

    @Override public void attachView(GetUserView getUserView) {
        getUserUseCase.execute(new BaseProfileSubscriber<GetUserView,User>(getUserView) {
            @Override  public void onNext(User user) {
                profileView.hideProgress();
                profileView.onSuccess(user);
            }
        });
    }

    @Override public void destroy() {
        getUserUseCase.unsubscribe();
    }
}
