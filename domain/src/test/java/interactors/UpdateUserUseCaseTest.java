package interactors;

import org.junit.Test;
import org.mockito.Mock;

import entities.User;
import respositories.SessionRepository;
import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class UpdateUserUseCaseTest extends BaseSessionUseCaseTest {
    private final static User A_VALID_USER = new User(), AN_INVALID_USER = null;
    @Mock private SessionRepository sessionRepository;
    private UpdateUserCase updateUserCase;

    @Override public void setUp() {
        super.setUp();
        updateUserCase = new UpdateUserCase(sessionRepository, subscribeOn, observeOn);

        when(sessionRepository.askForUpdateLoggedUser(A_VALID_USER)).thenReturn(Observable.just("Great"));
        when(sessionRepository.askForUpdateLoggedUser(null)).thenReturn(Observable.create(
            new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    subscriber.onError(new RuntimeException("Not Great"));
                }
            })
        );
    }

    @Test public void when_Update_With_Valid_User_Get_An_String_As_Response() {
        updateUserCase.setUserToUpdate(A_VALID_USER);
        subscribe_To_Update_And_Wait_Until_Observable_Is_Completed(0, 1);
    }

    @Test public void when_Update_With_Invalid_User_Then_Throw_An_Exception_On_Subscriber() {
        updateUserCase.setUserToUpdate(AN_INVALID_USER);
        subscribe_To_Update_And_Wait_Until_Observable_Is_Completed(1, 0);
    }

    private void subscribe_To_Update_And_Wait_Until_Observable_Is_Completed(int expectedErrors, int expectedEvents) {
        TestSubscriber<String> subscriber = new TestSubscriber<>();

        updateUserCase.execute(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(expectedErrors));
        assertThat(subscriber.getOnNextEvents().size(), is(expectedEvents));
    }
}
