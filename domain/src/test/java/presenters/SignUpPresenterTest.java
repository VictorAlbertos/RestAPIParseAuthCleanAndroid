package presenters;

import org.junit.Test;
import org.mockito.Mock;

import entities.User;
import interactors.SignUpUseCase;
import respositories.SessionRepository;
import rx.Observable;
import rx.Subscriber;
import views.SignUpView;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class SignUpPresenterTest extends BasePresenterTest {
    @Mock private SignUpView signUpView;

    @Test public void when_Sign_Up_Success_Then_Go_To_Profile_Is_Called() {
        SignUpPresenter loginPresenter = new SignUpPresenter(new SignUpUseCaseSuccess());
        loginPresenter.attachView(signUpView);
        loginPresenter.doSignUp(new User());

        verify(signUpView, timeout(WAIT).times(1)).goToProfile();
    }

    @Test public void when_Sign_Up_Failure_Then_Go_To_Profile_Is_Not_Called() {
        SignUpPresenter loginPresenter = new SignUpPresenter(new SignUpUseCaseFailure());
        loginPresenter.attachView(signUpView);
        loginPresenter.doSignUp(new User());

        verify(signUpView, timeout(WAIT).times(0)).goToProfile();
        verify(signUpView, timeout(WAIT).times(1)).onError(any(String.class));
    }

    private class SignUpUseCaseSuccess extends SignUpUseCase {
        SignUpUseCaseSuccess() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override protected Observable<String> buildUseCaseObservable() {
            return Observable.just("Great");
        }
    }

    private class SignUpUseCaseFailure extends SignUpUseCase {
        SignUpUseCaseFailure() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override protected Observable<String> buildUseCaseObservable() {
            return Observable.create(new Observable.OnSubscribe<String>() {
                @Override public void call(Subscriber<? super String> subscriber) {
                    subscriber.onError(new RuntimeException("Not great"));
                }
            });
        }
    }

}
