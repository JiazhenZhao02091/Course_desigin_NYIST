package com.example.libvirt.service;

import org.libvirt.LibvirtException;

import java.io.IOException;

/**
 * @author JiazhenZhao
 * 2023/5/30
 * 类说明：
 */
public interface NetworkService {
    public void closeNetWork() throws LibvirtException;
    public void openNetWork() throws LibvirtException;
    public String getNetState() throws LibvirtException;

    public void closeNetWorkByName(String name) throws IOException;
    public void openNetWorkByName(String name) throws IOException;
    public void changeNetKindToBridge(String name);
    public void changeNetKindToNat(String name);
}
