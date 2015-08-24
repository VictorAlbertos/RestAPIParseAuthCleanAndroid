package activities;

import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import javax.inject.Inject;

import internal.di.ApplicationComponent;
import navigation.Navigator;
import utilities.AndroidApplication;

@EActivity
public abstract class BaseActivity extends AppCompatActivity {
    @Inject Navigator navigator;

    @AfterInject protected void init() {
        getApplicationComponent().inject(this);
    }

    @AfterViews protected void initViews() {
    }

    public ApplicationComponent getApplicationComponent() {
        return ((AndroidApplication)getApplication()).getApplicationComponent();
    }
}
