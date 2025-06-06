package com.demo.daniel.service.chart;

import com.demo.daniel.model.entity.LogLogin;
import com.demo.daniel.model.vo.ChartVO;
import com.demo.daniel.repository.LogLoginRepository;
import com.demo.daniel.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChartLogLoginService {

    @Autowired
    private LogLoginRepository logLoginRepository;

    public ChartVO getDailyUserLoginCounts() {
        List<LogLogin> logLogins = logLoginRepository.findAll();

        Map<String, Map<String, Long>> dailyUserCounts = logLogins.stream()
                .collect(Collectors.groupingBy(
                        logLogin -> logLogin.getCreateTime().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        Collectors.groupingBy(LogLogin::getUsername,
                                Collectors.counting()
                        )
                ));

        List<String> dates = new ArrayList<>(dailyUserCounts.keySet());
        Collections.sort(dates);

        Set<String> usernames = dailyUserCounts.values().stream()
                .flatMap(m -> m.keySet().stream())
                .collect(Collectors.toSet());

        List<ChartVO.SeriesData> seriesDataList = new ArrayList<>();
        for (String username : usernames) {
            int[] data = new int[dates.size()];
            for (int i = 0; i < dates.size(); i++) {
                String date = dates.get(i);
                data[i] = dailyUserCounts.getOrDefault(date, Collections.emptyMap())
                        .getOrDefault(username, 0L)
                        .intValue();
            }
            seriesDataList.add(ChartVO.SeriesData.builder()
                    .name(username)
                    .data(data)
                    .build());
        }

        String[] labels = dates.toArray(new String[0]);

        return ChartVO.builder()
                .title("每日用户登录次数统计")
                .type(AppConstants.BAR)
                .labels(labels)
                .series(seriesDataList)
                .build();
    }
}
