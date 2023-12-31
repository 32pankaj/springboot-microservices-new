package com.programming.tichi.inventoryservice.service;

import com.programming.tichi.inventoryservice.dto.InventoryResponse;
import com.programming.tichi.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream().
                map(inventory ->
                        InventoryResponse.builder().skuCode(inventory.getSkuCode())
                                .isInStoke(inventory.getQuantity() > 0)
                                .build()).toList();

    }
}
