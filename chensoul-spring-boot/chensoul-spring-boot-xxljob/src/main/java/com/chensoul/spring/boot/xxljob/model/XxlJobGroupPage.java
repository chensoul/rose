package com.chensoul.spring.boot.xxljob.model;

import java.util.List;
import lombok.Data;

@Data
public class XxlJobGroupPage {

    private Long recordsFiltered;

    private Long recordsTotal;

    private List<XxlJobGroup> data;
}
