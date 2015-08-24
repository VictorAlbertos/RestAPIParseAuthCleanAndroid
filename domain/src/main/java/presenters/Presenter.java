package presenters;

import views.View;

public interface Presenter<T extends View> {
    void attachView(T view);
    void destroy();
}
