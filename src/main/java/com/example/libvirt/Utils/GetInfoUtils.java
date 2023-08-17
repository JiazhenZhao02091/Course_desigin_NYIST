package com.example.libvirt.Utils;

import com.example.libvirt.pojo.Host;
import com.example.libvirt.pojo.LibvirtConnect;
import com.example.libvirt.pojo.Virtual;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.libvirt.Domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JiazhenZhao
 * 2023/5/30
 * 类说明：
 */
@Log
public class GetInfoUtils {
    /**
     * 获取 libvirt 链接信息
     */
    public static LibvirtConnect getLibvirtConnectInformation() {
        return LibvirtUtils.getConnectionIo();
    }

    /**
     * getDomainById
     */
    @SneakyThrows
    public static Domain getDomainById(int id) {
        return LibvirtUtils.getConnection().domainLookupByID(id);
    }

    /**
     * getDomainByName
     */
    @SneakyThrows
    public static Domain getDomainByName(String name) {
        return LibvirtUtils.getConnection().domainLookupByName(name);
    }

    /**
     * getVirtualById
     */
    @SneakyThrows
    public static Virtual getVirtualById(int id) {
//        String cmd =
        Domain domain = getDomainById(id);
        String name =domain.getName();
        String netInterface = GetInfoUtils.getNetInterface(name);
        String netState = GetInfoUtils.getNetState(name,netInterface);
        String netType = GetInfoUtils.getNetType(name);
        return Virtual.builder()
                .id(domain.getID())
                .name(domain.getName())
                .state(domain.getInfo().state.toString())
                .netState(netState)
                .netInterface(netInterface)
                .netType(netType)
                .build();
    }

    /**
     * getVirtualByName
     */
    @SneakyThrows
    public static Virtual getVirtualByName(String name) {
        Domain domain = getDomainByName(name);
        String netInterface = GetInfoUtils.getNetInterface(name);
        String netState = GetInfoUtils.getNetState(name,netInterface);
        String netType = GetInfoUtils.getNetType(name);
        return Virtual.builder()
                .id(domain.getID())
                .name(domain.getName())
                .state(domain.getInfo().state.toString())
                .netState(netState)
                .netInterface(netInterface)
                .netType(netType)
                .build();
    }
    /**
     * huoqu Virtual List
     * @return
     */
    @SneakyThrows
    public static List<Virtual> getVirtualList() {
        ArrayList<Virtual> virtualList = new ArrayList<>();
        // live
        int[] ids = LibvirtUtils.getConnection().listDomains();
        for (int id : ids) virtualList.add(GetInfoUtils.getVirtualById(id));
        // down
        String[] names = LibvirtUtils.getConnection().listDefinedDomains();
        for (String name : names) virtualList.add(GetInfoUtils.getVirtualByName(name));
        return virtualList;
    }

    /**
     * Get host info
     * @return
     */
    @SneakyThrows
    public static Host getHostInfo(){
        return SigarUtils.getHostInfo();
    }

    /**
     * 获取网络状态和网络Interface
     */
    @SneakyThrows
    public static String getNetInterface(String name){
        String ans = "";
        String line;
        String cmd = "virsh domiflist " + name ;
        Process process = Runtime.getRuntime().exec(cmd);
        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        int flag = 0;
        while ((line = reader.readLine()) != null) {
            flag ++;
            if(flag == 3)
                ans = line;
        }
        String[] columns = ans.split("\\s+");

        return columns[0]; // interface
        /**
         * Interface  Type       Source     Model       MAC
         * -------------------------------------------------------
         * vnet0      network    default    virtio      52:54:00:e4:5f:ad
         */
    }
    @SneakyThrows
    public static String getNetState(String name,String netInterface){
        String ans = "";
        String cmd = "virsh domif-getlink " + name + " "+ netInterface + "\n";

        Process process = Runtime.getRuntime().exec(cmd);
        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.length() < 6){
                ans = "down";
                return ans;
            }
            ans=line.substring(6);
        }
        return ans;
    }

    /**
     * Get net Type
     */
    @SneakyThrows
    public static String getNetType(String name){
        String ans = "";
        String line;
        String cmd = "virsh domiflist " + name ;
        Process process = Runtime.getRuntime().exec(cmd);
        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        int flag = 0;
        while ((line = reader.readLine()) != null) {
            flag ++;
            if(flag == 3)
                ans = line;
        }
        String[] columns = ans.split("\\s+");

        return columns[1]; // interface
        /**
         * Interface  Type       Source     Model       MAC
         * -------------------------------------------------------
         * vnet0      network    default    virtio      52:54:00:e4:5f:ad
         */
    }
}
