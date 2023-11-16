package com.example.libvirt.service.impl;

import com.example.libvirt.Utils.GetInfoUtils;
import com.example.libvirt.Utils.LibvirtUtils;
import com.example.libvirt.service.VirtualService;
import lombok.extern.java.Log;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author JiazhenZhao
 * 2023/5/30
 * 类说明：
 */
@Service
@Log
public class virtualServiceImpl implements VirtualService {
    @Override
    public void addDomainByName(String name) throws LibvirtException {
        String xml = "<domain type='kvm'>\n" +
                "  <name>"+ name + "</name>\n" +
                "  <uuid>"+ UUID.randomUUID() +"</uuid>\n" +
                "  <memory unit='KiB'>1048756</memory>\n" +
                "  <currentMemory unit='KiB'>1048756</currentMemory>\n" +
                "  <vcpu placement='static'>1</vcpu>\n" +
                "  <os>\n" +
                "    <type arch='x86_64' machine='pc-i440fx-rhel7.0.0'>hvm</type>\n" +
                "    <boot dev='hd'/>\n" +
                "  </os>\n" +
                "  <features>\n" +
                "    <acpi/>\n" +
                "    <apic/>\n" +
                "  </features>\n" +
                "  <cpu mode='custom' match='exact' check='partial'>\n" +
                "    <model fallback='allow'>Nehalem-IBRS</model>\n" +
                "    <feature policy='require' name='md-clear'/>\n" +
                "    <feature policy='require' name='spec-ctrl'/>\n" +
                "    <feature policy='require' name='ssbd'/>\n" +
                "  </cpu>\n" +
                "  <clock offset='utc'>\n" +
                "    <timer name='rtc' tickpolicy='catchup'/>\n" +
                "    <timer name='pit' tickpolicy='delay'/>\n" +
                "    <timer name='hpet' present='no'/>\n" +
                "  </clock>\n" +
                "  <on_poweroff>destroy</on_poweroff>\n" +
                "  <on_reboot>restart</on_reboot>\n" +
                "  <on_crash>destroy</on_crash>\n" +
                "  <pm>\n" +
                "    <suspend-to-mem enabled='no'/>\n" +
                "    <suspend-to-disk enabled='no'/>\n" +
                "  </pm>\n" +
                "  <devices>\n" +
                "    <emulator>/usr/libexec/qemu-kvm</emulator>\n" +
                "    <disk type='file' device='disk'>\n" +
                "      <driver name='qemu' type='qcow2'/>\n" +
                "      <source file='/var/lib/libvirt/images/" + name + ".img'/>\n" +
                "     bootstrap.min <target dev='vda' bus='virtio'/>\n" +
                "      <address type='pci' domain='0x0000' bus='0x00' slot='0x07' function='0x0'/>\n" +
                "    </disk>\n" +
                "    <controller type='usb' index='0' model='ich9-ehci1'>\n" +
                "      <address type='pci' domain='0x0000' bus='0x00' slot='0x05' function='0x7'/>\n" +
                "    </controller>\n" +
                "    <controller type='usb' index='0' model='ich9-uhci1'>\n" +
                "      <master startport='0'/>\n" +
                "      <address type='pci' domain='0x0000' bus='0x00' slot='0x05' function='0x0' multifunction='on'/>\n" +
                "    </controller>\n" +
                "    <controller type='usb' index='0' model='ich9-uhci2'>\n" +
                "      <master startport='2'/>\n" +
                "      <address type='pci' domain='0x0000' bus='0x00' slot='0x05' function='0x1'/>\n" +
                "    </controller>\n" +
                "    <controller type='usb' index='0' model='ich9-uhci3'>\n" +
                "      <master startport='4'/>\n" +
                "      <address type='pci' domain='0x0000' bus='0x00' slot='0x05' function='0x2'/>\n" +
                "    </controller>\n" +
                "    <controller type='pci' index='0' model='pci-root'/>\n" +
                "    <controller type='virtio-serial' index='0'>\n" +
                "      <address type='pci' domain='0x0000' bus='0x00' slot='0x06' function='0x0'/>\n" +
                "    </controller>\n" +
                "    <interface type='network'>\n" +
                "      <mac address='52:54:00:e4:5f:ad'/>\n" +
                "      <source network='default'/>\n" +
                "      <model type='virtio'/>\n" +
                "      <address type='pci' domain='0x0000' bus='0x00' slot='0x03' function='0x0'/>\n" +
                "    </interface>\n" +
                "    <serial type='pty'>\n" +
                "      <target type='isa-serial' port='0'>\n" +
                "        <model name='isa-serial'/>\n" +
                "      </target>\n" +
                "    </serial>\n" +
                "    <console type='pty'>\n" +
                "      <target type='serial' port='0'/>\n" +
                "    </console>\n" +
                "    <channel type='spicevmc'>\n" +
                "      <target type='virtio' name='com.redhat.spice.0'/>\n" +
                "      <address type='virtio-serial' controller='0' bus='0' port='1'/>\n" +
                "    </channel>\n" +
                "    <input type='tablet' bus='usb'>\n" +
                "      <address type='usb' bus='0' port='1'/>\n" +
                "    </input>\n" +
                "    <input type='mouse' bus='ps2'/>\n" +
                "    <input type='keyboard' bus='ps2'/>\n" +
                "    <graphics type='spice' autoport='yes'>\n" +
                "      <listen type='address'/>\n" +
                "      <image compression='off'/>\n" +
                "    </graphics>\n" +
                "    <sound model='ich6'>\n" +
                "      <address type='pci' domain='0x0000' bus='0x00' slot='0x04' function='0x0'/>\n" +
                "    </sound>\n" +
                "    <video>\n" +
                "      <model type='qxl' ram='65536' vram='65536' vgamem='16384' heads='1' primary='yes'/>\n" +
                "      <address type='pci' domain='0x0000' bus='0x00' slot='0x02' function='0x0'/>\n" +
                "    </video>\n" +
                "    <redirdev bus='usb' type='spicevmc'>\n" +
                "      <address type='usb' bus='0' port='2'/>\n" +
                "    </redirdev>\n" +
                "    <redirdev bus='usb' type='spicevmc'>\n" +
                "      <address type='usb' bus='0' port='3'/>\n" +
                "    </redirdev>\n" +
                "    <memballoon model='virtio'>\n" +
                "      <address type='pci' domain='0x0000' bus='0x00' slot='0x08' function='0x0'/>\n" +
                "    </memballoon>\n" +
                "  </devices>\n" +
                "</domain>\n";
        LibvirtUtils.getConnection().domainDefineXML(xml);    // define ------> creat
        log.info(name + "虚拟机已创建！");
    }

    @Override
    public void deleteDomainByName(String name) throws LibvirtException {
        Domain domain = GetInfoUtils.getDomainByName(name);
        if (domain.isActive() == 1) domain.destroy();  // 强制关机
        domain.undefine();
        log.info(domain.getName() + "虚拟机已删除！");
    }
}
