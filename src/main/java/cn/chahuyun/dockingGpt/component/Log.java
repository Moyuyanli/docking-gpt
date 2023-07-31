package cn.chahuyun.dockingGpt.component;

import cn.chahuyun.dockingGpt.DockingGPT;
import net.mamoe.mirai.utils.MiraiLogger;

/**
 * 日志操作
 *
 * @author Moyuyanli
 * @Date 2023/7/29 13:45
 */
public class Log {

    private static final MiraiLogger LOGGER = DockingGPT.INSTANCE.getLogger();



    public static void info(String msg) {
            LOGGER.info(msg);
    }

    /**
     * 支持 String format 的日志打印
     *
     * @param msg 消息
     * @param params 参数
     * @author Moyuyanli
     * @date 2023/7/30 0:55
     */
    public static void info(String msg, Object... params) {
        LOGGER.info(String.format(msg, params));
    }


    public static void warning(String msg) {
        LOGGER.warning(msg);
    }

    /**
     * 支持 String format 的日志打印
     *
     * @param msg 消息
     * @param params 参数
     * @author Moyuyanli
     * @date 2023/7/30 0:55
     */
    public static void warning(String msg, Object... params) {
        LOGGER.warning(String.format(msg, params));
    }

    public static void error(String msg) {
        LOGGER.error(msg);
    }

    /**
     * 支持 String format 的日志打印
     *
     * @param msg 消息
     * @param params 参数
     * @author Moyuyanli
     * @date 2023/7/30 0:55
     */
    public static void error(String msg, Object... params) {
        LOGGER.error(String.format(msg, params));
    }

    public static void debug(String msg) {
        LOGGER.debug(msg);
    }

    /**
     * 支持 String format 的日志打印
     *
     * @param msg 消息
     * @param params 参数
     * @author Moyuyanli
     * @date 2023/7/30 0:55
     */
    public static void debug(String msg, Object... params) {
        LOGGER.debug(String.format(msg, params));
    }




}
