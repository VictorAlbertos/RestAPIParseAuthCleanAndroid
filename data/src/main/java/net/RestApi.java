package net;

import com.google.gson.JsonObject;

import javax.inject.Inject;

import entities.Credentials;
import entities.User;
import rx.Observable;

public class RestApi {
    private final Endpoints endpoints;

    @Inject public RestApi(Endpoints endpoints) {
        this.endpoints =  endpoints;
    }

    public Observable<Credentials> login(String username, String password) {
        return endpoints.login(username, password);
    }

    public Observable<Credentials> signUp(User user) {
        return endpoints.signUp(user);
    }

    public Observable<User> currentSession(String token) {
        return endpoints.currentSession(token);
    }

    public Observable<JsonObject> update(String token, String objectId, User user) {
        return endpoints.update(token, objectId, user);
    }
}
