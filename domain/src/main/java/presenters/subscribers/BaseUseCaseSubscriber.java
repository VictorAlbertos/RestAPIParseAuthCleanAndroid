package presenters.subscribers;

import rx.Subscriber;

public abstract class BaseUseCaseSubscriber<T> extends Subscriber<T> {
    @Override public void onCompleted() {}

    @Override public void onError(Throwable e) {}

    @Override public void onNext(T t) {}
}
