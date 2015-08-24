package views;

public interface LogoutView extends ProfileView {
    void onSuccess(String message);
    void toCredentials();
}
