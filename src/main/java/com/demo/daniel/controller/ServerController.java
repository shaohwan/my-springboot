package com.demo.daniel.controller;

import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.vo.monitor.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/monitor/server")
public class ServerController {

    /**
     * 服务器相关信息
     */
    @GetMapping("/info")
    @PreAuthorize("hasAuthority('monitor:server:all')")
    public ApiResponse<ServerVO> getServerInfo() {
        ServerVO server = new ServerVO();
        return ApiResponse.ok(server);
    }

    /**
     * CPU相关信息
     */
    @GetMapping("/cpu")
    @PreAuthorize("hasAuthority('monitor:server:all')")
    public ApiResponse<CpuVO> getCpuInfo() {
        CpuVO cpu = new CpuVO();
        return ApiResponse.ok(cpu);
    }

    /**
     * 内存相关信息
     */
    @GetMapping("/mem")
    @PreAuthorize("hasAuthority('monitor:server:all')")
    public ApiResponse<MemVO> getMemInfo() {
        MemVO mem = new MemVO();
        return ApiResponse.ok(mem);
    }

    /**
     * JVM相关信息
     */
    @GetMapping("/jvm")
    @PreAuthorize("hasAuthority('monitor:server:all')")
    public ApiResponse<JvmVO> getJvmInfo() {
        JvmVO jvm = new JvmVO();
        return ApiResponse.ok(jvm);
    }

    /**
     * 系统相关信息
     */
    @GetMapping("/sys")
    @PreAuthorize("hasAuthority('monitor:server:all')")
    public ApiResponse<SysVO> getSysInfo() {
        SysVO sys = new SysVO();
        return ApiResponse.ok(sys);
    }

    /**
     * 系统文件相关信息
     */
    @GetMapping("/disk")
    @PreAuthorize("hasAuthority('monitor:server:all')")
    public ApiResponse<List<DiskVO>> getSysFileInfo() {
        ServerVO server = new ServerVO(new DiskVO());
        return ApiResponse.ok(server.getDisks());
    }
}
