package com.programming.tichi.orderservice.service;

import com.programming.tichi.orderservice.dto.OrderLineItemsDto;
import com.programming.tichi.orderservice.dto.OrderRequest;
import com.programming.tichi.orderservice.model.Order;
import com.programming.tichi.orderservice.model.OrderLineItem;
import com.programming.tichi.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {


    @Autowired
    OrderRepository orderRepository;
    public void placeOrder(OrderRequest orderRequest){
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto).toList();

        order.setOrderLineItemList(orderLineItems);

        // call inventory Service and place order if product is in
//        stock

        orderRepository.save(order);
    }

    private OrderLineItem mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItem orderLineItem=new OrderLineItem();
        orderLineItem.setPrice(orderLineItemsDto.getPrice());
        orderLineItem.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItem.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItem;
    }
}
