package com.longan.user.controller;

import com.longan.result.Result;
import com.longan.user.entity.UserAddress;
import com.longan.user.service.UserAddressService;
import com.longan.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "收货地址")
@RestController
@RequestMapping("/api/user/address")
@RequiredArgsConstructor
public class UserAddressController {

    private final UserAddressService userAddressService;

    @Operation(summary = "获取收货地址列表")
    @GetMapping("/list")
    public Result list() {
        Long userId = UserContext.getUserId();
        List<UserAddress> list = userAddressService.getByUserId(userId);
        return Result.success(list);
    }

    @Operation(summary = "获取默认收货地址")
    @GetMapping("/default")
    public Result getDefault() {
        Long userId = UserContext.getUserId();
        UserAddress addr = userAddressService.getDefaultByUserId(userId);
        return Result.success(addr);
    }

    @Operation(summary = "添加收货地址")
    @PostMapping
    public Result add(@RequestBody UserAddress address) {
        Long userId = UserContext.getUserId();
        address.setId(null);
        address.setUserId(userId);
        if (address.getIsDefault() == null) {
            address.setIsDefault(0);
        }
        userAddressService.save(address);
        return Result.success(address);
    }

    @Operation(summary = "更新收货地址")
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody UserAddress address) {
        Long userId = UserContext.getUserId();
        UserAddress existing = userAddressService.getById(id);
        if (existing == null || !existing.getUserId().equals(userId)) {
            return Result.error("地址不存在");
        }
        address.setId(id);
        address.setUserId(userId);
        userAddressService.updateById(address);
        return Result.success();
    }

    @Operation(summary = "删除收货地址")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        Long userId = UserContext.getUserId();
        UserAddress existing = userAddressService.getById(id);
        if (existing == null || !existing.getUserId().equals(userId)) {
            return Result.error("地址不存在");
        }
        userAddressService.removeById(id);
        return Result.success();
    }

    @Operation(summary = "设置默认地址")
    @PutMapping("/{id}/default")
    public Result setDefault(@PathVariable Long id) {
        Long userId = UserContext.getUserId();
        UserAddress existing = userAddressService.getById(id);
        if (existing == null || !existing.getUserId().equals(userId)) {
            return Result.error("地址不存在");
        }
        userAddressService.setDefault(id, userId);
        return Result.success();
    }
}
