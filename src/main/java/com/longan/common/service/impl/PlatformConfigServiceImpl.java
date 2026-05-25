package com.longan.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.common.entity.PlatformConfig;
import com.longan.common.mapper.PlatformConfigMapper;
import com.longan.common.service.PlatformConfigService;
import org.springframework.stereotype.Service;

@Service
public class PlatformConfigServiceImpl extends ServiceImpl<PlatformConfigMapper, PlatformConfig> implements PlatformConfigService {
}
