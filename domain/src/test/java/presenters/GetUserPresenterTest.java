package presenters;

import org.junit.Test;
import org.mockito.Mock;

import entities.User;
import interactors.GetUserUseCase;
import respositories.SessionRepository;
import rx.Observable;
import rx.Subscriber;
import views.GetUserView;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class GetUserPresenterTest extends BasePresenterTest {
    @Mock private GetUserView getUserView;

    @Test public void when_Get_User_Success_Then_On_Success_Is_Called() {
        GetUserPresenter getUserPresenter = new GetUserPresenter(new GetUserUseCaseSuccess());
        getUserPresenter.attachView(getUserView);

        verify(getUserView, timeout(WAIT).times(1)).onSuccess(any(User.class));
        verify(getUserView, timeout(WAIT).times(0)).onError(any(String.class));
    }

    @Test public void when_Get_User_Failure_Then_On_Error_Is_Called() {
        GetUserPresenter getUserPresenter = new GetUserPresenter(new GetUserUseCaseFailure());
        getUserPresenter.attachView(getUserView);

        verify(getUserView, timeout(WAIT).times(0)).onSuccess(any(User.class));
        verify(getUserView, timeout(WAIT).times(1)).onError(any(String.class));
    }

    private class GetUserUseCaseSuccess extends GetUserUseCase {
        GetUserUseCaseSuccess() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override  protected Observable<User> buildUseCaseObservable() {
            return Observable.just(new User());
        }
    }

    private class GetUserUseCaseFailure extends GetUserUseCase {
        GetUserUseCaseFailure() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override  protected Observable<User> buildUseCaseObservable() {
            return Observable.create(new Observable.OnSubscribe<User>() {
                @Override public void call(Subscriber<? super User> subscriber) {
                    subscriber.onError(new RuntimeException("Not great"));
                }
            });
        }
    }
}
