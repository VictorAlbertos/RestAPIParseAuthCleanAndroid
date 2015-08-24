package views;

public interface CredentialsView extends View {
    void showProgress();
    void hideProgress();
    void onError(String message);
    void onSuccess(String message);
    void goToProfile();
}
