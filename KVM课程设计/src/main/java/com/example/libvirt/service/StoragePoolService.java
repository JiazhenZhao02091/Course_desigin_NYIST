package com.example.libvirt.service;

import com.example.libvirt.pojo.Storagepool;

import java.util.List;

/**
 * @author JiazhenZhao
 * 2023/5/31
 * 类说明：
 */
public interface StoragePoolService {
    public List<Storagepool> getStoragePoolList();
    public Storagepool getStoragePool(String name);
    public void deleteStoragePool(String name);
    public boolean createStoragePool(String name, String url);

}
