package com.food.ordering.system.order.query.service.model;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderQueryServiceResponseModelV2 extends RepresentationModel<OrderQueryServiceResponseModelV2> {
    private Long id;
    private Long userId;
    private String text;
    private String text2;
}
