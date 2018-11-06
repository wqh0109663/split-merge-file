package main;

import constant.BufferSizeConstant;
import utils.ConfUtil;

import java.io.*;
import java.util.Properties;

/**
 * 文件切割
 *
 * @author wqh
 * @date 18-11-5
 */
public class SplitFile {


    public static void main(String[] args) throws Exception {
        String location = ConfUtil.getFileLocationByProperties();

        File file = new File(location);
        split(file);


    }

    /**
     * 文件切割
     *
     * @param file 带切割的文件
     * @throws IOException IO异常
     */

    private static void split(File file) throws IOException {
        File partFile = new File(file.getParent() + File.separator + "part");
        if (!partFile.exists()) {
            boolean mkdirs = partFile.mkdirs();
            if (!mkdirs) {
                throw new RuntimeException("创建文件夹失败!!!");
            }
        }
        FileInputStream inputStream = new FileInputStream(file);
        FileOutputStream outputStream = null;
        int len;
        int count = 1;
        byte[] bytes = new byte[BufferSizeConstant.BUFFER_SIZE];
        while ((len = inputStream.read(bytes)) != -1) {
            outputStream = new FileOutputStream(new File(partFile, count++ + ".part"));
            outputStream.write(bytes, 0, len);
        }
        Properties properties = new Properties();
        properties.setProperty("count", String.valueOf(--count));
        properties.setProperty("fileName", file.getName());
        FileOutputStream fileOutputStream = new FileOutputStream(new File(partFile, "split.ini"));
        properties.store(fileOutputStream, "切割");
        if (outputStream != null) {
            outputStream.close();
        }
        inputStream.close();
        fileOutputStream.close();
    }


}
