package com.example.libvirt.service;

import org.libvirt.LibvirtException;

/**
 * @author JiazhenZhao
 * 2023/5/30
 * 类说明：
 */
public interface VirtualService {
    public void addDomainByName(String name) throws LibvirtException;
    public void deleteDomainByName(String name) throws LibvirtException;

}
