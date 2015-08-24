package presenters;

import org.junit.Test;
import org.mockito.Mock;

import interactors.LoginUseCase;
import respositories.SessionRepository;
import rx.Observable;
import rx.Subscriber;
import views.LoginView;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class LoginPresenterTest extends BasePresenterTest {
    @Mock private LoginView loginView;

    @Test public void when_Login_Success_Then_Go_To_Profile_Is_Called() {
        LoginPresenter loginPresenter = new LoginPresenter(new LoginUseCaseSuccess());
        loginPresenter.attachView(loginView);
        loginPresenter.doLogin("aValidUsername", "aValidPassword");

        verify(loginView, timeout(WAIT).times(1)).goToProfile();
    }

    @Test public void when_Login_Failure_Then_Go_To_Profile_Is_Not_Called() {
        LoginPresenter loginPresenter = new LoginPresenter(new LoginUseCaseFailure());
        loginPresenter.attachView(loginView);
        loginPresenter.doLogin("anInvalidUsername", "anInvalidPassword");

        verify(loginView, timeout(WAIT).times(0)).goToProfile();
        verify(loginView, timeout(WAIT).times(1)).onError(any(String.class));
    }

    private class LoginUseCaseSuccess extends LoginUseCase {
        public LoginUseCaseSuccess() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override protected Observable<String> buildUseCaseObservable() {
            return Observable.just("Great");
        }
    }

    private class LoginUseCaseFailure extends LoginUseCase {
        public LoginUseCaseFailure() {
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
