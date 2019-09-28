package ru.rz.musicat.media.interfaces;

public interface ProgressReporter {
    void resetProgress();
    void reportProgress(double progress);
}
