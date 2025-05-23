package com.demo.daniel.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@UtilityClass
@Slf4j
public class ExcelUtils {

    public static <T> void exportExcel(List<T> data, String fileName, String sheetName, Class<T> clazz) {
        HttpServletResponse response = HttpContextUtils.getHttpServletResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setCharacterEncoding("utf-8");
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fullFileName = fileName + "_" + timestamp + ".xlsx";
        String encodedFileName = URLEncoder.encode(fullFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + encodedFileName);

        try {
            ExcelWriterSheetBuilder writerBuilder = EasyExcel.write(response.getOutputStream(), clazz)
                    .autoCloseStream(true)
                    .sheet(StringUtils.isBlank(sheetName) ? AppConstants.DEFAULT_SHEET_NAME : sheetName);
            writerBuilder.doWrite(data);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }
}
