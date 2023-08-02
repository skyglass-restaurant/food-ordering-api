package com.food.ordering.system.order.query.service.business;

import com.food.ordering.system.order.query.service.common.model.OrderQueryServiceResponseListModel;
import com.food.ordering.system.order.query.service.common.model.OrderQueryServiceResponseModel;

import java.util.List;

public interface OrderQueryService {

    OrderQueryServiceResponseModel getDocumentById(String id);

    OrderQueryServiceResponseListModel getDocumentsByText(String text, String accessToken);

    List<OrderQueryServiceResponseModel> getAllDocuments();
}
