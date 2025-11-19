package com.nest.diamond.common.util;

import lombok.SneakyThrows;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class HostNameUtils {

    /**
     * 获取本地主机的名称
     *
     * @return 主机名，如果发生错误则返回 null
     */
    @SneakyThrows
    public static String getHostName() {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostName();
    }

    /**
     * 从环境变量中获取主机名
     *
     * @return 主机名，如果环境变量未设置则返回 null
     */
    public static String getHostNameFromEnv() {
        String hostName = System.getenv("HOSTNAME");
        if (hostName != null) {
            return hostName;
        }
        // 处理 Windows 系统的环境变量
        hostName = System.getenv("COMPUTERNOAME");
        return hostName;
    }

    /**
     * 从 JVM 名称中提取主机名
     *
     * @return 主机名，如果无法提取则返回 null
     */
    public static String getHostNameFromJVM() {
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        String[] parts = jvmName.split("@");
        if (parts.length > 1) {
            return parts[1];
        }
        return null;
    }

    /**
     * 获取所有网络接口的主机信息
     *
     * @return 网络接口的信息字符串，如果发生错误则返回 null
     */
    public static String getNetworkInterfacesInfo() {
        StringBuilder info = new StringBuilder();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                info.append("Network Interface: ").append(networkInterface.getName()).append("\n");

                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    info.append("  IP Address: ").append(inetAddress.getHostAddress()).append("\n");
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
        return info.toString();
    }
}
