package interactors;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mock;

import entities.User;
import respositories.SessionRepository;
import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public class SignUpUseCaseTest extends BaseSessionUseCaseTest {
    private final static User A_VALID_USER = new User(), AN_INVALID_USER = null;
    @Mock private SessionRepository sessionRepository;
    private SignUpUseCase signUpUseCase;

    @Override public void setUp() {
        super.setUp();
        signUpUseCase = new SignUpUseCase(sessionRepository, subscribeOn, observeOn);

        when(sessionRepository.askForSignUp(A_VALID_USER)).thenReturn(Observable.just("Great"));
        when(sessionRepository.askForSignUp(AN_INVALID_USER)).thenReturn(Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onError(new RuntimeException("Not great"));
            }
        }));
    }

    @Test public void when_Sign_Up_With_Valid_Inputs_Then_Get_A_Response_As_String() {
        signUpUseCase.setUser(A_VALID_USER);
        subscribe_To_Sign_Up_And_Wait_Until_Observable_Is_Completed(0,1);
    }

    @Test public void when_Sign_Up_With_Invalid_Inputs_Then_Throw_An_Exception_On_Subscriber() {
        signUpUseCase.setUser(AN_INVALID_USER);
        subscribe_To_Sign_Up_And_Wait_Until_Observable_Is_Completed(1, 0);
    }

    private void subscribe_To_Sign_Up_And_Wait_Until_Observable_Is_Completed(int expectedErrors, int expectedEvents) {
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        signUpUseCase.execute(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), Matchers.is(expectedErrors));
        assertThat(subscriber.getOnNextEvents().size(), Matchers.is(expectedEvents));
    }
}
