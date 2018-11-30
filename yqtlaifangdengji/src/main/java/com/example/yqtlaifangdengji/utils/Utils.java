package com.example.yqtlaifangdengji.utils;

import android.util.Log;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Utils {

    /**
     * Get Ip address 自动获取IP地址
     *   其中的ipType就是需要获取的网络ip地址类型，我们可以传入eth1，eth0,wlan0,等
     * @throws
     */
    public static String getIpAddress(String ipType) throws SocketException {
        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();

                if (ni.getName().equals(ipType)) {


                    Enumeration<InetAddress> ias = ni.getInetAddresses();
                    while (ias.hasMoreElements()) {

                        ia = ias.nextElement();
                        if (ia instanceof Inet6Address) {
                            continue;// skip ipv6
                        }
                        String ip = ia.getHostAddress();

                        // 过滤掉127段的ip地址
                        if (!"127.0.0.1".equals(ip)) {
                            hostIp = ia.getHostAddress();
                            break;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Log.d("vivi", "get the IpAddress--> " + hostIp + "");
        return hostIp;
    }

}
