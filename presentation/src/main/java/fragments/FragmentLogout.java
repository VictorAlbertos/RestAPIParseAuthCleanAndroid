package fragments;

import android.view.View;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import edu.victoralbertos.restapiparseauthcleanandroid.R;
import presenters.LogoutPresenter;
import views.LogoutView;

@EFragment(R.layout.fragment_logout)
public class FragmentLogout extends BaseFragment implements LogoutView {
    @ViewById protected View pb_logout_loading, bt_logout;
    @Inject LogoutPresenter logoutPresenter;

    @Override protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override protected void initViews() {
        super.initViews();
        logoutPresenter.attachView(this);
    }

    @Override public void showProgress() {
        pb_logout_loading.setVisibility(View.VISIBLE);
        bt_logout.setVisibility(View.INVISIBLE);
    }

    @Override public void hideProgress() {
        pb_logout_loading.setVisibility(View.INVISIBLE);
        bt_logout.setVisibility(View.VISIBLE);
    }

    @Override public void onError(String message) {
        super.showToast(message);
    }

    @Override public void onSuccess(String message) {
        super.showToast(message);
    }

    @Override public void toCredentials() {
        navigator.toCredentials(getActivity());
    }

    @Click protected void bt_logout() {
        logoutPresenter.doLogout();
    }
}
