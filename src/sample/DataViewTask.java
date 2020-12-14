package sample;

import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

/**
 * @author liuxf
 * @version 1.0
 * @date 2020/12/10 9:59
 */
public class DataViewTask extends Task {


    private TextArea textArea;

    private String data;

    public DataViewTask(TextArea textArea, String data) {

        this.textArea = textArea;
        this.data = data;
    }

    public DataViewTask() {
    }

    @Override
    protected Object call() throws Exception {
        return null;
    }

    @Override
    protected void succeeded() {
        if (textArea.getText().split("\n").length >= 100) {
            textArea.clear();
            textArea.insertText(0, data + System.lineSeparator());
        }
        textArea.insertText(textArea.getLength(), data + System.lineSeparator());
        super.succeeded();
    }

}
