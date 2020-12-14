package sample;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author liuxf
 * @version 1.0
 * @date 2020/12/9 16:49
 */
public class LogService {


    public static void logInfo(String log) {

        String data = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        String pathStr = System.getProperty("user.dir") + File.separator + "logs" + File.separator + data + File.separator;

        File path = new File(pathStr);

        if (!path.exists()) {
            path.mkdirs();
        }

        String filePath = pathStr + data + "-info.log";

        try {

            RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");

            randomAccessFile.seek(randomAccessFile.length());

            log = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))+"---"+log+System.lineSeparator();

            randomAccessFile.write(log.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void logError(String log) {

        String errorData = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        String pathStr = System.getProperty("user.dir") + File.separator + "logs" + File.separator + errorData + File.separator;

        File path = new File(pathStr);

        if (!path.exists()) {
            path.mkdirs();
        }

        String filePath = pathStr + errorData + "-error.log";

        try {

            RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");

            randomAccessFile.seek(randomAccessFile.length());

            log = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))+"---"+log+System.lineSeparator();

            randomAccessFile.write(log.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
