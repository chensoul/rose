package com.chensoul.rose.xxljob.model;

import java.util.List;
import lombok.Data;

@Data
public class XxlJobInfoPage {

    private Long recordsFiltered;

    private Long recordsTotal;

    private List<XxlJobInfo> data;
}
