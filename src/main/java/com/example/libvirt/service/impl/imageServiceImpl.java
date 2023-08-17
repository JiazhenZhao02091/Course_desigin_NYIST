package com.example.libvirt.service.impl;

import com.example.libvirt.pojo.ImgFile;
import com.example.libvirt.service.ImageService;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JiazhenZhao
 * 2023/5/30
 * 类说明：
 */
@Service
@Log
public class imageServiceImpl implements ImageService {
    @Override
    public List<ImgFile> getImgList() {
        List<ImgFile> list = new ArrayList<>();
        File[] files = new File("/var/lib/libvirt/images/").listFiles(); // 配置所有的镜像源
        if (files != null) {
            for (File file : files) {
                list.add(ImgFile.builder()
                        .name(file.getName())
                        .size(FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file)))
                        .build());
            }
        }
        return list;
    }

    @Override
    public Boolean addImgFile(String name, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            file.transferTo(new File("/var/lib/libvirt/images/" + name + ".img"));
            log.info("文件" + name + ".img已经保存！");
            return true;
        }
        log.info("文件" + name + ".img保存失败！");
        return false;
    }

    @Override
    public String downImgFile(String name, HttpServletResponse response) throws IOException {
        File file = new File("/var/lib/libvirt/images/" + name);
        if (!file.exists()) return "下载文件不存在";
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(file.getName(), "UTF-8"));  // 设置编码格式
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        OutputStream os = response.getOutputStream();
        int i = 0;
        byte[] buff = new byte[1024];
        while ((i = bis.read(buff)) != -1) {
            os.write(buff, 0, i);
            os.flush();
        }
        bis.close();
        os.close();
        return "下载成功";
    }

    @Override
    public Boolean deleteImgFile(String name) {
        if (new File("/var/lib/libvirt/images/" + name).delete()) {
            log.info("文件" + name + "已经删除！");
            return true;
        }
        log.info("文件" + name + "文件不存在");
        return false;
    }
}
