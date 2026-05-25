package com.longan.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.longan.result.PageResult;
import com.longan.user.entity.WalletLog;
import com.longan.user.vo.WalletLogVO;

public interface WalletLogService extends IService<WalletLog> {
    PageResult<WalletLogVO> getWalletLog(Long userId, Integer type, Integer businessType, Integer page, Integer size);
}
