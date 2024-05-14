package com.example.inventorydemo.api;

import com.example.inventorydemo.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class InventoryApi {
    @Autowired
    private InventoryService inventoryService;
    @GetMapping("/test")
    public String test(int reduceNum) {
        inventoryService.reduceInventory(reduceNum);
        return "ok";
    }
}
