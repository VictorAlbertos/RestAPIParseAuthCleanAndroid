package presenters;


import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.schedulers.Schedulers;
import schedulers.ObserveOn;
import schedulers.SubscribeOn;

import static org.mockito.Mockito.when;

public class BasePresenterTest {
    protected final static int WAIT = 50;
    @Mock protected ObserveOn observeOn;
    @Mock protected SubscribeOn subscribeOn;

    @Before public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(observeOn.getScheduler()).thenReturn(Schedulers.newThread());
        when(subscribeOn.getScheduler()).thenReturn(Schedulers.newThread());
    }
}
