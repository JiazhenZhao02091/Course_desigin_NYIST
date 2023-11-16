package com.example.libvirt.pojo;

import lombok.*;
/**
 * @author JiazhenZhao
 * 2023/5/30
 * 类说明：
 */
@Builder
@Getter
@ToString
public class LibvirtConnect {
    private String url;
    private String hostName;
    private long libVirVersion;
    private String hypervisorVersion;
}