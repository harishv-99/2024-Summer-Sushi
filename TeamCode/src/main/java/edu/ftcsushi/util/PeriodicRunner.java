package edu.ftcsushi.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Register OpModeActiveLoopable functions to process each loop.  This is a Singleton; use
 * {@link #getInstance()} before calling other members.
 */
public class PeriodicRunner {
    private static PeriodicRunner instance;

    List<PeriodicRunnable> toUpdatePeriodically;

    private PeriodicRunner() {
        toUpdatePeriodically = new ArrayList<>(100);
    }

    static public PeriodicRunner getInstance() {
        // Create and initialize the singleton if required.
        if (instance == null) {
            instance = new PeriodicRunner();
        }

        return instance;
    }

    public void addPeriodicRunnable(PeriodicRunnable periodicRunnable) {
        toUpdatePeriodically.add(periodicRunnable);
    }

    public void removePeriodicRunnable(PeriodicRunnable periodicRunnable) {
        toUpdatePeriodically.remove(periodicRunnable);
    }

    public void runAllPeriodicRunnables() {
        for (PeriodicRunnable cur : toUpdatePeriodically)
            cur.onPeriodic();
    }
}
