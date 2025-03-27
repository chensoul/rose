package com.chensoul.upms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chensoul.upms.entity.UserTenant;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper extends BaseMapper<UserTenant> {
}
