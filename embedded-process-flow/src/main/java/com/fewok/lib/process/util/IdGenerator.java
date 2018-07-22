package com.fewok.lib.process.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * 初始化唯一Id生成器
 *
 * @author notreami on 18/7/7.
 */
@Slf4j
public class IdGenerator {

    static {
        String localIp = getLocalIp();
        String[] localIpSegment = localIp.split("\\.");
        String[] dataCenters = localIpSegment[localIpSegment.length - 2].split("");
        String[] workers = localIpSegment[localIpSegment.length - 1].split("");
        int dataCenterId = Arrays.stream(dataCenters).map(Integer::parseInt).reduce(0, Math::addExact);
        int workerId = Arrays.stream(workers).map(Integer::parseInt).reduce(0, Math::addExact);
        IdWorker.getInstance(dataCenterId, workerId);
    }

    /**
     * 方法 getLocalIp
     * 获取本机ip地址
     *
     * @return 本机ip地址
     */
    private static String getLocalIp() {
        String localIp = "";
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) netInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (!ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                        localIp = ip.getHostAddress();
                        break;
                    }
                }
                if (!"".equals(localIp)) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("获取服务器IP地址失败!");
        }
        return localIp;
    }
}
