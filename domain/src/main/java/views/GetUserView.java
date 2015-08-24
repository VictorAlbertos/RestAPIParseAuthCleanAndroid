package views;

import entities.User;

public interface GetUserView extends ProfileView {
    void onSuccess(User user);
}
