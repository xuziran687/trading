package com.longan.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.result.PageResult;
import com.longan.user.entity.WalletLog;
import com.longan.user.mapper.WalletLogMapper;
import com.longan.user.service.WalletLogService;
import com.longan.user.vo.WalletLogVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WalletLogServiceImpl extends ServiceImpl<WalletLogMapper, WalletLog> implements WalletLogService {
    private final WalletLogMapper walletLogMapper;

    @Override
    public PageResult<WalletLogVO> getWalletLog(Long userId, Integer type, Integer businessType, Integer page, Integer size) {
        IPage<WalletLog> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<WalletLog> wrapper = buildQueryWrapper(userId, type, businessType);
        walletLogMapper.selectPage(pageParam, wrapper);

        List<WalletLogVO> voList = pageParam.getRecords().stream().map(walletLog -> {
            WalletLogVO vo = new WalletLogVO();
            vo.set(walletLog);
            return vo;
        }).toList();

        PageResult<WalletLogVO> result = new PageResult<>();
        result.setPages(pageParam.getPages());
        result.setTotal(pageParam.getTotal());
        result.setList(voList);
        return result;
    }

    private LambdaQueryWrapper<WalletLog> buildQueryWrapper(Long userId, Integer type, Integer businessType) {
        LambdaQueryWrapper<WalletLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WalletLog::getUserId, userId);
        wrapper.eq(type != null, WalletLog::getType, type);
        wrapper.eq(businessType != null, WalletLog::getBusinessType, businessType);
        wrapper.orderByDesc(WalletLog::getCreateTime);
        return wrapper;
    }
}
