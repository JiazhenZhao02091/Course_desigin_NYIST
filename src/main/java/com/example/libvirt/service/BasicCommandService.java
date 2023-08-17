package com.example.libvirt.service;

/**
 * @author JiazhenZhao
 * 2023/5/30
 * 类说明：
 */
public interface BasicCommandService {
    public void suspendedDomainByName(String name);
    public void resumeDomainByName(String name);
    public void saveDomainByName(String name);
    public void restoreDomainByName(String name);
    public void initiateDomainByName(String name);
    public void shutdownDomainByName(String name);
    public void shutdownMustDomainByName(String name);
    public void rebootDomainByName(String name);

}
