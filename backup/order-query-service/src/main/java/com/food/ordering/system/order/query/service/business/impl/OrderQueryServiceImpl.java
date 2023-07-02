package com.food.ordering.system.order.query.service.business.impl;

import com.food.ordering.system.config.OrderQueryServiceConfigData;
import com.food.ordering.system.order.query.service.business.OrderQueryService;
import com.food.ordering.system.order.query.service.common.client.OrderQueryClient;
import com.food.ordering.system.order.query.service.common.exception.ElasticQueryServiceException;
import com.food.ordering.system.order.query.service.common.model.OrderModel;
import com.food.ordering.system.order.query.service.common.model.OrderQueryServiceResponseListModel;
import com.food.ordering.system.order.query.service.common.model.OrderQueryServiceResponseModel;
import com.food.ordering.system.order.query.service.model.assembler.OrderQueryServiceResponseModelAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.food.ordering.system.mdc.Constants.CORRELATION_ID_HEADER;
import static com.food.ordering.system.mdc.Constants.CORRELATION_ID_KEY;

@Service
public class OrderQueryServiceImpl implements OrderQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderQueryServiceImpl.class);

    private final OrderQueryServiceResponseModelAssembler orderQueryServiceResponseModelAssembler;

    private final OrderQueryClient<OrderModel> orderQueryClient;

    private final OrderQueryServiceConfigData orderQueryServiceConfigData;

    private final WebClient.Builder webClientBuilder;

    public OrderQueryServiceImpl(OrderQueryServiceResponseModelAssembler assembler,
                                 OrderQueryClient<OrderModel> queryClient,
                                 OrderQueryServiceConfigData queryServiceConfigData,
                                 @Qualifier("webClientBuilder")
                                              WebClient.Builder clientBuilder) {
        this.orderQueryServiceResponseModelAssembler = assembler;
        this.orderQueryClient = queryClient;
        this.orderQueryServiceConfigData = queryServiceConfigData;
        this.webClientBuilder = clientBuilder;
    }

    @Override
    public OrderQueryServiceResponseModel getDocumentById(String id) {
        LOG.info("Querying Order Search by id {}", id);
        return orderQueryServiceResponseModelAssembler.toModel(orderQueryClient.getModelById(id));
    }

    @Override
    public OrderQueryServiceResponseListModel getDocumentsByText(String text, String accessToken) {
        LOG.info("Querying order search by text {}", text);
        List<OrderQueryServiceResponseModel> orderQueryServiceResponseModels =
                orderQueryServiceResponseModelAssembler.toModels(orderQueryClient.getModelByText(text));

        return OrderQueryServiceResponseListModel.builder()
                .queryResponseModels(getResponseList(text, accessToken))
                .build();
    }

    @Override
    public List<OrderQueryServiceResponseModel> getAllDocuments() {
        LOG.info("Querying all documents in elasticsearch");
        return orderQueryServiceResponseModelAssembler.toModels(orderQueryClient.getAllModels());
    }

    private List<OrderQueryServiceResponseModel> getResponseList(String text, String accessToken) {
        OrderQueryServiceConfigData.Query query =
                orderQueryServiceConfigData.getQuery();
        return retrieveResponseModels(text, accessToken, query);
    }

    private List<OrderQueryServiceResponseModel> retrieveResponseModels(String text,
                                                                            String accessToken,
                                                                            OrderQueryServiceConfigData.Query query) {
        return webClientBuilder
                .build()
                .method(HttpMethod.valueOf(query.getMethod()))
                .uri(query.getUri(), uriBuilder -> uriBuilder.build(text))
                .headers(h -> {
                    h.setBearerAuth(accessToken);
                    h.set(CORRELATION_ID_HEADER, MDC.get(CORRELATION_ID_KEY));
                })
                .accept(MediaType.valueOf(query.getAccept()))
                .retrieve()
                .onStatus(
                        s -> s.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not authenticated")))
                .onStatus(
                        HttpStatus::is4xxClientError,
                        clientResponse -> Mono.just(new
                                ElasticQueryServiceException(clientResponse.statusCode().getReasonPhrase())))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        clientResponse -> Mono.just(new Exception(clientResponse.statusCode().getReasonPhrase())))
                .bodyToFlux(OrderQueryServiceResponseModel.class)
                .collectList()
                .log()
                .block();

    }
}
