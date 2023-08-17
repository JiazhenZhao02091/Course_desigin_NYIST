package com.example.libvirt.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
/**
 * @author JiazhenZhao
 * 2023/5/30
 * 类说明：
 */
@Builder
@Getter
@ToString
public class Snapshot {
    private String name;
    private String creationTime;
    private String state;
}
