package edu.ftcsushi.util;

/**
 * Implementors of this class will be able to process an event every time an
 * loop occurs during the active op-mode.
 */
public interface PeriodicRunnable {
    /**
     * This method will be called for each loop
     */
    void onPeriodic();
}
