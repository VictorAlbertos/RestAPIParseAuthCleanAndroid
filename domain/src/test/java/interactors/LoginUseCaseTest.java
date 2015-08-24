package interactors;

import org.junit.Test;
import org.mockito.Mock;

import respositories.SessionRepository;
import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class LoginUseCaseTest extends BaseSessionUseCaseTest {
    private final static String A_VALID_USERNAME = "aValidUsername", A_VALID_PASSWORD = "aValidPassword",
            AN_INVALID_USERNAME = "anInvalidUsername", AN_INVALID_PASSWORD = "anInvalidPassword";
    private LoginUseCase loginUseCase;
    @Mock private SessionRepository sessionRepository;

    @Override public void setUp() {
        super.setUp();
        loginUseCase = new LoginUseCase(sessionRepository, subscribeOn, observeOn);

        when(sessionRepository.askForLogin(A_VALID_USERNAME, A_VALID_PASSWORD)).thenReturn(Observable.just("Great"));
        when(sessionRepository.askForLogin(AN_INVALID_USERNAME, AN_INVALID_PASSWORD)).thenReturn(Observable.create(
            new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    subscriber.onError(new RuntimeException("Not Great"));
                }
            })
        );
    }

    @Test public void when_Login_With_Valid_Inputs_Then_Get_A_Response_As_String() {
        loginUseCase.doLogin(A_VALID_USERNAME, A_VALID_PASSWORD);
        subscribe_To_Login_And_Wait_Until_Observable_Is_Completed(0, 1);
    }

    @Test public void when_Login_With_Invalid_Inputs_Then_Throw_An_Exception_On_Subscriber() {
        loginUseCase.doLogin(AN_INVALID_USERNAME, AN_INVALID_PASSWORD);
        subscribe_To_Login_And_Wait_Until_Observable_Is_Completed(1, 0);
    }

    private void subscribe_To_Login_And_Wait_Until_Observable_Is_Completed(int expectedErrors, int expectedEvents) {
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        loginUseCase.execute(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(expectedErrors));
        assertThat(subscriber.getOnNextEvents().size(), is(expectedEvents));
    }
}
