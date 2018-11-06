package utils;

import main.SplitFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author wqh
 * @date 18-11-6
 */
public class ConfUtil {

    /**
     * 根据配置文件读取文件路径
     *
     * @return 文件路径
     */
    public static String getFileLocationByProperties(String propertiesPath) {
        InputStream resourceAsStream = null;
        try {
            Properties properties = new Properties();
            resourceAsStream = SplitFile.class.getClassLoader().getResourceAsStream(propertiesPath);
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
