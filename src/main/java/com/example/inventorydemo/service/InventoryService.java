package com.example.inventorydemo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.inventorydemo.mapper.InventoryMapper;
import com.example.inventorydemo.model.Orders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class InventoryService extends ServiceImpl<InventoryMapper, Orders> {
    //创建线程池
    private static final ExecutorService SERVICE = Executors.newCachedThreadPool();

    /**
     * 减扣逻辑
     *
     * @return 剩余数量
     */
    public void reduceInventory(int reduceNum) {
        boolean a;
        do {
            // 查询现有的
            Orders orders = getOne(new LambdaQueryWrapper<Orders>().eq(Orders::getId, 1));

            if (ObjectUtils.isEmpty(orders) ||
                    ObjectUtils.isEmpty(orders.getRemainingNum()) ||
                    orders.getRemainingNum() == 0) {
                {
                    log.warn("当前没有剩余");
                    return;
                }
            }

            if (orders.getRemainingNum() - reduceNum < 0) {
                log.warn("当前库存不够, 减扣失败. 当前库存为: {}, 需要减扣的库存为: {}",
                        orders.getRemainingNum(), reduceNum);
                return;
            }
            orders.setRemainingNum(orders.getRemainingNum() - reduceNum);

            LambdaUpdateWrapper<Orders> queryWrapper = new LambdaUpdateWrapper<>();
            queryWrapper.eq(Orders::getId, orders.getId());
            queryWrapper.eq(Orders::getVersion, orders.getVersion());
            queryWrapper.gt(Orders::getRemainingNum, reduceNum);
            String replace = UUID.randomUUID().toString().replace("-", "");
            queryWrapper.set(Orders::getVersion, replace);
            queryWrapper.set(Orders::getRemainingNum, orders.getRemainingNum() - reduceNum);
            a = update(queryWrapper);
            if (!a) {
                log.info("库存减扣成功，扣除之后剩余: {}, 扣除数量: {}, 扣除之前的版本: {}, 扣除更新版本: {}",
                        orders.getRemainingNum(), reduceNum, orders.getVersion(), replace);
            }
        } while (a);

    }
}
