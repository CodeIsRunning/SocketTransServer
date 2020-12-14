package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * @author liuxf
 * @version 1.0
 * @date 2020/12/10 10:51
 */
public class CloseAppTask extends Task {
    @Override
    protected Object call() throws Exception {
        return null;
    }

    @Override
    protected void succeeded() {
        Platform.exit();
        super.succeeded();
    }
}
