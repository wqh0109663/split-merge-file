# 文件切割与合并

## 为什么要写这个
原因很简单
1. 将文件切割之后，别人不知道里面是什么内容，比如百度网盘，如果涉及到侵权，或者内容是******，这样百度就不知道，从而保存*****
2. 二是过于依赖编辑器，通过写这个小工具，就会知道原始的javac的使用方式和shell或者bat批处理文件的方式

## 在编辑器中的使用方法
1. git clone https://github.com/wqh0109663/split-merge-file.git
2. 修改conf/filePath.properties ,指定一个文件
3. 运行splitFile和mergeFile

## 命令行中使用方式(主要针对黑窗口Linux)
```bash
vim out/conf/filePath.properties
# add you need to be splited file
cd out
java main.SplitFile
java main.MergeFile
```


