package com.longan.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.longan.user.entity.UserAddress;
import java.util.List;

public interface UserAddressService extends IService<UserAddress> {
    List<UserAddress> getByUserId(Long userId);

    UserAddress getDefaultByUserId(Long userId);

    void setDefault(Long id, Long userId);
}
