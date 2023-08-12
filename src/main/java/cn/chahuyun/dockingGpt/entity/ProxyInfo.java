package cn.chahuyun.dockingGpt.entity;

/**
 * 代理信息
 *
 * @author Moyuyanli
 * @Date 2023/8/12 23:06
 */
public class ProxyInfo {



    /**
     * 代理类型
     */
    private String proxyType;
    /**
     * 代理地址
     */
    private String url;
    /**
     * 代理端口
     */
    private String port;

    public ProxyInfo(String proxyType, String url, String port) {
        this.proxyType = proxyType;
        this.url = url;
        this.port = port;
    }

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
