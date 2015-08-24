package fragments;

import android.view.View;
import android.widget.EditText;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import edu.victoralbertos.restapiparseauthcleanandroid.R;
import entities.User;
import presenters.SignUpPresenter;
import views.SignUpView;

@EFragment(R.layout.fragment_sign_up)
public class FragmentSignUp extends BaseFragment implements SignUpView {
    @Inject  SignUpPresenter signUpPresenter;
    @ViewById protected View sign_up_pb_loading, sign_up_bt_submit;
    @ViewById protected EditText sign_up_et_username, sign_up_et_email, sign_up_et_phone, sign_up_et_password;

    @Override protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override protected void initViews() {
        super.initViews();
        signUpPresenter.attachView(this);
    }

    @Override public void showProgress() {
        sign_up_pb_loading.setVisibility(View.VISIBLE);
        sign_up_bt_submit.setVisibility(View.INVISIBLE);
    }

    @Override public void hideProgress() {
        sign_up_pb_loading.setVisibility(View.INVISIBLE);
        sign_up_bt_submit.setVisibility(View.VISIBLE);
    }

    @Override public void onError(String message) {
        super.showToast(message);
    }

    @Override public void onSuccess(String message) {
        super.showToast(message);
    }

    @Override public void goToProfile() {
        navigator.toProfile(getActivity());
    }

    @Click protected void sign_up_bt_submit() {
        String username = sign_up_et_username.getText().toString();
        String email = sign_up_et_email.getText().toString();
        String phone = sign_up_et_phone.getText().toString();
        String password = sign_up_et_password.getText().toString();

        signUpPresenter.doSignUp(new User(username, email, phone, password));
    }
}
