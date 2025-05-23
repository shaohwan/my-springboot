package com.demo.daniel.model.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.demo.daniel.model.entity.LogLoginOperation;
import com.demo.daniel.model.entity.LogStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogLoginVO {

    @ExcelIgnore
    private Long id;

    @ExcelProperty(value = "用户名", index = 0)
    private String username;

    @ExcelProperty(value = "登录IP", index = 1)
    private String ip;

    @ExcelProperty(value = "登录地点", index = 2)
    private String address;

    @ExcelProperty(value = "User Agent", index = 3)
    private String userAgent;

    @ExcelProperty(value = "登录状态", index = 4, converter = LogStatusConverter.class)
    private LogStatus status;

    @ExcelProperty(value = "操作信息", index = 5, converter = LogLoginOperationConverter.class)
    private LogLoginOperation operation;

    @ExcelProperty(value = "创建时间", index = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    public static class LogStatusConverter implements Converter<LogStatus> {
        @Override
        public Class<?> supportJavaTypeKey() {
            return LogStatus.class;
        }

        @Override
        public CellDataTypeEnum supportExcelTypeKey() {
            return CellDataTypeEnum.STRING;
        }

        @Override
        public WriteCellData<?> convertToExcelData(LogStatus value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
            String description = switch (value) {
                case SUCCESS -> "成功";
                case FAILURE -> "失败";
            };
            return new WriteCellData<>(description);
        }
    }

    public static class LogLoginOperationConverter implements Converter<LogLoginOperation> {
        @Override
        public Class<?> supportJavaTypeKey() {
            return LogLoginOperation.class;
        }

        @Override
        public CellDataTypeEnum supportExcelTypeKey() {
            return CellDataTypeEnum.STRING;
        }

        @Override
        public WriteCellData<?> convertToExcelData(LogLoginOperation value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
            String description = switch (value) {
                case LOGIN_SUCCESS -> "登录成功";
                case LOGOUT_SUCCESS -> "退出成功";
                case ACCOUNT_FAILURE -> "账号错误";
            };
            return new WriteCellData<>(description);
        }
    }
}
