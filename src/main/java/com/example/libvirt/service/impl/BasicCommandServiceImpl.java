package com.example.libvirt.service.impl;

import com.example.libvirt.Utils.GetInfoUtils;
import com.example.libvirt.service.BasicCommandService;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.libvirt.Domain;
import org.springframework.stereotype.Service;

import javax.swing.*;

/**
 * @author JiazhenZhao
 * 2023/5/30
 * 类说明：
 */
@Service
@Log
public class BasicCommandServiceImpl implements BasicCommandService {


    /**
     * 暂停/挂起 虚拟机
     */
    @SneakyThrows
    @Override
    public void suspendedDomainByName(String name) {
        Domain domain = GetInfoUtils.getDomainByName(name);
        if (domain.isActive() == 1) {
            domain.suspend();
            log.info(domain.getName() + "虚拟机已挂起！");
        } else log.info("虚拟机未打开");
    }


    /**
     * 还原 暂停/挂起 虚拟机
     */
    @SneakyThrows
    @Override
    public void resumeDomainByName(String name) {
        Domain domain = GetInfoUtils.getDomainByName(name);
        if (domain.isActive() == 1) {
            domain.resume();
            log.info(domain.getName() + "虚拟机已唤醒！");
        } else log.info("虚拟机未打开");
    }

    /*
        保存虚拟机：
     */
    @SneakyThrows
    @Override
    public void saveDomainByName(String name) {
        Domain domain = GetInfoUtils.getDomainByName(name);
        JFileChooser jf = new JFileChooser();
        jf.setFileSelectionMode(JFileChooser.SAVE_DIALOG | JFileChooser.DIRECTORIES_ONLY);
        jf.showDialog(null, null);
        String f = jf.getSelectedFile().getAbsolutePath() + "/save.img";
        if (domain.isActive() == 1) {
            domain.save(f);
            log.info(domain.getName() + "虚拟机状态已保存！" + "save: " + f);
        } else log.info("虚拟机未打开");
    }
    /**
     * 恢复 虚拟机 --->img文件
     */
    @SneakyThrows
    @Override
    public void restoreDomainByName(String name) {
        Domain domain = GetInfoUtils.getDomainByName(name);
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        if (chooser.getSelectedFile() != null) {
            String path = chooser.getSelectedFile().getPath();
            if (domain.isActive() == 0) {
                domain.getConnect().restore(path);
                log.info(domain.getName() + "虚拟机状态已恢复！！" + "path: " + path);
            } else log.info("虚拟机未关闭");
        }
    }
    /**
     * 启动 虚拟机
     */
    @SneakyThrows
    @Override
    public void initiateDomainByName(String name) {
        Domain domain = GetInfoUtils.getDomainByName(name);
        if (domain.isActive() == 0) {
            domain.create();
            log.info(domain.getName() + "虚拟机已启动！");
        } else log.info("虚拟机已经打开过！");
    }

    /**
     * 关闭 虚拟机
     */
    @SneakyThrows
    @Override
    public void shutdownDomainByName(String name) {
        Domain domain = GetInfoUtils.getDomainByName(name);
        if (domain.isActive() == 1) {
            domain.shutdown();
            log.info(domain.getName() + "虚拟机已正常关机！");
        } else log.info("虚拟机未打开");

    }
    /**
     * 强制关闭 虚拟机
     */
    @SneakyThrows
    @Override
    public void shutdownMustDomainByName(String name) {
        Domain domain = GetInfoUtils.getDomainByName(name);
        if (domain.isActive() == 1) {
            domain.destroy();
            log.info(domain.getName() + "虚拟机已强制关机！");
        } else log.info("虚拟机未打开");
    }

    /**
     * 重启 虚拟机
     */
    @SneakyThrows
    @Override
    public void rebootDomainByName(String name) {
        Domain domain = GetInfoUtils.getDomainByName(name);
        if (domain.isActive() == 1) {
            domain.reboot(0);
            log.info(domain.getName() + "虚拟机状态已重启！");
        } else log.info("虚拟机未打开");
    }



}
