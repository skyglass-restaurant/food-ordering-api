package com.food.ordering.system.order.query.service.common.client;

import com.food.ordering.system.order.query.service.common.model.OrderModel;

import java.util.List;

public interface OrderQueryClient<T extends OrderModel> {

    T getModelById(String id);

    List<T> getModelByText(String text);

    List<T> getAllModels();
}

