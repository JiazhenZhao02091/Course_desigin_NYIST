package com.example.libvirt.service.impl;

import com.example.libvirt.Utils.LibvirtUtils;
import com.example.libvirt.pojo.Storagepool;
import com.example.libvirt.service.StoragePoolService;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.libvirt.StoragePool;
import org.libvirt.StoragePoolInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author JiazhenZhao
 * 2023/5/31
 * 类说明：
 */
@Service
@Log
public class storagePoolServiceImpl implements StoragePoolService {
    @Override
    @SneakyThrows
    public List<Storagepool> getStoragePoolList() {
        String[] pools = LibvirtUtils.getConnection().listStoragePools();
        String[] definedPools = LibvirtUtils.getConnection().listDefinedStoragePools();
        log.info("pools" + Arrays.toString(pools) + "definedPools" + Arrays.toString(definedPools));
        List<Storagepool> storagePoolList = new ArrayList<>();
        for (String pool : pools) storagePoolList.add(getStoragePool(pool));
        for (String pool : definedPools) storagePoolList.add(getStoragePool(pool));
        return storagePoolList;
    }

    @Override
    @SneakyThrows
    public Storagepool getStoragePool(String name) {
        StoragePool storagePool = LibvirtUtils.getConnection().storagePoolLookupByName(name);
        StoragePoolInfo info = storagePool.getInfo();
        return Storagepool.builder()
                .name(name)     // 名称
                .type("qcow2")  // 类型
                .capacity((int) (info.capacity / 1024.00 / 1024.00 / 1024.00))     // GB 容量
                .available((int) (info.available / 1024.00 / 1024.00 / 1024.00))   // GB 可用容量
                .allocation((int) (info.allocation / 1024.00 / 1024.00 / 1024.00)) // GB 已用容量
                .usage(((int) ((info.allocation / 1024.00 / 1024.00 / 1024.00) / (info.capacity / 1024.00 / 1024.00 / 1024.00) * 100)) + "%") // 使用率(%)
                .state(info.state.toString())                // 状态
                .xml(storagePool.getXMLDesc(0))        // 描述xml
                .build();
    }

    @Override
    @SneakyThrows
    public void deleteStoragePool(String name) {
        StoragePool storagePool = LibvirtUtils.getConnection().storagePoolLookupByName(name);
        for (String pool : LibvirtUtils.getConnection().listStoragePools())
            if (pool.equals(name)) storagePool.destroy();
        for (String pool : LibvirtUtils.getConnection().listDefinedStoragePools())
            if (pool.equals(name)) storagePool.undefine();
    }

    @Override
    @SneakyThrows
    public boolean createStoragePool(String name, String url) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<pool type=\"dir\">\n" +
                "    <name>" + name + "</name> \n" +       // 名称必须唯一
                "    <source>\n" +
                "    </source>\n" +
                "    <target>\n" +
                "        <path>" + url + "</path> \n" +                     // StoragePool 在宿主机的路径
                "        <permissions> \n" +                                // 权限
                "            <mode>0711</mode>\n" +
                "            <owner>0</owner>\n" +
                "            <group>0</group>\n" +
                "        </permissions>\n" +
                "    </target>\n" +
                "</pool>";
        return LibvirtUtils.getConnection().storagePoolCreateXML(xml, 0) == null ? false : true;
    }
}
