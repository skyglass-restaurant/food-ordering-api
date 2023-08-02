package com.food.ordering.system.order.query.service.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderQueryServiceResponseListModel {

    private List<OrderQueryServiceResponseModel> queryResponseModels;
}
