package sample;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;

/**
 * @author liuxf
 * @version 1.0
 * @date 2020/12/10 10:35
 */
public class AlertTask extends Task {


    private String content;


    private Alert.AlertType alertType;

    private String msg;

    public AlertTask(Alert.AlertType alertType, String msg, String content) {
        this.alertType = alertType;
        this.msg = msg;
        this.content = content;
    }

    public AlertTask() {
    }

    @Override
    protected Object call() throws Exception {
        return null;
    }

    @Override
    protected void succeeded() {


        Alert alert = new Alert(alertType);
        if (alertType == Alert.AlertType.ERROR) {
            alert.setTitle("Error Dialog");
        } else {
            alert.setTitle("info Dialog");
        }

        alert.setHeaderText(msg);
        alert.setContentText(content);
        alert.show();

        super.succeeded();
    }
}
