package com.demo.daniel.model.vo.monitor;

import cn.hutool.system.SystemUtil;
import cn.hutool.system.oshi.OshiUtil;
import lombok.Data;

@Data
public class SysVO {

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 系统架构
     */
    private String osArch;

    /**
     * 系统版本
     */
    private String osVersion;


    /**
     * 服务器名称
     */
    private String computerName;

    /**
     * 服务器Ip
     */
    private String computerIp;

    public SysVO() {
        this.setOsName(SystemUtil.getOsInfo().getName());
        this.setOsArch(SystemUtil.getOsInfo().getArch());
        this.setOsVersion(SystemUtil.getOsInfo().getVersion());
        this.setComputerName(OshiUtil.getOs().getNetworkParams().getHostName());
        this.setComputerIp(SystemUtil.getHostInfo().getAddress());
    }
}
