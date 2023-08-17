package com.example.libvirt;
import com.example.libvirt.Utils.GetInfoUtils;
import com.example.libvirt.Utils.LibvirtUtils;
import com.example.libvirt.Utils.SigarUtils;
import com.example.libvirt.pojo.Host;
import com.example.libvirt.pojo.Virtual;
import com.example.libvirt.service.BasicCommandService;
import com.example.libvirt.service.NetworkService;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.junit.Test;
import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * @author JiazhenZhao
 * 2023/5/30
 * 类说明：
 */
@SpringBootTest
public class libvirtApplicationTest {

    @Autowired
    public NetworkService networkService;

    @Test
    public void connect() {

    }

    @Test
    @SneakyThrows
    public void cmd() {
        ProcessBuilder processBuilder = new ProcessBuilder("ls", "-l", "/root");
        //        ProcessBuilder processBuilder = new ProcessBuilder("touch","/root");
        // 启动进程
        Process process = processBuilder.start();

        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // 等待命令执行完成
        int exitCode = process.waitFor();

        System.out.println("命令执行完成，退出码: " + exitCode);
    }

    @Test
    @SneakyThrows
    public void mkinfo() {
        String filePath = "/etc/libvirt/qemu/" + "demo6.xml";
        String name = "demo6";
        String xml = "<domain type='kvm'>\n" +
                "  <name>" + name + "</name>\n" +
                "  <uuid>" + UUID.randomUUID() + "</uuid>\n" +
                "  <memory unit='KiB'>1862656</memory>\n" +
                "  <currentMemory unit='KiB'>1862656</currentMemory>\n" +
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
                "      <source file='/root/tmp/cirros-0.5.2-x86_64-disk.img'/>\n" +
                "      <target dev='vda' bus='virtio'/>\n" +
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @SneakyThrows
    public void testNetwork() {

        Domain domain = GetInfoUtils.getDomainByName(GetInfoUtils.getVirtualList().get(0).getName());
        Network network = domain.getConnect().networkLookupByName("default");
        System.out.println(domain.getName());
        System.out.println(domain.getInfo());
        System.out.println(network.getName());
        System.out.println(network.isActive());
        //        network.create();
        System.out.println(network.isActive());
        //        System.out.println(network.getAutostart());
        String xmlDesc = network.getXMLDesc(0);
        System.out.println(xmlDesc);
    }

    /**
     * Test  get net work state
     */
//    @Test
//    @SneakyThrows
//    public void testGetNetwork(){
//        int[] ids = LibvirtUtils.getConnection().listDomains();
//       for(int id : ids) {
//           Domain domain = GetInfoUtils.getDomainById(id);
//           // net state
//           System.out.println(domain.getName() + "   " + domain.isActive());
//       }
//       // Get conn
//       Network network = LibvirtUtils.getConnection().networkLookupByName("default");
//       String xmlDesc = network.getXMLDesc(0);
//       xmlDesc = xmlDesc.replace("<forward mode='nat'/>\n", "<forward mode='none'/>\n");
////       network.defineXML(xmlDesc);
//        network.undefine();
//        network.
//    }
    @Test
    @SneakyThrows
    public void testGetNetwork() {
        int[] ids = LibvirtUtils.getConnection().listDomains();
        for (int id : ids) {
            Domain domain = GetInfoUtils.getDomainById(id);
//            if(domain.getName().equals("demo2")){
//                String xmlDesc = domain.getXMLDesc(0);
//                xmlDesc = xmlDesc.replace("<interface type='network'>", "<interface type='none'>");
//                domain.updateDeviceFlags(xmlDesc,0);
//                domain.updateDeviceFlags()
//            }


//            if(domain.getName().equals("demo2"))
//            {
//                String xmlDesc = domain.getXMLDesc(0);
//                xmlDesc = xmlDesc.replace(
//                        "<interface type='network'>\n"+
//                                "      <mac address='52:54:00:e4:5f:ad'/>\n" +
//                                "      <source network='default'/>\n" +// 配置默认的网络类型文件
//                                "      <model type='virtio'/>\n" +
//                                "      <address type='pci' domain='0x0000' bus='0x00' slot='0x03' function='0x0'/>\n" +
//                                "    </interface>\n",
//                        "    <interface type='hostdev'>\n" +
//                                "      <mac address='52:54:00:e4:5f:ad'/>\n" +
//                                "      <source network='default'/>\n" +// 配置默认的网络类型文件
//                                "      <model type='virtio'/>\n" +
//                                "      <address type='pci' domain='0x0000' bus='0x00' slot='0x03' function='0x0'/>\n" +
//                                "    </interface>\n");
//                // 重新定义虚拟机
//                domain.undefine();
//                domain = LibvirtUtils.getConnection().domainDefineXML(xmlDesc);
//                domain.create();
//
//            }
            // net state
            System.out.println(domain.getName() + "   " + domain.isActive());
        }

    }


    @Test
    @SneakyThrows
    public void getHostInfo() {
//        System.out.println(SigarUtils.getHostInfo());
    }

    @Test
    @SneakyThrows
    public void testMigrate() {
        Connect sourceConnect = new Connect("qemu+tcp://source_host/system");
        Connect destinationConnect = new Connect("qemu+tcp://destination_host/system");

        Domain domain = sourceConnect.domainLookupByName("demo");

        int flags = 0;
        String dxml = null; // domain name
        String uri = null; // default uri
        int bandwidth = 0; //

        domain.migrate(destinationConnect, flags, dxml, uri, bandwidth);

        sourceConnect.close();
    }

    @Test
    @SneakyThrows
    public void testNet() {
        String networkXml = "<network>\n" +
                "  <name>my_network1</name>\n" +
                "  <forward mode='bridge'/>\n" +
                "  <bridge name=\"br0\"/>\n" +
                "  <ip address='192.168.122.1' netmask='255.255.255.0'>\n" +
                "    <dhcp>\n" +
                "      <range start='192.168.122.2' end='192.168.122.254' />\n" +
                "    </dhcp>\n" +
                "  </ip>\n" +
                "</network>";

        Network network = LibvirtUtils.getConnection().networkCreateXML(networkXml);
        System.out.println("Virtual network created successfully.");
        LibvirtUtils.getConnection().close();
    }

    @Test
    @SneakyThrows
    public void testGetvirsh() {
        String ans = "";
        String name = "demo";
//        String command = "virsh domif-getlink " + name + "vnet0";
        String command = "virsh domif-getlink " + name + " vnet0\n";
//        System.out.println(command);
//        String command = "ls -l";
        Process process = Runtime.getRuntime().exec(command);

        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            ans = line.substring(6);
            System.out.println(ans);
        }
//        int exitCode = process.waitFor();
//        System.out.println(exitCode);
    }

