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

public class LogoutUseCaseTest extends BaseSessionUseCaseTest {
    private LogoutUseCase logoutUseCase;
    @Mock private SessionRepository sessionRepository;

    @Override public void setUp() {
        super.setUp();
        logoutUseCase = new LogoutUseCase(sessionRepository, subscribeOn, observeOn);
    }

    @Test public void when_Logout_With_Active_Session_Then_Get_A_Response_As_String() {
        when(sessionRepository.askForLogout()).thenReturn(Observable.just("Great!"));

        TestSubscriber<String> subscriber = new TestSubscriber<>();
        logoutUseCase.execute(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test public void when_Logout_Without_Active_Session_Then_Throw_An_Exception_On_Subscriber() {
        when(sessionRepository.askForLogout()).thenReturn(Observable.create(
            new Observable.OnSubscribe<String>() {
                @Override public void call(Subscriber<? super String> subscriber) {
                    subscriber.onError(new RuntimeException("Not Great"));
                }
            })
        );

        TestSubscriber<String> subscriber = new TestSubscriber<>();
        logoutUseCase.execute(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(1));
    }
}
