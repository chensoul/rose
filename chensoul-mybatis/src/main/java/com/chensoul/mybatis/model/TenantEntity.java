package com.chensoul.mybatis.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class TenantEntity extends BaseEntity implements Serializable {

	@TableField(fill = FieldFill.INSERT)
	private String tenantId;

}
