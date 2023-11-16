package com.example.libvirt.controller;

import com.example.libvirt.Utils.GetInfoUtils;
import com.example.libvirt.pojo.*;
import com.example.libvirt.service.*;
import lombok.SneakyThrows;
import org.libvirt.LibvirtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class LibvirtController {

    @Autowired
    private StoragePoolService storagePoolService;
    @Autowired
    private BasicCommandService basicCommandService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private NetworkService networkService;
    @Autowired
    private SnapshotService snapshotService;
    @Autowired
    private VirtualService virtualService;


    @RequestMapping(value = {"/index"})
    public String index(Model model) {
        Host hostInfo = GetInfoUtils.getHostInfo();
        model.addAttribute("hostinfo", hostInfo);
        LibvirtConnect connectInformation = GetInfoUtils.getLibvirtConnectInformation();
        model.addAttribute("connectInformation", connectInformation);
        return "index";
    }

    @RequestMapping("/main")
    public String main(Model model) throws LibvirtException {
        List<Virtual> virtualList = GetInfoUtils.getVirtualList();
        model.addAttribute("virtualList", virtualList);
        String netState = networkService.getNetState();
        model.addAttribute("netState", netState);
        Host hostInfo = GetInfoUtils.getHostInfo();
        model.addAttribute("hostinfo", hostInfo);
        LibvirtConnect connectInformation = GetInfoUtils.getLibvirtConnectInformation();
        model.addAttribute("connectInformation", connectInformation);
        return "main";
    }

    @RequestMapping("/openOrCloseNetWork")
    public String openOrCloseNetWork(@RequestParam("netState") String netState) throws LibvirtException {
        if ("on".equals(netState)) networkService.closeNetWork();
        if ("off".equals(netState)) networkService.openNetWork();
        return "redirect:main";
    }

    @SneakyThrows
    @RequestMapping("/initiate")
    public String initiateVirtual(@RequestParam("name") String name) {
        basicCommandService.initiateDomainByName(name);
        Thread.sleep(1000);
        return "redirect:main";
    }

    @SneakyThrows
    @RequestMapping("/suspended")
    public String suspendedVirtual(@RequestParam("name") String name) {
        basicCommandService.suspendedDomainByName(name);
        Thread.sleep(1000);
        return "redirect:main";
    }

    @SneakyThrows
    @RequestMapping("/resume")
    public String resumeVirtual(@RequestParam("name") String name) {
        basicCommandService.resumeDomainByName(name);
        Thread.sleep(1000);
        return "redirect:main";
    }

    @SneakyThrows
    @RequestMapping("/save")
    public String saveVirtual(@RequestParam("name") String name) {

        basicCommandService.saveDomainByName(name);
        Thread.sleep(1000);
        return "redirect:main";
    }

    @SneakyThrows
    @RequestMapping("/restore")
    public String restoreVirtual(@RequestParam("name") String name) {
        basicCommandService.restoreDomainByName(name);
        Thread.sleep(1000);
        return "redirect:main";
    }

    @SneakyThrows
    @RequestMapping("/shutdown")
    public String shutdownVirtual(@RequestParam("name") String name) {
        basicCommandService.shutdownDomainByName(name);
        Thread.sleep(1000);
        return "redirect:main";
    }

    @SneakyThrows
    @RequestMapping("/shutdownMust")
    public String shutdownMustVirtual(@RequestParam("name") String name) {
        basicCommandService.shutdownMustDomainByName(name);
        Thread.sleep(1000);
        return "redirect:main";
    }

    @SneakyThrows
    @RequestMapping("/reboot")
    public String rebootVirtual(@RequestParam("name") String name) {
        basicCommandService.rebootDomainByName(name);
        Thread.sleep(1000);
        return "redirect:main";
    }

    @RequestMapping("/delete")
    public String deleteVirtual(@RequestParam("name") String name) throws LibvirtException {
        virtualService.deleteDomainByName(name);
        imageService.deleteImgFile(name + ".img");
        return "redirect:main";
    }

    @RequestMapping("/toAddVirtual")
    public String toAddVirtual(Model model) throws LibvirtException {
        String netState = networkService.getNetState();
        model.addAttribute("netState", netState);
        return "addVirtual";
    }

    @RequestMapping(value = "/addVirtual", method = RequestMethod.POST)
    public String addVirtual(@RequestParam("virtualName") String name,
                             @RequestPart("file") MultipartFile file) throws LibvirtException, IOException {
        virtualService.addDomainByName(name);
        imageService.addImgFile(name, file);
        return "redirect:main";
    }

    @RequestMapping("/img")
    public String imgList(Model model) throws LibvirtException {
        List<ImgFile> imgList = imageService.getImgList();
        model.addAttribute("imgList", imgList);
        String netState = networkService.getNetState();
        model.addAttribute("netState", netState);
        return "img";
    }

    @RequestMapping("/toAddImg")
    public String toAddImg(Model model) throws LibvirtException {
        String netState = networkService.getNetState();
        model.addAttribute("netState", netState);
        return "addImg";
    }

    @RequestMapping("/addImg")
    public String addImg(@RequestParam("imgName") String name,
                         @RequestPart("file") MultipartFile file) throws IOException {
        imageService.addImgFile(name, file);
        return "redirect:img";
    }

    @RequestMapping("/deleteImg")
    public String deleteImg(@RequestParam("name") String name) {
        imageService.deleteImgFile(name);
        return "redirect:img";
    }

    @ResponseBody
    @RequestMapping("/downImg")
    public String downImg(@RequestParam("name") String name, HttpServletResponse response) throws IOException {
        return imageService.downImgFile(name, response);
    }

    @RequestMapping("/getSnapshotList")
    public String getSnapshotList(@RequestParam("name") String name,
                                  Model model) throws IOException {
        List<Snapshot> snapshotList = snapshotService.getSnapshotListByName(name);
        model.addAttribute("snapshotList", snapshotList);
        model.addAttribute("virtualName", name);
        return "snapshot";
    }

    @SneakyThrows
    @RequestMapping("/deleteSnapshot")
    public String deleteSnapshot(@RequestParam("virtualName") String virtualName,
                                 @RequestParam("snapshotName") String snapshotName) {
        snapshotService.deleteSnapshot(virtualName, snapshotName);
        Thread.sleep(1000);
        return "redirect:/getSnapshotList?name=" + virtualName;
    }

    @SneakyThrows
    @RequestMapping("/revertSnapshot")
    public String revertSnapshot(@RequestParam("virtualName") String virtualName,
                                 @RequestParam("snapshotName") String snapshotName) {
        snapshotService.revertSnapshot(virtualName, snapshotName);
        Thread.sleep(1000);
        return "redirect:/getSnapshotList?name=" + virtualName;
    }

    @SneakyThrows
    @RequestMapping("/createSnapshot")
    public String createSnapshot(@RequestParam("virtualName") String virtualName,
                                 @RequestParam("snapshotName") String snapshotName) {
        snapshotService.createSnapshot(virtualName, snapshotName);
        Thread.sleep(1000);
        return "redirect:/getSnapshotList?name=" + virtualName;
    }

    @RequestMapping("/storagePoolList")
    public String storagePoolList(Model model) throws LibvirtException {
        List<Storagepool> storagePoolList = storagePoolService.getStoragePoolList();
        model.addAttribute("storagePoolList", storagePoolList);
        String netState = networkService.getNetState();
        model.addAttribute("netState", netState);
        return "storagepool";
    }

    @SneakyThrows
    @RequestMapping("/deleteStoragePool")
    public String deleteStoragePool(@RequestParam("name") String name) {
        storagePoolService.deleteStoragePool(name);
        Thread.sleep(1000);
        return "redirect:/storagePoolList";
    }

    @RequestMapping("/toCreateStoragepool")
    public String toCreateStoragepool(Model model) throws LibvirtException {
        String netState = networkService.getNetState();
        model.addAttribute("netState", netState);
        return "addStoragepool";
    }

    @SneakyThrows
    @RequestMapping("/createStoragepool")
    public String createStoragepool(@RequestParam("storagepoolName") String name,
                                    @RequestParam("storagepoolPath") String url) {
        storagePoolService.createStoragePool(name, url);
        Thread.sleep(1000);
        return "redirect:/storagePoolList";
    }

    @SneakyThrows
    @RequestMapping("/openOrcloseNetWorkByName")
    public String openNetByName(@RequestParam("name")String name,
                                @RequestParam("netState")String netState){
        if(netState.equals("down")){
            networkService.openNetWorkByName(name);
        }
        else if(netState.equals("up")){
            networkService.closeNetWorkByName(name);
        }
        return "redirect:main";
    }
    @SneakyThrows
    @RequestMapping("/switchNetState")
    public String switchNetState(@RequestParam("name")String name,
                                @RequestParam("netType")String netType){
        if(netType.equals("bridge")){
            networkService.changeNetKindToNat(name);
        }else if (netType.equals("network")){
            networkService.changeNetKindToBridge(name);
        }
        return "redirect:main";
    }
}
