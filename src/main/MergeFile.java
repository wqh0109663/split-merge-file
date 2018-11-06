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
@SuppressWarnings("all")
public class MergeFile {
    public static void main(String[] args) throws IOException {

        String location = ConfUtil.getFileLocationByProperties();

        File locationFile = new File(location);
        File file = new File(locationFile.getParent(), File.separator + "part");
        mergeFile(file);


    }

    private static void mergeFile(File file) throws IOException {
        File[] files = file.listFiles(new SuffixFilter(".ini"));

        if (files != null && files.length != 1) {
            throw new RuntimeException("【配置文件数量不正确");
        }
        assert files != null;
        File iniFile = files[0];
        FileInputStream iniInputStream = new FileInputStream(iniFile);
        Properties properties = new Properties();
        properties.load(iniInputStream);
        String count = properties.getProperty("count");
        String fileName = properties.getProperty("fileName");


        File[] partFiles = file.listFiles(new SuffixFilter(".part"));
        assert partFiles != null;
        if (partFiles.length != Integer.parseInt(count)) {
            throw new RuntimeException("配置文件数量不正确");
        } else if (partFiles.length == 1) {
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
        /**
         * 将数组排序,可以使用lambda表达式简化
         */
        Arrays.sort(partFiles, (o1, o2) -> Integer.parseInt(o1.getName().substring(0, o1.getName().indexOf("."))) >
                Integer.parseInt(o2.getName().substring(0, o2.getName().indexOf("."))) ? 1 : -1);

        List<FileInputStream> list = new ArrayList<FileInputStream>();
        for (int i = 0; i < partFiles.length; i++) {
            list.add(new FileInputStream(partFiles[i]));
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


    static class SuffixFilter implements FilenameFilter {
        public String suffixName;

        public SuffixFilter(String suffixName) {
            this.suffixName = suffixName;
        }

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(suffixName);
        }
    }


}
