## 基于socket的文件传输桌面程序

### 1、程序构建

程序使用javafx基于socket开发的一个java桌面程序

1、可自定义启动端口

2、可选择没包数据大小

3、可选择存放目录

4、支持客户端携带目录

5、自定义日志模块除了jdk没有引入别依赖

### 2、程序使用

1、启动程序 按设定开启服务

2、客户端demo

```java
public void transClient(String path) throws Exception {

        File file = new File(path);

        File[] list = file.listFiles();

        int bufferSize = 1024 * 100;

        byte[] buf = new byte[bufferSize];

        for (File f : list) {

            try (
                    Socket socket = new Socket("127.0.0.1", 8888);
                    DataInputStream dis = new DataInputStream(new FileInputStream(f.getAbsolutePath()));
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            ) {
                //自定义追加目录
                String appendPath = "test";
                dos.writeUTF( appendPath+File.separator + f.getName());
                dos.flush();

                dos.writeLong(f.length());

                dos.flush();

                int len = 0;
                while ((len = dis.read(buf)) != -1) {
                    dos.write(buf, 0, len);
                }
                dos.flush();
                dis.close();
                dos.close();
                socket.close();

                log.info(f.getName() + "---传输完成");
            } catch (Exception e) {
                log.info(ExceptionUtils.getStackTrace(e));
            }
        }
    }
```

### 3、目录下文件遍历

```java
 public static void loopFileByPath(List<File> list, String path) {

        File file = new File(path);

        File[] files = file.listFiles();

        for (File f : files) {
            if (f.isFile()) {
                list.add(f);
            } else {
                loopFileByPath(list, f.getPath());
            }
        }
    }

```



# Apache 2.0 license