package com.example.inventorydemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.inventorydemo.model.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InventoryMapper extends BaseMapper<Orders> {
}
