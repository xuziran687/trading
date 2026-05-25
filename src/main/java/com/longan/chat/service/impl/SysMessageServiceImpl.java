package com.longan.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.chat.entity.SysMessage;
import com.longan.chat.mapper.SysMessageMapper;
import com.longan.chat.service.SysMessageService;
import org.springframework.stereotype.Service;

@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements SysMessageService {
}
