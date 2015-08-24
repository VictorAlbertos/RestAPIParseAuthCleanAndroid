package views;

public interface ProfileView extends View {
    void showProgress();
    void hideProgress();
    void onError(String message);
}
