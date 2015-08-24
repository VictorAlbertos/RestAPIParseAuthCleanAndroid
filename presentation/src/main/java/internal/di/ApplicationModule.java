package internal.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import schedulers.ObserveOn;
import schedulers.SubscribeOn;
import navigation.Navigator;
import repositories.SessionDataRepository;
import respositories.SessionRepository;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import utilities.AndroidApplication;

@Module(includes = DataModule.class) public class ApplicationModule {
    private final AndroidApplication application;

    public ApplicationModule(AndroidApplication application) {
        this.application = application;
    }

    @Provides @Singleton Context provideApplicationContext() {
        return application;
    }

    @Provides @Singleton public SessionRepository provideSessionRepository(SessionDataRepository sessionDataRepository) {
        return sessionDataRepository;
    }

    @Singleton @Provides Navigator provideNavigator() {
        return new Navigator();
    }

    @Singleton @Provides SubscribeOn provideSubscribeOn() {
        return (new SubscribeOn() {
            @Override public Scheduler getScheduler() {
                return Schedulers.newThread();
            }
        });
    }

    @Singleton @Provides ObserveOn provideObserveOn() {
        return (new ObserveOn() {
            @Override public Scheduler getScheduler() {
                return AndroidSchedulers.mainThread();
            }
        });
    }
}
