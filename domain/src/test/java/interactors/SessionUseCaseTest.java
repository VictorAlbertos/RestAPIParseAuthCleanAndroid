package interactors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import entities.User;
import respositories.SessionRepository;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(Parameterized.class)
public class SessionUseCaseTest extends BaseSessionUseCaseTest {
    private SessionUseCase sessionUseCase;

    @Parameterized.Parameter(value = 0) public Observable observable;
    @Parameterized.Parameter(value = 1) public int expectedEvents;

    @Override public void setUp() {
        super.setUp();
        sessionUseCase = new SessionUseCaseTestClass();
    }

    @Parameterized.Parameters public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Observable.empty(), 0}, {Observable.just(""), 1}, {Observable.just(new User()), 1}
        });
    }

    @Test public void when_Build_Observable_Then_Get_Correct_Result() {
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        sessionUseCase.execute(testSubscriber);
        testSubscriber.awaitTerminalEvent();

        assertThat(testSubscriber.getOnNextEvents().size(), is(expectedEvents));
    }

    @Test public void when_Session_UseCase_Unsubscribe_Then_Attached_Subscriber_Is_Unsubscribed() {
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        sessionUseCase.execute(testSubscriber);
        sessionUseCase.unsubscribe();

        assertThat(testSubscriber.isUnsubscribed(), is(true));
    }

    private final class SessionUseCaseTestClass extends SessionUseCase {
        public SessionUseCaseTestClass() {
            super(mock(SessionRepository.class), subscribeOn, observeOn);
        }

        @Override protected Observable buildUseCaseObservable() {
            return observable;
        }
    }
}
