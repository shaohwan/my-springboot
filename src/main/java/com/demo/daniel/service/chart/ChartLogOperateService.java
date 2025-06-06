package com.demo.daniel.service.chart;

import com.demo.daniel.model.entity.LogOperate;
import com.demo.daniel.model.vo.ChartVO;
import com.demo.daniel.repository.LogOperateRepository;
import com.demo.daniel.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChartLogOperateService {

    @Autowired
    private LogOperateRepository logOperateRepository;

    public ChartVO getDailyUserOperateCounts() {
        List<LogOperate> logOperates = logOperateRepository.findAll();

        Map<String, Map<String, Long>> dailyTypeCounts = logOperates.stream()
                .collect(Collectors.groupingBy(
                        logOperate -> logOperate.getCreateTime().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        Collectors.groupingBy(logOperate -> logOperate.getType().name(),
                                Collectors.counting()
                        )
                ));

        List<String> dates = new ArrayList<>(dailyTypeCounts.keySet());
        Collections.sort(dates);

        Set<String> operationTypes = dailyTypeCounts.values().stream()
                .flatMap(typeMap -> typeMap.keySet().stream())
                .collect(Collectors.toSet());

        List<ChartVO.SeriesData> seriesDataList = new ArrayList<>();
        for (String operationType : operationTypes) {
            int[] data = new int[dates.size()];
            for (int i = 0; i < dates.size(); i++) {
                String date = dates.get(i);
                data[i] = dailyTypeCounts.getOrDefault(date, Collections.emptyMap())
                        .getOrDefault(operationType, 0L)
                        .intValue();
            }
            seriesDataList.add(ChartVO.SeriesData.builder()
                    .name(operationType)
                    .data(data)
                    .build());
        }

        String[] labels = dates.toArray(new String[0]);

        return ChartVO.builder()
                .title("每日系统操作类型统计")
                .type(AppConstants.BAR)
                .labels(labels)
                .series(seriesDataList)
                .build();
    }
}
