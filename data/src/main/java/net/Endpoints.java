package net;

import com.google.gson.JsonObject;

import entities.Credentials;
import entities.User;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface Endpoints {
    @Headers({ConfigEndpoints.HEADER_APP_ID, ConfigEndpoints.HEADER_REST_API_KEY})
    @GET("/login")
    Observable<Credentials> login(@Query("username") String username, @Query("password") String password);

    @Headers({ConfigEndpoints.HEADER_APP_ID, ConfigEndpoints.HEADER_REST_API_KEY})
    @POST("/users")
    Observable<Credentials> signUp(@Body User user);

    @Headers({ConfigEndpoints.HEADER_APP_ID, ConfigEndpoints.HEADER_REST_API_KEY})
    @GET("/users/me")
    Observable<User> currentSession(@Header("X-Parse-Session-Token") String token);

    @Headers({ConfigEndpoints.HEADER_APP_ID, ConfigEndpoints.HEADER_REST_API_KEY})
    @PUT("/users/{objectId}")
    Observable<JsonObject> update(@Header("X-Parse-Session-Token") String token,
                              @Path("objectId") String objectId, @Body User user);
}
