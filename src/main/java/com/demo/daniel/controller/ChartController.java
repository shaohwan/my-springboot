package com.demo.daniel.controller;

import com.demo.daniel.model.ApiResponse;
import com.demo.daniel.model.vo.ChartVO;
import com.demo.daniel.service.chart.ChartLogLoginService;
import com.demo.daniel.service.chart.ChartLogOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chart")
public class ChartController {

    @Autowired
    private ChartLogOperateService chartLogOperateService;
    @Autowired
    private ChartLogLoginService chartLogLoginService;

    @GetMapping
    public ApiResponse<ChartVO> getChart(@RequestParam("chartId") int chartId) {
        ChartVO chart = switch (chartId) {
            case 1 -> chartLogOperateService.getDailyUserOperateCounts();
            case 2 -> chartLogLoginService.getDailyUserLoginCounts();
            default ->
                // empty data
                    ChartVO.builder().title("").type("").labels(new String[]{}).series(List.of()).build();
        };
        return ApiResponse.ok(chart);
    }
}
