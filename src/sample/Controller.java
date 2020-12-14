package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;

/**
 * @author Administrator
 */
public class Controller {

    private Stage primaryStage;

    @FXML
    private Button startServer;


    @FXML
    private TextField localPath;

    @FXML
    private Button selectFilePath;

    @FXML
    private TextField port;

    @FXML
    private TextArea dataView;

    @FXML
    private ProgressBar progress;

    @FXML
    private Label proValue;


    @FXML
    private ChoiceBox bufSizeChoice;

    @FXML
    private Button endServer;

    @FXML
    private Button cleanTextArea;


    private String bufStr = "1024*5";


    private long bufSize;


    SocketThread socketThread;

    @FXML
    public void cleanTextArea() {
        dataView.clear();
    }

    @FXML
    public void startServe(ActionEvent actionEvent) throws Exception {

        if (!valid()) {
            return;
        }

        init();

        socketThread = new SocketThread();

        socketThread.start();

        endServer.setDisable(false);

        startServer.setDisable(true);

        new Thread(new DataViewTask(dataView, "服务启动成功。。。")).start();

        LogService.logInfo("服务启动成功。。。");

    }

    @FXML
    public void endServe(ActionEvent actionEvent) throws Exception {

        if (socketThread.isAlive()) {

            try {
                socketThread.closeServer();
            } catch (Exception e) {
                LogService.logError(e.getLocalizedMessage());
            }


            socketThread.interrupt();
        }
        LogService.logInfo("服务停止。。。");
        new Thread(new DataViewTask(dataView, "服务停止...")).start();
        endServer.setDisable(true);
        port.setEditable(true);
        selectFilePath.setDisable(false);
        bufSizeChoice.setDisable(false);
        startServer.setDisable(false);

    }

    @FXML
    public void selectFilePath(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(primaryStage);
        String path = file.getPath();
        localPath.setText(path);
    }


    @FXML
    public void choiceBoxBuf() {

        bufStr = (String) bufSizeChoice.getSelectionModel().getSelectedItem();

    }

    private void init() {

        switch (bufStr) {
            case "1024*10":
                bufSize = 1024 * 10;
                break;
            case "1024*100":
                bufSize = 1024 * 100;
                break;
            default:
                bufSize = 1024 * 5;

        }

        System.out.println(bufSize);

        port.setEditable(false);
        selectFilePath.setDisable(true);
        bufSizeChoice.setDisable(true);


    }

    private boolean valid() {

        String path = localPath.getText();

        String portStr = port.getText();

        if (portStr.length() == 0) {

            LogService.logError("端口为空");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("端口为空");
            alert.setContentText("port is null");
            alert.showAndWait();
            return false;
        }

        if (path.length() == 0) {

            LogService.logError("保存路径为空");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("保存路径为空");
            alert.setContentText("savePath is null");
            alert.showAndWait();
            return false;
        }

        return true;

    }

    /**
     * 服务端
     *
     * @throws Exception
     */
    class SocketThread extends Thread {

        ServerSocket serverSocket;


        boolean isClose = false;

        int serverPort = Integer.valueOf(port.getText());

        public void closeServer() throws IOException {

            isClose = true;

            new Socket("localhost",serverPort);

           // serverSocket.close();

        }

        @Override
        public void run() {

            super.run();

            try {
                serverSocket = new ServerSocket(serverPort);

            } catch (Exception e) {


                LogService.logError("启动异常。。。" + e.getLocalizedMessage());

                new Thread(new AlertTask(Alert.AlertType.ERROR, "启动异常", "start error" + e.getMessage())).start();

                return;
            }

            while (!isClose) {
                try {

                    Socket socket = serverSocket.accept();

                    if (isClose){
                        socket.close();
                        serverSocket.close();
                        break;
                    }


                    int bufferSize = 1024 * 1000;

                    byte[] buf = new byte[bufferSize];
                    //传输完成的数据长度
                    long donelen = 0;

                    //接入地址
                    new Thread(new DataViewTask(dataView, socket.getInetAddress().getHostAddress() + "---已连接")).start();

                    DataInputStream dis = new DataInputStream(socket.getInputStream());


                    String filePath = localPath.getText() + File.separator;

                    String remoteFile = dis.readUTF();

                    String filePathTo = filePath + remoteFile;

                    File file = new File(filePathTo);

                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }

                    //文件长度
                    Long fileLen = dis.readLong();


                    new Thread(new DataViewTask(dataView, "开始接收文件---" + file.getName())).start();


                    DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));

                    int read = 0;
                    while (true) {
                        if (dis != null && socket.isConnected() && !socket.isClosed()) {
                            read = dis.read(buf);

                        }
                        if (read == -1) {
                            break;
                        }

                        donelen += read;

                        DecimalFormat df = new DecimalFormat("#.00");

                        String pro = df.format((double) donelen / fileLen);

                        new Thread(new ProgressTask(progress, Double.valueOf(pro))).start();

                        dos.write(buf, 0, read);
                    }

                    dos.flush();

                    if (donelen == fileLen) {
                        new Thread(new DataViewTask(dataView, file.getName() + "---接收完成" + System.lineSeparator())).start();
                        LogService.logInfo(file.getName() + "---接收完成");
                    } else {

                        LogService.logError("来自" + socket.getInetAddress().getHostName() + "的" + file.getName() + "---接收不完整已删除。");
                        new Thread(new DataViewTask(dataView, "来自" + socket.getInetAddress().getHostName() +
                                "的" + file.getName() + "---接收不完整已删除。")).start();

                        file.delete();
                    }

                    dis.close();
                    dos.close();

                } catch (Exception e) {

                    e.printStackTrace();
                    LogService.logError("接收异常。。。" + e.getLocalizedMessage());

                    new Thread(new AlertTask(Alert.AlertType.ERROR, "接收异常", "Receive error" + e.getMessage())).start();

                }

            }

        }
    }

}
