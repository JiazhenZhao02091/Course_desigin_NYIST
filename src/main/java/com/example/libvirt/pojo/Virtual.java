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
public class Virtual {
    private int id;
    private String name;
    private String state;
    private String netState;
    private String netInterface;
    private String netType;
}
