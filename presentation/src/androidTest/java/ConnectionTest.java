import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import activities.CredentialsActivity_;
import activities.LaunchActivity_;
import activities.ProfileActivity_;
import edu.victoralbertos.restapiparseauthcleanandroid.R;


@RunWith(AndroidJUnit4.class)
public class ConnectionTest extends ActivityInstrumentationTestCase2 {
    private final static int WAIT_SERVER_RESPONSE = 500;
    private Solo mSolo;
    private final UserMock mUserMock;

    public ConnectionTest() {
        super(LaunchActivity_.class);
        mUserMock = new UserMock();
    }

    @Before public void setUp() {
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mSolo = new Solo(getInstrumentation());
        getActivity();
    }

    @Test @LargeTest public void start() {
        signUp();
        updateUser();
        logOut();
        login();
    }

    private void signUp() {
        mSolo.waitForActivity(CredentialsActivity_.class);

        mSolo.enterText((EditText) mSolo.getView(R.id.sign_up_et_username), mUserMock.getUsername());
        mSolo.enterText((EditText) mSolo.getView(R.id.sign_up_et_email), mUserMock.getEmail());
        mSolo.enterText((EditText) mSolo.getView(R.id.sign_up_et_phone), mUserMock.getPhone());
        mSolo.enterText((EditText) mSolo.getView(R.id.sign_up_et_password), mUserMock.getPassword());

        mSolo.clickOnView(mSolo.getView(R.id.sign_up_bt_submit));
        mSolo.sleep(WAIT_SERVER_RESPONSE);
        mSolo.assertCurrentActivity("Wrong activity!", ProfileActivity_.class);
    }

    private void updateUser() {
        String phoneUpdate =  mUserMock.getPhone() + "666";
        mSolo.clearEditText((EditText) mSolo.getView(R.id.et_phone));
        mSolo.enterText((EditText) mSolo.getView(R.id.et_phone), phoneUpdate);

        mSolo.clickOnView(mSolo.getView(R.id.bt_update));
        mSolo.sleep(WAIT_SERVER_RESPONSE);

        mSolo.goBack();

        launchActivity(getActivity().getApplicationContext().getPackageName(), LaunchActivity_.class, null);
        mSolo.waitForActivity(ProfileActivity_.class);
        mSolo.sleep(WAIT_SERVER_RESPONSE);

        String candidatePhoneUpdate =((EditText) mSolo.getView(R.id.et_phone)).getText().toString();
        assertEquals("Attribute phone not updated!", phoneUpdate, candidatePhoneUpdate);
    }

    private void logOut() {
        mSolo.clickOnView(mSolo.getView(R.id.bt_logout));
        mSolo.assertCurrentActivity("Wrong activity!", CredentialsActivity_.class);
    }

    private void login() {
        mSolo.enterText((EditText) mSolo.getView(R.id.login_et_username), mUserMock.getUsername());
        mSolo.enterText((EditText) mSolo.getView(R.id.login_et_password), mUserMock.getPassword());
        mSolo.clickOnView(mSolo.getView(R.id.login_bt_submit));

        mSolo.sleep(WAIT_SERVER_RESPONSE);
        mSolo.assertCurrentActivity("Wrong activity!", ProfileActivity_.class);
    }


    @Override public void tearDown() throws Exception {
        mSolo.finishOpenedActivities();
    }

    private class UserMock {
        private final String username, email, phone, password;

        public UserMock() {
            long time = System.currentTimeMillis();
            username = "ut" + time;
            email = "et@" + time + "t.com";
            phone = "pht" + time;
            password = "pat" + time;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getPassword() {
            return password;
        }
    }
}
