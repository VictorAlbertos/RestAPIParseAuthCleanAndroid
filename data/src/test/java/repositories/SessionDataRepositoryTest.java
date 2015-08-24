package repositories;

import android.content.Context;

import com.google.gson.JsonObject;

import net.RestApi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import entities.Credentials;
import entities.User;
import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;
import storage.Persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class SessionDataRepositoryTest {
    @Mock private RestApi restApi;
    @Mock private Persistence persistence;
    @Mock private Context context;
    private SessionDataRepository sessionDataRepository;

    @Before public void setUp() {
       MockitoAnnotations.initMocks(this);
        sessionDataRepository = new SessionDataRepository(context, restApi, persistence);
    }

    @Test public void when_Login_With_Valid_Inputs_Then_Get_Credentials_And_Saved_It() {
        when(restApi.login(any(String.class), any(String.class))).thenReturn(Observable.just(new Credentials()));

        TestSubscriber<String> subscriber = new TestSubscriber<>();
        sessionDataRepository.askForLogin("username", "password").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnNextEvents().size(), is(1));
        verify(persistence).save(any(String.class), any(Object.class));
    }

    @Test public void when_Login_With_Invalid_Inputs_Then_Credentials_Is_Not_Saved() {
        when(restApi.login(any(String.class), any(String.class))).thenReturn(Observable.create(
                        new Observable.OnSubscribe<Credentials>() {
                            @Override
                            public void call(Subscriber<? super Credentials> subscriber) {
                                subscriber.onError(new RuntimeException("Not Great"));
                            }
                        })
        );

        TestSubscriber<String> subscriber = new TestSubscriber<>();
        sessionDataRepository.askForLogin("username", "password").subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(1));
        verifyZeroInteractions(persistence);
    }

    @Test public void when_Get_User_Being_Logged_Then_Get_User() {
        Credentials credentials = new Credentials();

        when(persistence.retrieve(any(String.class), eq(Credentials.class))).thenReturn(credentials);
        when(restApi.currentSession(null)).thenReturn(Observable.just(new User()));

        TestSubscriber<User> subscriber = new TestSubscriber<>();
        sessionDataRepository.askForUserLogged().subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test public void when_Get_User_Not_Being_Logged_Then_Throw_Exception_On_Subscribe() {
        TestSubscriber<User> subscriber = new TestSubscriber<>();
        sessionDataRepository.askForUserLogged().subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(1));
    }

    @Test public void when_Update_User_Being_Logged_Then_Get_User() {
        Credentials credentials = new Credentials();

        when(persistence.retrieve(any(String.class), eq(Credentials.class))).thenReturn(credentials);
        when(restApi.update(any(String.class), any(String.class), any(User.class))).thenReturn(Observable.just(new JsonObject()));

        TestSubscriber<String> subscriber = new TestSubscriber<>();
        sessionDataRepository.askForUpdateLoggedUser(new User()).subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test public void when_Update_User_Not_Being_Logged_Then_Throw_Exception_On_Subscribe() {
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        sessionDataRepository.askForUpdateLoggedUser(new User()).subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(1));
    }

    @Test public void when_Sign_Up_With_Valid_Inputs_Then_Get_Credentials_And_Saved_It() {
        when(restApi.signUp(any(User.class))).thenReturn(Observable.just(new Credentials()));

        TestSubscriber<String> subscriber = new TestSubscriber<>();
        sessionDataRepository.askForSignUp(new User()).subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnNextEvents().size(), is(1));
        verify(persistence).save(any(String.class), any(Object.class));
    }

    @Test public void when_Sign_Up_With_Invalid_Inputs_Then_Credentials_Is_Not_Saved() {
        when(restApi.signUp(null)).thenReturn(Observable.create(
                        new Observable.OnSubscribe<Credentials>() {
                            @Override
                            public void call(Subscriber<? super Credentials> subscriber) {
                                subscriber.onError(new RuntimeException("Not Great"));
                            }
                        })
        );

        TestSubscriber<String> subscriber = new TestSubscriber<>();
        sessionDataRepository.askForSignUp(null).subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(1));
        verifyZeroInteractions(persistence);
    }

    @Test public void when_Logout_With_Active_Session_Then_Get_A_Response_As_String() {
        when(persistence.retrieve(any(String.class), eq(Credentials.class))).thenReturn(new Credentials());

        TestSubscriber<String> subscriber = new TestSubscriber<>();
        sessionDataRepository.askForLogout().subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        verify(persistence).delete(Credentials.class.getName());
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test public void when_Logout_Without_Active_Session_Then_Throw_An_Exception_On_Subscriber() {
        when(persistence.retrieve(any(String.class), eq(Credentials.class))).thenReturn(null);

        TestSubscriber<String> subscriber = new TestSubscriber<>();
        sessionDataRepository.askForLogout().subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        assertThat(subscriber.getOnErrorEvents().size(), is(1));
    }
}
