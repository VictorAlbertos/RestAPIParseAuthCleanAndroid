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

public class GetUserUseCaseTest extends BaseSessionUseCaseTest {
    private GetUserUseCase getUserUseCase;
    @Mock private SessionRepository sessionRepository;

    @Override public void setUp() {
        super.setUp();
        getUserUseCase = new GetUserUseCase(sessionRepository, subscribeOn, observeOn);
    }

    @Test public void when_There_Is_A_User_Logged_Then_Get_User() {
        when(sessionRepository.askForUserLogged()).thenReturn(Observable.just(new User()));

        TestSubscriber<User> subscriber = new TestSubscriber<>();
        getUserUseCase.execute(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnNextEvents().size(), is(1));;
    }

    @Test public void when_There_Is_Not_A_User_Logged_Then_Throw_An_Exception_On_Subscriber() {
        when(sessionRepository.askForUserLogged()).thenReturn(Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                subscriber.onError(new RuntimeException("Not User"));
            }
        }));

        TestSubscriber<User> subscriber = new TestSubscriber<>();
        getUserUseCase.execute(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(1));
    }
}
