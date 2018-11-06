package utils;

import main.SplitFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 获取配置信息
 *
 * @author wqh
 * @date 18-11-6
 */
public class ConfUtil {
    public static String getFileLocationByProperties() {
        InputStream resourceAsStream = null;
        try {
            Properties properties = new Properties();
            resourceAsStream = SplitFile.class.getClassLoader().getResourceAsStream("conf/filePath.properties");
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


