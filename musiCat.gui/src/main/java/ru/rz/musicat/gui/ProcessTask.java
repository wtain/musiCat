package ru.rz.musicat.gui;

import javafx.concurrent.Task;
import ru.rz.musicat.media.MCApplication;
import ru.rz.musicat.media.ea.EAMusicStore;
import ru.rz.musicat.media.interfaces.ProgressReporter;

import java.util.Set;
import java.util.Stack;

public class ProcessTask extends Task<EAMusicStore> implements ProgressReporter {

    String path;
    Set<String> exts;
    Boolean recursive;

    public ProcessTask(String path, Set<String> exts, Boolean recursive) {
        this.path = path;
        this.exts = exts;
        this.recursive = recursive;
        initProgress();
    }

    @Override
    protected EAMusicStore call() throws Exception {
        MCApplication app = new MCApplication(msg -> updateMessage(msg), this);
        app.Run(path, exts, recursive);
        updateMessage("Done");

        return app.getStore();
    }

    /*@Override
    public void resetProgress() {
        updateProgress(0, 100);
    }*/

    Stack<Double> denominators;
    Stack<Integer> currentSection;
    Stack<Double> overallProgress;

    /*
1 (1/3)       0- 33
  1.1 (1/6)   0- 16
  1.2 (1/6)  16- 33
2 (1/3)      34- 67
  2.1 (1/9)  34- 43
  2.2 (1/9)  44- 55
  2.3 (1/9)  56- 67
3 (1/3)      67-100
  3.1 (1/12) 67- 75
  3.2 (1/12) 76- 84
  3.3 (1/12) 85- 93
  3.4 (1/12) 93-100
    * */

    void initProgress() {
        denominators = new Stack<>();
        currentSection = new Stack<>();
        overallProgress = new Stack<>();
    }

    double getDenominator() {
        return denominators.empty() ? 1.0 : denominators.peek();
    }

    int getCurrentSection() {
        return currentSection.empty() ? 0 : currentSection.peek();
    }

    double getOverallProgress() {
        return overallProgress.empty() ? 0.0 : overallProgress.peek();
    }

    double getTotalProgress(double progress) {
        return getOverallProgress() + (100 * getCurrentSection() + progress) * getDenominator();
    }

    @Override
    public void reportProgress(double progress) {
        updateProgress(progress, 100);
        System.out.println(String.format("progress=%f %%; denominator=%f; cs=%d; op=%f; tp=%f",
                progress, getDenominator(), getCurrentSection(), getOverallProgress(), getTotalProgress(progress)));

        //currentSection.peek()

        //double p =
        //updateProgress(progress, 100);
    }

    @Override
    public void pushSection(int nSections) {
        //updateProgress(0, 100);
        double denominator = 1.0;
        if (!denominators.empty())
            denominator = denominators.peek();
        denominators.push(denominator / nSections);
        currentSection.push(-1);
        //overallProgress.push();
    }

    @Override
    public void startSection() {
        int section = currentSection.peek() + 1;
        currentSection.pop();
        currentSection.push(section);
        //ov
        // erallProgress = (section-1) * denominators.peek();
    }

    @Override
    public void endSection() {

    }

    @Override
    public void popSection() {
        currentSection.pop();
        denominators.pop();
        //overallProgress.pop();
    }
}
