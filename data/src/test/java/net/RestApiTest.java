package net;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import entities.Credentials;
import entities.User;
import internal.di.DaggerDataComponent;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RestApiTest {
    private RestApi restApi;

    @Before public void setUp() {
        Endpoints endpoints = DaggerDataComponent.create().endpoints();
        restApi = new RestApi(endpoints);
    }

    @Test public void first_When_Sign_Up_With_Valid_Inputs_Then_Get_Credentials() {
        TestSubscriber<Credentials> subscriber = new TestSubscriber<>();
        restApi.signUp(makeValidUser()).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        credentials = subscriber.getOnNextEvents().get(0);
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test public void first_When_Sign_Up_With_Invalid_Inputs_Then_Throw_An_ExceptionOnSubscriber() {
        TestSubscriber<Credentials> subscriber = new TestSubscriber<>();
        restApi.signUp(makeInvalidUser()).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnErrorEvents().size(), is(1));
    }

    @Test public void second_When_Update_With_Valid_Inputs_Then_User_Is_Updated() {
        TestSubscriber<JsonObject> subscriber = new TestSubscriber<>();
        restApi.update(credentials.getSessionToken(), credentials.getObjectId(), makeValidUser()).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test public void second_When_Update_With_Invalid_Inputs_Then_User_Is_Not_Updated() {
        TestSubscriber<JsonObject> subscriber = new TestSubscriber<>();
        restApi.update("anInvalidToken", "anObjectId", makeInvalidUser()).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnErrorEvents().size(), is(1));
    }

    @Test public void third_When_Ask_For_User_With_Valid_Inputs_Then_Get_User() {
        TestSubscriber<User> subscriber = new TestSubscriber<>();
        restApi.currentSession(credentials.getSessionToken()).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test public void third_When_Ask_For_User_With_Invalid_Inputs_Then_Throw_An_Exception_On_Subscriber() {
        TestSubscriber<User> subscriber = new TestSubscriber<>();
        restApi.currentSession("anInvalidToken").subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnErrorEvents().size(), is(1));
    }

    @Test public void fourth_When_Login_With_Valid_Inputs_Then_Get_Credentials() {
        TestSubscriber<Credentials> subscriber = new TestSubscriber<>();
        restApi.login(makeValidUser().getUsername(), makeValidUser().getPassword()).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test public void fourth_When_Login_With_Invalid_Inputs_Then_Throw_An_ExceptionOnSubscriber() {
        TestSubscriber<Credentials> subscriber = new TestSubscriber<>();
        restApi.login("anInvalidUsername", "anInvalidPassword").subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnErrorEvents().size(), is(1));
    }

    private User makeInvalidUser() {
        return new User();
    }

    private static Credentials credentials;
    private static User user;
    private static User makeValidUser() {
        if (user != null) return user;

        long time = System.currentTimeMillis();
        String username = "ut" + time;
        String email = "et@" + time + "t.com";
        String phone = "pht" + time;
        String password = "pat" + time;
        return user = new User(username, email, phone, password);
    }

}
