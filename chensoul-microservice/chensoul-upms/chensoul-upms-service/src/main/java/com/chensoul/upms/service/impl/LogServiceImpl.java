package com.chensoul.upms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chensoul.upms.entity.Log;
import com.chensoul.upms.mapper.LogMapper;
import com.chensoul.upms.service.LogService;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {}
