package com.food.ordering.system.order.query.service.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderQueryServiceRequestModel {
    private String id;
    @NotEmpty
    private String text;
}
