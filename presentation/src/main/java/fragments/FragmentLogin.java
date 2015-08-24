package fragments;

import android.view.View;
import android.widget.EditText;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import edu.victoralbertos.restapiparseauthcleanandroid.R;
import presenters.LoginPresenter;
import views.LoginView;

@EFragment(R.layout.fragment_login)
public class FragmentLogin extends BaseFragment implements LoginView {
    @Inject LoginPresenter loginPresenter;
    @ViewById protected View login_pb_loading, login_bt_submit;

    @Override protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override protected void initViews() {
        super.initViews();
        loginPresenter.attachView(this);
    }

    @Override public void showProgress() {
        login_bt_submit.setVisibility(View.INVISIBLE);
        login_pb_loading.setVisibility(View.VISIBLE);
    }

    @Override public void hideProgress() {
        login_bt_submit.setVisibility(View.VISIBLE);
        login_pb_loading.setVisibility(View.INVISIBLE);
    }

    @Override public void onError(String message) {
        super.showToast(message);
    }

    @Override public void onSuccess(String message) {
        super.showToast(message);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        loginPresenter.destroy();
    }

    @Override public void goToProfile() {
        navigator.toProfile(getActivity());
    }

    @ViewById protected EditText login_et_username, login_et_password;
    @Click protected void login_bt_submit() {
        String username = login_et_username.getText().toString();
        String password = login_et_password.getText().toString();
        loginPresenter.doLogin(username, password);
    }
 }
