package com.food.ordering.system.order.query.web.client.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderQueryWebClientRequestModel {
    private String id;
    @NotEmpty
    private String text;
}
