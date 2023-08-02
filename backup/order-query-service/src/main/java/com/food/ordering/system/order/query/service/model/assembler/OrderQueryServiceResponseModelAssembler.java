package com.food.ordering.system.order.query.service.model.assembler;

import com.food.ordering.system.order.query.service.common.model.OrderModel;
import com.food.ordering.system.order.query.service.api.OrderDocumentController;
import com.food.ordering.system.order.query.service.common.model.OrderQueryServiceResponseModel;
import com.food.ordering.system.order.query.service.common.transformer.OrderToResponseModelTransformer;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderQueryServiceResponseModelAssembler
        extends RepresentationModelAssemblerSupport<OrderModel, OrderQueryServiceResponseModel> {

    private final OrderToResponseModelTransformer elasticToResponseModelTransformer;

    public OrderQueryServiceResponseModelAssembler(OrderToResponseModelTransformer transformer) {
        super(OrderDocumentController.class, OrderQueryServiceResponseModel.class);
        this.elasticToResponseModelTransformer = transformer;
    }

    @Override
    public OrderQueryServiceResponseModel toModel(OrderModel orderModel) {
        OrderQueryServiceResponseModel responseModel =
                elasticToResponseModelTransformer.getResponseModel(orderModel);
        responseModel.add(
                linkTo(methodOn(OrderDocumentController.class)
                        .getDocumentById((orderModel.getId())))
                        .withSelfRel());
        responseModel.add(
                linkTo(OrderDocumentController.class)
                        .withRel("documents"));
        return responseModel;
    }

    public List<OrderQueryServiceResponseModel> toModels(List<OrderModel> orderModels) {
        return orderModels.stream().map(this::toModel).collect(Collectors.toList());
    }


}

