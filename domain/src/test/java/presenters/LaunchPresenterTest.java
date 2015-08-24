package presenters;

import org.junit.Test;
import org.mockito.Mock;

import entities.User;
import interactors.GetUserUseCase;
import respositories.SessionRepository;
import rx.Observable;
import rx.Subscriber;
import views.LaunchView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class LaunchPresenterTest extends BasePresenterTest {
    private LaunchPresenter launchPresenter;
    @Mock LaunchView launchView;

    @Test public void when_User_Logged_Then_Go_To_Profile_Is_Called() {
        GetUserUseCaseLogged getUserUseCase = new GetUserUseCaseLogged();
        launchPresenter = new LaunchPresenter(getUserUseCase);
        launchPresenter.attachView(launchView);

        verify(launchView, timeout(WAIT).times(0)).goToCredentials();
        verify(launchView, timeout(WAIT).times(1)).goToProfile();
    }

    @Test public void when_Not_User_Logged_Then_Go_To_Credentials_Is_Called() {
        GetUserUseCaseNotLogged getUserUseCase = new GetUserUseCaseNotLogged();
        launchPresenter = new LaunchPresenter(getUserUseCase);
        launchPresenter.attachView(launchView);

        verify(launchView, timeout(WAIT).times(0)).goToProfile();
        verify(launchView, timeout(WAIT).times(1)).goToCredentials();
    }

    private class GetUserUseCaseLogged extends GetUserUseCase {
        GetUserUseCaseLogged() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override protected Observable<User> buildUseCaseObservable() {
            return Observable.just(new User());
        }
    }

    private class GetUserUseCaseNotLogged extends GetUserUseCase {
        GetUserUseCaseNotLogged() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override protected Observable<User> buildUseCaseObservable() {
            return Observable.create(new Observable.OnSubscribe<User>() {
                @Override
                public void call(Subscriber<? super User> subscriber) {
                    subscriber.onError(new RuntimeException("Not User Logged"));
                }
            });
        }
    }
}
