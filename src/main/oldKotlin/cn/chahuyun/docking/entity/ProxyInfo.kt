package cn.chahuyun.docking.entity

/**
 * 代理信息
 *
 * @author Moyuyanli
 * @Date 2023/8/12 23:06
 */
data class ProxyInfo(
    /**
     * 代理类型
     */
    var proxyType: String,
    /**
     * 代理地址
     */
    var url: String,
    /**
     * 代理端口
     */
    var port: Int,
)
