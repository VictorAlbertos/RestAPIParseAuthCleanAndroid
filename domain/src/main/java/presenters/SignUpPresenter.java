package presenters;

import javax.inject.Inject;

import entities.User;
import interactors.SignUpUseCase;
import presenters.subscribers.BaseCredentialsSubscriber;
import views.SignUpView;

public class SignUpPresenter implements Presenter<SignUpView> {
    private final SignUpUseCase singUpUseCase;
    private SignUpView signUpView;

    @Inject public SignUpPresenter(SignUpUseCase singUpUseCase) {
        this.singUpUseCase = singUpUseCase;
    }

    @Override public void attachView(SignUpView signUpView) {
        this.signUpView = signUpView;
    }

    @Override  public void destroy() {
        singUpUseCase.unsubscribe();
    }

    public void doSignUp(User user) {
        singUpUseCase.setUser(user);
        singUpUseCase.execute(new BaseCredentialsSubscriber<SignUpView>(signUpView){});
    }
}
