# 文件切割与合并

## 使用方法
1. git clone https://github.com/wqh0109663/split-merge-file.git
2. 修改conf/filePath.properties ,指定一个文件
3. 运行splitFile和mergeFile
## 使用idea-find-bugs找出的一个bug
### 待我细细说来
> 先贴上代码
```java
/**
 * 将数组排序,可以使用lambda表达式简化,待会写
 */
//TODO
Arrays.sort(partFiles, new Comparator<File>() {
    @Override
    public int compare(File o1, File o2) {
        String substring = o1.getName().substring(0, o1.getName().indexOf("."));
        String substring1 = o2.getName().substring(0, o2.getName().indexOf("."));
        int i = Integer.parseInt(substring);
        int i1 = Integer.parseInt(substring1);
        int index = i > i1 ? 1 : -1;
        return index;
    }
});
```
我细细一想，好像是真的有问题.
* 有这种情况
假如我的文件大小只有4M,那么只会有一份文件别切割,也就相当于是文件的拷贝了,所以只会有一个文件.问题就在这.
    ```java
    if (partFiles != null && partFiles.length != Integer.parseInt(count)) {
                throw new RuntimeException("配置文件数量不正确");
            }
    ```
尽管我上面做了空指针的判断，但是如果只有一个文件，那么这个sort函数，必将报错
### 解决办法
在文件切割开头做判断，假如文件大小是5M以下，直接将此时的切割大小改为1M，或者直接完成一份文件的合并就可以了.

