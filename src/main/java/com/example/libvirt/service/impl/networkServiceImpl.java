package com.example.libvirt.service.impl;

import com.example.libvirt.Utils.GetInfoUtils;
import com.example.libvirt.Utils.LibvirtUtils;
import com.example.libvirt.pojo.Virtual;
import com.example.libvirt.service.NetworkService;
import lombok.extern.java.Log;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;
import org.libvirt.Network;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * @author JiazhenZhao
 * 2023/5/30
 * 类说明：
 */
@Service
@Log
public class networkServiceImpl implements NetworkService {
    @Override
    public void closeNetWork() throws LibvirtException {
        Domain domain = GetInfoUtils.getDomainByName(GetInfoUtils.getVirtualList().get(0).getName());
        Network network = domain.getConnect().networkLookupByName("default");
        if (network.isActive() == 1) {
            network.destroy();
            log.info("网络" + network.getName() + "已经被关闭！");
        } else log.info("网络" + network.getName() + "已经处于关闭状态！");
    }

    @Override
    public void openNetWork() throws LibvirtException {
        Domain domain = GetInfoUtils.getDomainByName(GetInfoUtils.getVirtualList().get(0).getName());
        Network network = domain.getConnect().networkLookupByName("default");
        if (network.isActive() == 0) {
            network.create();
            log.info("网络" + network.getName() + "已经被打开！");
        } else log.info("网络" + network.getName() + "已经处于打开状态！");
    }

    @Override
    public String getNetState() throws LibvirtException {
        Domain domain = GetInfoUtils.getDomainByName(GetInfoUtils.getVirtualList().get(0).getName());
        if (domain.getConnect().networkLookupByName("default").isActive() == 1) return "on";
        else return "off";
    }

    @Override
    public void closeNetWorkByName(String name) throws IOException {
        Virtual virtual = GetInfoUtils.getVirtualByName(name);
        String cmd = "virsh domif-setlink " + virtual.getName() + " " +virtual.getNetInterface() + " down";
        // ExeComand
        Runtime.getRuntime().exec(cmd);
    }

    @Override
    public void openNetWorkByName(String name) throws IOException {
        Virtual virtual = GetInfoUtils.getVirtualByName(name);
        String cmd = "virsh domif-setlink " + virtual.getName() + " " +virtual.getNetInterface() + " up";
        // ExeComand
        Runtime.getRuntime().exec(cmd);
    }


    @Override
    public void changeNetKindToBridge(String name){
        try {
            // 加载 XML 文件
            String path = "/etc/libvirt/qemu/" + name + ".xml";
            File xmlFile = new File(path);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            // 查找要修改的元素或属性
            NodeList interfaceList = document.getElementsByTagName("interface");
            for (int i = 0; i < interfaceList.getLength(); i++) {
                Element interfaceElement = (Element) interfaceList.item(i);
                String type = interfaceElement.getAttribute("type");

                // 判断是否为需要修改的 <interface type='network'>
                if (type.equals("network")) {
                    // 修改 <interface> 的 type 属性为 'bridge'
                    interfaceElement.setAttribute("type", "bridge");

                    // 查找 <source> 元素并修改 network 属性为 'new_network'
                    NodeList sourceList = interfaceElement.getElementsByTagName("source");
                    for (int j = 0; j < sourceList.getLength(); j++) {
                        Element sourceElement = (Element) sourceList.item(j);
                        sourceElement.removeAttribute("network");
                        sourceElement.setAttribute("bridge", "br0");
                    }
                }
            }
            // 保存修改后的 XML 文件
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);
            //cp
            String cmd = "cp /etc/libvirt/qemu/" + name + ".xml" + " /etc/libvirt/qemu/" + name + "_copy";
            // add wait time
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
            //undefine
            cmd = "virsh undefine " + name;
            Process process1 = Runtime.getRuntime().exec(cmd);
            process1.waitFor();
            // mv
            cmd = "mv /etc/libvirt/qemu/" + name + "_copy" + " /etc/libvirt/qemu/" + name + ".xml";
            Process process2 = Runtime.getRuntime().exec(cmd);
            process2.waitFor();
            LibvirtUtils.cleaniXmls(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void changeNetKindToNat(String name){
        try {
//            // 加载 XML 文件
            String path = "/etc/libvirt/qemu/" + name + ".xml";
            File xmlFile = new File(path);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            // 查找要修改的元素或属性
            NodeList interfaceList = document.getElementsByTagName("interface");
            for (int i = 0; i < interfaceList.getLength(); i++) {
                Element interfaceElement = (Element) interfaceList.item(i);
                String type = interfaceElement.getAttribute("type");

                // 判断是否为需要修改的 <interface type='network'>
                if (type.equals("bridge")) {
                    // 修改 <interface> 的 type 属性为 'bridge'
                    interfaceElement.setAttribute("type", "network");

                    // 查找 <source> 元素并修改 network 属性为 'new_network'
                    NodeList sourceList = interfaceElement.getElementsByTagName("source");
                    for (int j = 0; j < sourceList.getLength(); j++) {
                        Element sourceElement = (Element) sourceList.item(j);
                        sourceElement.removeAttribute("bridge");
                        sourceElement.setAttribute("network", "default");
                    }
                }
            }

            // 保存修改后的 XML 文件
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);
            //cp
            String cmd = "cp /etc/libvirt/qemu/" + name + ".xml" + " /etc/libvirt/qemu/" + name + "_copy";
            // add wait time
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
            //undefine
            cmd = "virsh undefine " + name;
            Process process1 = Runtime.getRuntime().exec(cmd);
            process1.waitFor();
            // mv
            cmd = "mv /etc/libvirt/qemu/" + name + "_copy" + " /etc/libvirt/qemu/" + name + ".xml";
            Process process2 = Runtime.getRuntime().exec(cmd);
            process2.waitFor();
            LibvirtUtils.cleaniXmls(name);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    String cmd = "cp /etc/libvirt/qemu/" + name + ".xmls" + " /etc/libvirt/qemu/" + name + ".xml";
     */
}
