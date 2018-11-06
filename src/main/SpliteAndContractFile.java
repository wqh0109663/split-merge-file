package main;

import java.io.*;
import java.util.Properties;

/**
 * 文件切割
 *
 * @author wqh
 * @date 18-11-5
 */
public class SpliteAndContractFile {

    private static final int SIZE = 1024 * 1024 * 5;

    public static void main(String[] args) throws Exception {
        String location = getFileLocationByProperties();

        File file = new File(location);
        splite(file);


    }

    /**
     * 文件切割
     *
     * @param file 带切割的文件
     * @throws IOException IO异常
     */

    private static void splite(File file) throws IOException {
        File partFile = new File(file.getParent() + "/part");
        if (!partFile.exists()) {
            partFile.mkdirs();
        }
        FileInputStream inputStream = new FileInputStream(file);
        FileOutputStream outputStream = null;
        int len;
        int count = 1;
        byte[] bytes = new byte[SIZE];
        while ((len = inputStream.read(bytes)) != -1) {
            outputStream = new FileOutputStream(new File(partFile, count++ + ".part"));
            outputStream.write(bytes, 0, len);
        }
        Properties properties = new Properties();
        properties.setProperty("count", String.valueOf(--count));
        properties.setProperty("fileName", file.getName());
        FileOutputStream fileOutputStream = new FileOutputStream(new File(partFile, "splite.ini"));
        properties.store(fileOutputStream, "切割");
        if (outputStream != null) {
            outputStream.close();
        }
        inputStream.close();
        fileOutputStream.close();
    }


    /**
     * 根据配置文件读取文件路径
     *
     * @return 文件路劲
     */
    private static String getFileLocationByProperties() {
        InputStream resourceAsStream = null;
        try {
            Properties properties = new Properties();
            resourceAsStream = SpliteAndContractFile.class.getClassLoader().getResourceAsStream("conf/fileAbsoluteLocation.properties");
            properties.load(resourceAsStream);
            return properties.getProperty("fileLocation");
        } catch (IOException e) {
            throw new RuntimeException("IO异常");
        } finally {
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }

}
