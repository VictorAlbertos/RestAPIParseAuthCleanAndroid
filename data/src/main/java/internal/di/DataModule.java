package internal.di;

import com.google.gson.JsonObject;

import net.ConfigEndpoints;
import net.Endpoints;

import dagger.Module;
import dagger.Provides;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

@Module
public class DataModule {
    @Provides Endpoints provideEndpoints() {
        return new RestAdapter.Builder()
                .setEndpoint(ConfigEndpoints.URL_BASE)
                .setErrorHandler(new ErrorHandler() {
                    @Override public Throwable handleError(RetrofitError cause) {
                        JsonObject jsonObject = (JsonObject) cause.getBodyAs(JsonObject.class);
                        String message = jsonObject.get("error").getAsString();
                        return new RuntimeException(message);
                    }
                }).build().create(Endpoints.class);
    }
}
