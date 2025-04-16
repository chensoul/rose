package com.chensoul.rose.upms.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chensoul.rose.upms.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {}
