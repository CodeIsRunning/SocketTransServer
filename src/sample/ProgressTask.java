package sample;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

/**
 * @author liuxf
 * @version 1.0
 * @date 2020/12/10 15:46
 */
public class ProgressTask extends Task {

    private ProgressBar progressBar;


    private double progressValue;

    public ProgressTask(ProgressBar progressBar, double progressValue) {
        this.progressBar = progressBar;
        this.progressValue = progressValue;
    }

    @Override
    protected Object call() throws Exception {
        return null;
    }

    @Override
    protected void succeeded() {

        progressBar.setProgress(progressValue);

        super.succeeded();
    }
}
