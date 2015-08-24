package activities;

import org.androidannotations.annotations.EActivity;

import javax.inject.Inject;

import presenters.LaunchPresenter;
import views.LaunchView;

@EActivity
public class LaunchActivity extends BaseActivity implements LaunchView {
    @Inject LaunchPresenter launchPresenter;

    @Override protected void init() {
        super.init();

        getApplicationComponent().inject(this);
        launchPresenter.attachView(this);
    }

    @Override public void goToCredentials() {
        navigator.toCredentials(this);
    }

    @Override public void goToProfile() {
        navigator.toProfile(this);
    }
}
