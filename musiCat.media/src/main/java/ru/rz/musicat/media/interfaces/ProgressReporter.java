package ru.rz.musicat.media.interfaces;

public interface ProgressReporter {
    //void resetProgress();
    void reportProgress(double progress);

    void pushSection(int nSections);
    void startSection();
    void endSection();
    void popSection();
}
