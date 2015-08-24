package fragments;

import android.view.View;
import android.widget.EditText;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import edu.victoralbertos.restapiparseauthcleanandroid.R;
import entities.User;
import presenters.GetUserPresenter;
import presenters.UpdateUserPresenter;
import views.GetUserView;
import views.UpdateUserView;

@EFragment(R.layout.fragment_profile)
public class FragmentProfile extends BaseFragment implements UpdateUserView, GetUserView {
    @ViewById protected EditText et_username, et_email, et_phone;
    @ViewById protected View pb_loading, bt_update;
    @Inject UpdateUserPresenter updateUserPresenter;
    @Inject GetUserPresenter getUserPresenter;

    @Override protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override protected void initViews() {
        super.initViews();
        updateUserPresenter.attachView(this);
        getUserPresenter.attachView(this);
    }

    @Override public void showProgress() {
        bt_update.setVisibility(View.INVISIBLE);
        pb_loading.setVisibility(View.VISIBLE);
    }

    @Override public void hideProgress() {
        bt_update.setVisibility(View.VISIBLE);
        pb_loading.setVisibility(View.INVISIBLE);
    }

    @Override public void onError(String message) {
        super.showToast(message);
    }

    @Override public void onSuccess(User user) {
        et_username.setText(user.getUsername());
        et_email.setText(user.getEmail());
        et_phone.setText(user.getPhone());
    }

    @Override public void onSuccess(String message) {
        super.showToast(message);
    }

    @Click protected void bt_update() {
        String username = et_username.getText().toString();
        String email = et_email.getText().toString();
        String phone = et_phone.getText().toString();

        User user = new User(username, email, phone);
        updateUserPresenter.doUpdateUser(user);
    }
}