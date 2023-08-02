package com.food.ordering.system.order.query.service.common.model;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderQueryServiceResponseModel extends RepresentationModel<OrderQueryServiceResponseModel> {
    private String id;
    private Long userId;
    private String text;
    private ZonedDateTime createdAt;
}
