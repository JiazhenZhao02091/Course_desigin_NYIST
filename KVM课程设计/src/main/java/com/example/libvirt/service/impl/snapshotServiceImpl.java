package com.example.libvirt.service.impl;

import com.example.libvirt.Utils.GetInfoUtils;
import com.example.libvirt.pojo.Snapshot;
import com.example.libvirt.service.SnapshotService;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JiazhenZhao
 * 2023/5/30
 * 类说明：
 */
@Service
@Log
public class snapshotServiceImpl implements SnapshotService {
    @Override
    @SneakyThrows
    public List<Snapshot> getSnapshotListByName(String name) throws IOException {
        String cmd = "virsh snapshot" + "-list " + name;
        Process process = Runtime.getRuntime().exec(cmd);
        LineNumberReader line = new LineNumberReader(new InputStreamReader(process.getInputStream()));
        ArrayList<Snapshot> snapshots = new ArrayList<>();
        String str;
        int linCount = 0;
        int snapshotNum = GetInfoUtils.getDomainByName(name).snapshotNum(); // 2
        while ((str = line.readLine()) != null && snapshotNum > 0) {
            linCount++;
            if (linCount <= 2) continue;  // -2 line
            else {
                snapshotNum--;
                String[] lineStr = str.split("   ");
                snapshots.add(Snapshot.builder().name(lineStr[0]).creationTime(lineStr[1]).state(lineStr[2]).build());
            }
        }
        return snapshots;
    }
    /**
     * 创建快照
     */
    @SneakyThrows
    @Override
    public void createSnapshot(String name, String snapshotName) {
        String cmd = "";
        cmd = "virsh snapshot-create-as " + name + " " + snapshotName;
        Runtime.getRuntime().exec(cmd);
        log.info("虚拟机" + name + "成功创建快照" + snapshotName);
    }

    /**
     * 删除快照
     */
    @SneakyThrows
    @Override
    public void deleteSnapshot(String name, String snapshotName) {
        String cmd = "";
        cmd = "virsh snapshot-delete " + name + " " + snapshotName;
        Runtime.getRuntime().exec(cmd);
        log.info("虚拟机" + name + "成功删除快照" + snapshotName);
    }

    /**
     * 启动快照
     */
    @SneakyThrows
    @Override
    public void revertSnapshot(String name, String snapshotName) {
        String cmd = "";
        cmd = "virsh snapshot-revert " + name + " " + snapshotName;
        Runtime.getRuntime().exec(cmd);
        log.info("虚拟机" + name + "成功切换快照" + snapshotName);
    }
}
