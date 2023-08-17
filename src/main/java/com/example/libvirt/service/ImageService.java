package com.example.libvirt.service;

import com.example.libvirt.pojo.ImgFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author JiazhenZhao
 * 2023/5/30
 * 类说明：
 */
public interface ImageService {
    public List<ImgFile> getImgList();
    public Boolean addImgFile(String name, MultipartFile file) throws IOException;
    public String downImgFile(String name, HttpServletResponse response) throws IOException;
    public Boolean deleteImgFile(String name);
}
