package presenters;

import org.junit.Test;
import org.mockito.Mock;

import interactors.LogoutUseCase;
import respositories.SessionRepository;
import rx.Observable;
import rx.Subscriber;
import views.LogoutView;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class LogoutPresenterTest extends BasePresenterTest {
    @Mock private LogoutView logoutView;

    @Test public void when_Logout_Success_Then_On_Success_Is_Called() {
        LogoutPresenter logoutPresenter = new LogoutPresenter(new LogoutUseCaseSuccess());
        logoutPresenter.attachView(logoutView);
        logoutPresenter.doLogout();

        verify(logoutView, timeout(WAIT).times(1)).onSuccess(any(String.class));
        verify(logoutView, timeout(WAIT).times(0)).onError(any(String.class));
    }

    @Test public void when_Logout_Failure_Then_On_Error_Is_Called() {
        LogoutPresenter logoutPresenter = new LogoutPresenter(new LogoutUseCaseFailure());
        logoutPresenter.attachView(logoutView);
        logoutPresenter.doLogout();

        verify(logoutView, timeout(WAIT).times(0)).onSuccess(any(String.class));
        verify(logoutView, timeout(WAIT).times(1)).onError(any(String.class));
    }

    private class LogoutUseCaseSuccess extends LogoutUseCase {
        LogoutUseCaseSuccess() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override protected Observable<String> buildUseCaseObservable() {
            return Observable.just("Great");
        }
    }

    private class LogoutUseCaseFailure extends LogoutUseCase {
        LogoutUseCaseFailure() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override protected Observable<String> buildUseCaseObservable() {
            return Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    subscriber.onError(new RuntimeException("Not great"));
                }
            });
        }
    }
}
