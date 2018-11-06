package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author wqh
 * @date 18-11-6
 */
public class IOCopyUtil {
    /**
     * 自定义缓冲区大小的拷贝工具类
     * @param source 输入流
     * @param sink 输出流
     * @param bufferSize 缓冲区大小
     * @return 拷贝比特数
     * @throws IOException IO异常
     */
    public static long copy(InputStream source, OutputStream sink,int bufferSize)
            throws IOException
    {
        long nread = 0L;
        byte[] buf = new byte[bufferSize];
        int n;
        while ((n = source.read(buf)) > 0) {
            sink.write(buf, 0, n);
            nread += n;
        }
        return nread;
    }


}
