package cn.chahuyun.dockingGpt.component.utils;

import cn.chahuyun.authorize.utils.Log;
import cn.chahuyun.dockingGpt.DockingGPT;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;

/**
 * 获取人设
 *
 * @author Moyuyanli
 * @Date 2023/8/12 21:21
 */
public class PersonUtil {



    private PersonUtil() {
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
        path = "/data/cn.chahuyun.DockingGPt/" + path;
        String resource = DockingGPT.INSTANCE.getResource(path);
        if (resource == null || resource.isEmpty()) {
            Log.error("获取ai设定文件出错!");
            return null;
        }
        return resource;
    }




}
