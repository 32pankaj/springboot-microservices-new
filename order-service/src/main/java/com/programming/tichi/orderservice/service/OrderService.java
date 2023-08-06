package com.programming.tichi.orderservice.service;

import com.programming.tichi.orderservice.dto.OrderLineItemsDto;
import com.programming.tichi.orderservice.dto.OrderRequest;
import com.programming.tichi.orderservice.model.Order;
import com.programming.tichi.orderservice.model.OrderLineItem;
import com.programming.tichi.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {


    @Autowired
    OrderRepository orderRepository;

    @Autowired
    WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto).toList();

        order.setOrderLineItemList(orderLineItems);
        List<String>skuCodes=order.getOrderLineItemList().stream().map(orderLineItem -> orderLineItem.getSkuCode()).toList();

        // call inventory Service and place order if product is in
        //        stock

        Boolean Result = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        UriBuilder->UriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Result) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is  not in stock , please try again");
        }

    }

    private OrderLineItem mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(orderLineItemsDto.getPrice());
        orderLineItem.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItem.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItem;
    }
}
