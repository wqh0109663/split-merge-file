package main;

import constant.BufferSizeConstant;

import java.io.*;
import java.util.*;

/**
 * @author wqh
 * @date 18-11-6
 */
@SuppressWarnings("all")
public class MergeFile {
    public static void main(String[] args) throws IOException {

        final File file = new File("/home/wqh/github/splite/part");
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


        File[] partFiles = file.listFiles(new SuffixFilter(".part"));
        if (partFiles != null && partFiles.length != Integer.parseInt(count)) {
            throw new RuntimeException("配置文件数量不正确");
        }
        Arrays.sort(partFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                String substring = o1.getName().substring(0, o1.getName().indexOf("."));
                String substring1 = o2.getName().substring(0, o2.getName().indexOf("."));
                int i = Integer.parseInt(substring);
                int i1 = Integer.parseInt(substring1);
                int index =  i > i1 ? 1 : -1;
                return index;
            }
        });
        List<FileInputStream> list = new ArrayList<FileInputStream>();
        for (int i = 0; i < partFiles.length; i++) {
            list.add(new FileInputStream(partFiles[i]));
        }
        Enumeration<FileInputStream> enumeration = Collections.enumeration(list);
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);
        FileOutputStream outputStream = new FileOutputStream(new File(file, "/a.mp4"));
        int len;
        byte[] bytes = new byte[BufferSizeConstant.SIZE];
        while ((len = sequenceInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }
        outputStream.close();
        sequenceInputStream.close();
        iniInputStream.close();
    }


    static class SuffixFilter implements FilenameFilter {


        public String suffixName;

        public SuffixFilter(String suffixName) {
            this.suffixName = suffixName;
        }

        /**
         * Tests if a specified file should be included in a file list.
         *
         * @param dir  the directory in which the file was found.
         * @param name the name of the file.
         * @return <code>true</code> if and only if the name should be
         * included in the file list; <code>false</code> otherwise.
         */

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(suffixName);
        }
    }


}
