package com.food.ordering.system.order.query.service.common.transformer;

import com.food.ordering.system.order.query.service.common.model.OrderModel;
import com.food.ordering.system.order.query.service.common.model.OrderQueryServiceResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderToResponseModelTransformer {

    public OrderQueryServiceResponseModel getResponseModel(OrderModel orderModel) {
        return OrderQueryServiceResponseModel
                .builder()
                .id(orderModel.getId())
                .userId(orderModel.getUserId())
                .text(orderModel.getText())
                .createdAt(orderModel.getCreatedAt())
                .build();
    }

    public List<OrderQueryServiceResponseModel> getResponseModels(List<OrderModel> orderModels) {
        return orderModels.stream().map(this::getResponseModel).collect(Collectors.toList());
    }
}
