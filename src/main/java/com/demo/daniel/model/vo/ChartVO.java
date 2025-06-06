package com.demo.daniel.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartVO {

    private String title;

    private String type;

    private String[] labels;

    private List<SeriesData> series;

    @Data
    @Builder
    public static class SeriesData {
        private String name;

        private int[] data;
    }
}
