package schedulers;

import rx.Scheduler;

public interface SubscribeOn {
    Scheduler getScheduler();
}
