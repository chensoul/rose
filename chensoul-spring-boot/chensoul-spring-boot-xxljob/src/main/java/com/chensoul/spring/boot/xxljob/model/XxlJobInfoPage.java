package com.chensoul.spring.boot.xxljob.model;

import lombok.Data;

import java.util.List;

@Data
public class XxlJobInfoPage {

	private Long recordsFiltered;

	private Long recordsTotal;

	private List<XxlJobInfo> data;
}
