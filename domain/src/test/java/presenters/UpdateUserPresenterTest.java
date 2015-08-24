package presenters;

import org.junit.Test;
import org.mockito.Mock;

import entities.User;
import interactors.UpdateUserCase;
import respositories.SessionRepository;
import rx.Observable;
import rx.Subscriber;
import views.UpdateUserView;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class UpdateUserPresenterTest extends BasePresenterTest {
    @Mock private UpdateUserView updateUserView;

    @Test public void when_Update_User_Success_Then_On_Success_Is_Called() {
        UpdateUserPresenter updateUserPresenter = new UpdateUserPresenter(new UpdateUserCaseSuccess());
        updateUserPresenter.attachView(updateUserView);
        updateUserPresenter.doUpdateUser(new User());

        verify(updateUserView, timeout(WAIT).times(1)).onSuccess(any(String.class));
        verify(updateUserView, timeout(WAIT).times(0)).onError(any(String.class));
    }

    @Test public void when_Update_User_Failure_Then_On_Error_Is_Called() {
        UpdateUserPresenter updateUserPresenter = new UpdateUserPresenter(new UpdateUserCaseFailure());
        updateUserPresenter.attachView(updateUserView);
        updateUserPresenter.doUpdateUser(null);

        verify(updateUserView, timeout(WAIT).times(0)).onSuccess(any(String.class));
        verify(updateUserView, timeout(WAIT).times(1)).onError(any(String.class));
    }

    private class UpdateUserCaseSuccess extends interactors.UpdateUserCase {
        UpdateUserCaseSuccess() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override protected Observable<String> buildUseCaseObservable() {
            return Observable.just("Great");
        }
    }

    private class UpdateUserCaseFailure extends UpdateUserCase {
        UpdateUserCaseFailure() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override protected Observable<String> buildUseCaseObservable() {
            return Observable.create(new Observable.OnSubscribe<String>() {
                @Override  public void call(Subscriber<? super String> subscriber) {
                    subscriber.onError(new RuntimeException("No great!"));
                }
            });
        }
    }
}
