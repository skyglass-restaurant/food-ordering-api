package com.food.ordering.system.order.query.web.client.service;


import com.food.ordering.system.order.query.web.client.common.model.OrderQueryWebClientRequestModel;
import com.food.ordering.system.order.query.web.client.common.model.OrderQueryWebClientResponseModel;

import java.util.List;

public interface OrderQueryWebClient {

    List<OrderQueryWebClientResponseModel> getDataByText(OrderQueryWebClientRequestModel requestModel);
}
