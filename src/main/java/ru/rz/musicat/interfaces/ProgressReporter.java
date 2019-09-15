package ru.rz.musicat.interfaces;

public interface ProgressReporter {
    void resetProgress();
    void reportProgress(double progress);
}
