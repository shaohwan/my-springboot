package com.demo.daniel.model.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.demo.daniel.model.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserVO {

    @ExcelProperty(value = "ID", index = 0)
    private Long id;

    @ExcelProperty(value = "用户名", index = 1)
    private String username;

    @ExcelIgnore
    private String password;

    @ExcelProperty(value = "真实姓名", index = 2)
    private String realName;

    @ExcelProperty(value = "邮箱", index = 3)
    private String email;

    @ExcelProperty(value = "电话", index = 4)
    private String phone;

    @ExcelIgnore
    private String avatar;

    @ExcelProperty(value = "启用状态", index = 5, converter = EnabledConverter.class)
    private Boolean enabled;

    @ExcelProperty(value = "超级管理员", index = 6, converter = SuperAdminConverter.class)
    private Boolean superAdmin;

    @ExcelProperty(value = "创建时间", index = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ExcelProperty(value = "角色", index = 8, converter = RolesConverter.class)
    private Set<Role> roles = new HashSet<>();

    public static class EnabledConverter implements Converter<Boolean> {
        @Override
        public Class<?> supportJavaTypeKey() {
            return Boolean.class;
        }

        @Override
        public CellDataTypeEnum supportExcelTypeKey() {
            return CellDataTypeEnum.STRING;
        }

        @Override
        public WriteCellData<?> convertToExcelData(Boolean value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
            return new WriteCellData<>(value != null && value ? "启用" : "禁用");
        }
    }

    public static class SuperAdminConverter implements Converter<Boolean> {
        @Override
        public Class<?> supportJavaTypeKey() {
            return Boolean.class;
        }

        @Override
        public CellDataTypeEnum supportExcelTypeKey() {
            return CellDataTypeEnum.STRING;
        }

        @Override
        public WriteCellData<?> convertToExcelData(Boolean value, ExcelContentProperty property, GlobalConfiguration globalConfiguration) {
            return new WriteCellData<>(value != null && value ? "是" : "否");
        }
    }

    public static class RolesConverter implements Converter<Set<Role>> {
        @Override
        public Class<?> supportJavaTypeKey() {
            return Set.class;
        }

        @Override
        public CellDataTypeEnum supportExcelTypeKey() {
            return CellDataTypeEnum.STRING;
        }

        @Override
        public WriteCellData<?> convertToExcelData(Set<Role> value, ExcelContentProperty property, GlobalConfiguration globalConfiguration) {
            if (value == null || value.isEmpty()) {
                return new WriteCellData<>("无角色");
            }
            String rolesStr = value.stream()
                    .map(role -> role.getName() != null ? role.getName() : "未知角色")
                    .collect(Collectors.joining(", "));
            return new WriteCellData<>(rolesStr);
        }
    }
}
