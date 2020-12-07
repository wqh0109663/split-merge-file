package main;

import constant.BufferSizeConstant;
import utils.ConfUtil;

import java.io.*;
import java.util.*;

/**
 * 文件合并
 *
 * @author wqh
 * @date 18-11-6
 */
public class MergeFile {
    public static void main(String[] args) throws IOException {

        String location = ConfUtil.getFileLocationByProperties();

        File locationFile = new File(location);
        File file = new File(locationFile.getParent(), File.separator + "part");
        mergeFile(file);
    }
    @SuppressWarnings("all")
    private static void mergeFile(File file) throws IOException {
        /*过滤出文件结尾为ini的文件数组*/
        File[] files = file.listFiles((f, str) -> str.endsWith(".ini"));
        if (files == null || files.length != 1) throw new RuntimeException("【配置文件数量不正确】");
        File iniFile = files[0];
        FileInputStream iniInputStream = new FileInputStream(iniFile);
        Properties properties = new Properties();
        properties.load(iniInputStream);
        String count = properties.getProperty("count");
        String fileName = properties.getProperty("fileName");
        /* 使用lambda表达式实现FilenameFilter*/
        File[] partFiles = file.listFiles((f, str) -> str.endsWith(".part"));
        if (partFiles == null && partFiles.length!=0) throw new RuntimeException("【碎片文件不存在！】");

        if (partFiles.length != Integer.parseInt(count)) {
            throw new RuntimeException("【碎片文件数量不正确】");
        } else if (partFiles.length == 1) {
            /*
             *如果文件比较小，只有一个碎片文件
             */
            FileOutputStream out = new FileOutputStream(new File(file, File.separator + fileName));
            FileInputStream in = new FileInputStream(partFiles[0]);
            int len;
            byte[] bytes = new byte[1024];
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.close();
            in.close();
            iniInputStream.close();
            return;
        }
        output(file, iniInputStream, fileName, partFiles);
    }

    private static void output(File file, FileInputStream iniInputStream, String fileName, File[] partFiles) throws IOException {
        /*
         * 将数组排序
         */
        Arrays.sort(partFiles, Comparator.comparingInt(o -> Integer.parseInt(o.getName().substring(0, o.getName().indexOf(".")))));
        List<FileInputStream> list = new ArrayList<>();
        for (File partFile : partFiles) {
            list.add(new FileInputStream(partFile));
        }
        Enumeration<FileInputStream> enumeration = Collections.enumeration(list);
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
        FileOutputStream outputStream = new FileOutputStream(new File(file, File.separator + fileName));
        int len;
        byte[] bytes = new byte[BufferSizeConstant.BUFFER_SIZE];
        while ((len = sequenceInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }
        outputStream.close();
        iniInputStream.close();
        sequenceInputStream.close();
    }


}
