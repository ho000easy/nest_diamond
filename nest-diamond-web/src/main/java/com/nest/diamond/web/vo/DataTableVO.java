package com.nest.diamond.web.vo;

import lombok.Data;

import java.util.List;

@Data
public class DataTableVO<T> {
    private Integer draw;
    private Integer recordsTotal;
    private Integer recordsFiltered;
    private List<T> data;

    public static <T> DataTableVO<T> create(List<T> data){
        DataTableVO<T> dataTableVO = new DataTableVO<T>();
        dataTableVO.setDraw(1);
        dataTableVO.setRecordsTotal(data.size());
        dataTableVO.setRecordsFiltered(data.size());
        dataTableVO.setData(data);
        return dataTableVO;
    }
}
