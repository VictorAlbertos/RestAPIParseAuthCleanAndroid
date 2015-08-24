package schedulers;

import rx.Scheduler;

public interface ObserveOn {
    Scheduler getScheduler();
}
