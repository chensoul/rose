package com.chensoul.rose.upms.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chensoul.rose.upms.domain.entity.Log;
import com.chensoul.rose.upms.domain.mapper.LogMapper;
import com.chensoul.rose.upms.domain.service.LogService;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {}