    @Test
    @SneakyThrows
    public void test() {
        int[] ids = LibvirtUtils.getConnection().listDomains();
        for (int id : ids) {
            Domain domain = GetInfoUtils.getDomainById(id);
            String name = domain.getName();
            System.out.println(name);
            String cmd = "virsh domif-getlink " + name + " vnet0\n";
            Process process = Runtime.getRuntime().exec(cmd);
            String ans = "";
            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (ans.length() > 3)
                    ans = line.substring(6);
            }
            System.out.print(ans);
        }
    }

    @Test
    @SneakyThrows
    public void test2() {
        String name = "demo";
//        String cmd = "virsh domif-getlink" + name+ " vnet0\n";
        String cmd = "virsh domiflist " + name;
        Process process = Runtime.getRuntime().exec(cmd);
        String ans = "";
        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        int flag = 0;
        while ((line = reader.readLine()) != null) {
            flag++;
            if (flag == 3)
                ans = line;
        }
        System.out.println(ans);
        String[] columns = ans.split("\\s+");
        System.out.println(columns[0]);
    }


    @Test
    @SneakyThrows
    public void test3() {
        String name = "demo";
        Virtual virtual = GetInfoUtils.getVirtualByName(name);
        System.out.println(virtual.getNetState());
        System.out.println(virtual.getNetInterface());
//        System.out.println(GetInfoUtils.getNetInterface(name));
//        System.out.println(GetInfoUtils.getNetState(name,GetInfoUtils.getNetInterface(name)));
    }

    @Test
    @SneakyThrows
    public void test4() {
        String name = "demo";
        Virtual virtual = GetInfoUtils.getVirtualByName(name);
        System.out.println("begin" + virtual.getNetState());
        String cmd = "virsh domif-setlink " + virtual.getName() + " " + virtual.getNetInterface() + " up";
        System.out.println("cmd = " + cmd);
        // ExeComand
        Process process = Runtime.getRuntime().exec(cmd);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println("end" + GetInfoUtils.getVirtualByName(name).getNetState());
    }

    @Test
    @SneakyThrows
    public void test5() {
        String name = "demo3";
        String ans = "";
        String line;
        String cmd = "virsh domiflist " + name;
        Process process = Runtime.getRuntime().exec(cmd);
        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        int flag = 0;
        while ((line = reader.readLine()) != null) {
            flag++;
            if (flag == 3)
                ans = line;
        }
        String[] columns = ans.split("\\s+");
        System.out.println(columns[1]);
    }

    @Test
    @SneakyThrows
    public void test7() {
        String name = "demo3";
        String netInterface = "vnet0";
        String ans = "";
        String cmd = "virsh domif-getlink " + name + " " + netInterface + "\n";
        Process process = Runtime.getRuntime().exec(cmd);
        // 读取命令输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
            ans = line.substring(6);
        }
        System.out.println(ans);
    }


    @Test
    @SneakyThrows
    public void test8() {
        Connect connect = LibvirtUtils.getConnection();
        Domain domain = connect.domainLookupByName("demo3");

        // 获取域的 XML 描述
        String xmlDesc = domain.getXMLDesc(0);
        System.out.println(xmlDesc);

    }

    @Test
    @SneakyThrows
    public void test9() {
//        String cmd = "virsh edit demo";
//        String cmd = "y";
        // %s/type='network'/type='bridge'/gc
        // %s/type='bridge'/type='network'/gc
        // %s/bridge='br0'/network='default'/g
        // %s/network='default'/bridge='br0'/g
        // 创建进程构建器，并设置要执行的命令
        ProcessBuilder processBuilder = new ProcessBuilder("cd /root", "vim test.txt", "command3");

        // 启动进程
        Process process = processBuilder.start();
        String cmd = "cd /root/";
        Runtime.getRuntime().exec(cmd);
        cmd = "vim test.txt";
        Runtime.getRuntime().exec(cmd);
        cmd = "i";
        Runtime.getRuntime().exec(cmd);
        cmd = "vim test.txt";
        Runtime.getRuntime().exec(cmd);
        cmd = ":wq";
        Runtime.getRuntime().exec(cmd);
//        cmd = ":";
//        Runtime.getRuntime().exec(cmd);
    }

    @Test
    @SneakyThrows
    public void test10() {
        Connect connection = LibvirtUtils.getConnection();
        Domain domain = connection.domainLookupByName("demo3");
        String xmlDesc = domain.getXMLDesc(0);
        String modifyXmlDesc = "";
        modifyXmlDesc = xmlDesc.replace("type='bridge'", "type='network'");
        modifyXmlDesc = modifyXmlDesc.replace("bridge='br0'", "network='default'");

    }

    @Test
    @SneakyThrows
    public void test11() {
        try {
            // 执行 virsh edit 命令，进入编辑模式
            ProcessBuilder processBuilder = new ProcessBuilder("virsh", "edit", "demo3", "y");
            Process process = processBuilder.start();
            process.waitFor();

            // 读取编辑后的 XML 配置
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder xmlBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                xmlBuilder.append(line).append(System.lineSeparator());
            }
            reader.close();

            // 替换 type='network' 为 type='bridge'
            String xml = xmlBuilder.toString();
            xml = xml.replace("type='bridge'", "type='network'");

            // 保存修改并退出 Vim
            ProcessBuilder vimProcessBuilder = new ProcessBuilder("vim", "-");
            Process vimProcess = vimProcessBuilder.start();

            // 将修改后的 XML 写入 Vim 的标准输入
            OutputStreamWriter writer = new OutputStreamWriter(vimProcess.getOutputStream());
            writer.write(xml);
            writer.flush();

            // 发送保存退出命令给 Vim
            writer.write(":wq\n");
            writer.flush();

            // 关闭流
            writer.close();

            // 等待 Vim 执行结束
            vimProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Autowired
    private BasicCommandService basicCommandService;
//nat -- > bridge
    @Test
    @SneakyThrows
    public void test12() {
        try {
            // 加载 XML 文件
            String name = "demo2";
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
            String cmd = "cp /etc/libvirt/qemu/" + name + ".xml" + " /etc/libvirt/qemu/" + name + ".xmls";
            System.out.println(cmd);
            Runtime.getRuntime().exec(cmd);
            System.out.println("XML 文件修改成功！");
            //undefine
            cmd = "virsh undefine " + name;
            Runtime.getRuntime().exec(cmd);
            // mv
            cmd = "mv /etc/libvirt/qemu/" + name + ".xmls" + " /etc/libvirt/qemu/" + name + ".xml";
            System.out.println(cmd);
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// bridge --> nat
    @Test
    @SneakyThrows
    public void test13() {
        try {
//            // 加载 XML 文件
            String name = "demo2";
            String path = "/etc/libvirt/qemu/" + name + ".xml";
            System.out.println(path);
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
    @Test
    @SneakyThrows
    public void test14(){
//        String name = "demo2";
//        String cmd = "cp /etc/libvirt/qemu/" + name + ".xml" + " /etc/libvirt/qemu/" + name + ".xmls";
//        Runtime.getRuntime().exec(cmd);
//        cmd = "mv /etc/libvirt/qemu/" + name + ".xmls" + " /etc/libvirt/qemu/" + name + ".xml";
//        Runtime.getRuntime().exec(cmd);
//        String name ="demo2";
//        String cmd = "virsh undefine /etc/libvirt/qemu/" + name+".xml";
//        System.out.println(cmd);
//        Runtime.getRuntime().exec(cmd);
        String name = "demo2";
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

    @Test
    @SneakyThrows
    public void tests(){
//        String name = "demo2";
//        String cmd = "mv /etc/libvirt/qemu/" + name + ".xmls" + " /etc/libvirt/qemu/" + name + ".xml";
//        Runtime.getRuntime().exec(cmd);
        String name ="demo2";
        String cmd = "virsh define /etc/libvirt/qemu/"+name+".xml";
        System.out.println(cmd);
        Runtime.getRuntime().exec(cmd);
    }
}
