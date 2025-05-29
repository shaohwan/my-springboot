package com.demo.daniel.model.vo.monitor;

import com.demo.daniel.util.ArityUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

import java.util.LinkedList;
import java.util.List;

@Data
@Slf4j
public class ServerVO {

    @JsonProperty("cpu")
    private CpuVO cpuVO;

    @JsonProperty("disks")
    private List<DiskVO> disks = new LinkedList<>();

    @JsonProperty("jvm")
    private JvmVO jvmVO;

    @JsonProperty("mem")
    private MemVO memVO;

    @JsonProperty("sys")
    private SysVO sysVO;

    public ServerVO() {
        this.cpuVO = new CpuVO();
        this.memVO = new MemVO();
        this.jvmVO = new JvmVO();
        this.sysVO = new SysVO();
        this.setDiskList();
    }

    public ServerVO(DiskVO disk) {
        this.setDiskList();
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

    /**
     * 设置磁盘信息
     */
    private void setDiskList() {
        SystemInfo systemInfo = new SystemInfo();
        FileSystem fileSystem = systemInfo.getOperatingSystem().getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            DiskVO diskVO = new DiskVO();
            diskVO.setDiskName(fs.getName());
            diskVO.setDiskType(fs.getType());
            diskVO.setDirName(fs.getMount());
            diskVO.setTotal(convertFileSize(total));
            diskVO.setFree(convertFileSize(free));
            diskVO.setUsed(convertFileSize(used));
            diskVO.setUsage(ArityUtil.mul(ArityUtil.div(used, total, 4), 100));
            this.disks.add(diskVO);
        }
    }
}
