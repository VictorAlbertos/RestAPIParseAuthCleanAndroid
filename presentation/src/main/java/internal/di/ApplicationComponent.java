package internal.di;

import javax.inject.Singleton;

import dagger.Component;
import activities.BaseActivity;
import activities.LaunchActivity;
import fragments.BaseFragment;
import fragments.FragmentLogin;
import fragments.FragmentLogout;
import fragments.FragmentProfile;
import fragments.FragmentSignUp;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity baseActivity);
        void inject(LaunchActivity launchActivity);

    void inject(BaseFragment baseFragment);
        void inject(FragmentLogin fragmentLogin);
        void inject(FragmentSignUp fragmentSignUp);
        void inject(FragmentProfile fragmentProfile);
        void inject(FragmentLogout fragmentLogout);
}
