package navigation;

import android.content.Context;

import javax.inject.Inject;

import activities.CredentialsActivity_;
import activities.ProfileActivity_;


public class Navigator {
    @Inject public void Navigator() {}

    public void toCredentials(Context context) {
        if (context != null)
            CredentialsActivity_.intent(context).start();
    }

    public void toProfile(Context context) {
        if (context != null)
            ProfileActivity_.intent(context).start();
    }
}
