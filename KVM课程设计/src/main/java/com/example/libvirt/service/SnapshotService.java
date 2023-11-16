package com.example.libvirt.service;

import com.example.libvirt.pojo.Snapshot;

import java.io.IOException;
import java.util.List;

/**
 * @author JiazhenZhao
 * 2023/5/30
 * 类说明：
 */
public interface SnapshotService {
    public List<Snapshot> getSnapshotListByName(String name) throws IOException;
    public void createSnapshot(String name, String snapshotName);
    public void deleteSnapshot(String name, String snapshotName);
    public void revertSnapshot(String name, String snapshotName);
}
