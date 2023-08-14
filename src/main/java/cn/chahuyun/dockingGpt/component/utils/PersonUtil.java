package cn.chahuyun.dockingGpt.component.utils;

import cn.chahuyun.authorize.utils.Log;
import cn.chahuyun.dockingGpt.DockingGPT;
import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 获取人设
 *
 * @author Moyuyanli
 * @Date 2023/8/12 21:21
 */
public class PersonUtil {

    private static final String DATA_PATH = DockingGPT.INSTANCE.getDataFolderPath().toString();

    private PersonUtil() {
    }

    public static void init() {
        try (InputStream inputStream = DockingGPT.INSTANCE.getResourceAsStream("person.txt")) {
            File file = FileUtil.newFile(DATA_PATH + "/person.txt");
            FileUtil.writeFromStream(inputStream, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过资源路径获取设定文件
     *
     * @param path 路径
     * @return java.lang.String
     * @author Moyuyanli
     * @date 2023/8/12 22:51
     */
    public static String getPerson(String path) {
        path = DATA_PATH +"\\"+ path;
        String resource = FileUtil.readUtf8String(path);
        if (resource == null || resource.isEmpty()) {
            Log.error("获取ai设定文件出错!");
            return null;
        }
        return resource;
    }




}
