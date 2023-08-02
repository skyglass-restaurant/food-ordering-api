package com.food.ordering.system.order.query.web.client.service.impl;

import com.food.ordering.system.config.OrderQueryWebClientConfigData;
import com.food.ordering.system.order.query.web.client.common.exception.ElasticQueryWebClientException;
import com.food.ordering.system.order.query.web.client.common.model.OrderQueryWebClientResponseModel;
import com.food.ordering.system.order.query.web.client.service.OrderQueryWebClient;
import com.food.ordering.system.order.query.web.client.common.model.OrderQueryWebClientRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.food.ordering.system.mdc.Constants.CORRELATION_ID_HEADER;
import static com.food.ordering.system.mdc.Constants.CORRELATION_ID_KEY;

@Service
public class OrderQueryWebClientImpl implements OrderQueryWebClient {

    private static final Logger LOG = LoggerFactory.getLogger(OrderQueryWebClientImpl.class);

    private final WebClient.Builder webClientBuilder;

    private final OrderQueryWebClientConfigData orderQueryWebClientConfigData;

    public OrderQueryWebClientImpl(@Qualifier("webClientBuilder") WebClient.Builder clientBuilder,
                                   OrderQueryWebClientConfigData webClientConfigData) {
        this.webClientBuilder = clientBuilder;
        this.orderQueryWebClientConfigData = webClientConfigData;
    }

    @Override
    public List<OrderQueryWebClientResponseModel> getDataByText(OrderQueryWebClientRequestModel requestModel) {
        LOG.info("Querying by text {}", requestModel.getText());
        return getWebClient(requestModel)
                .bodyToFlux(OrderQueryWebClientResponseModel.class)
                .log()//Logger
                .collectList()
                .block();
    }

    private WebClient.ResponseSpec getWebClient(OrderQueryWebClientRequestModel requestModel) {
        return webClientBuilder
                .build()
                .method(HttpMethod.valueOf(orderQueryWebClientConfigData.getQueryByText().getMethod()))
                .uri(orderQueryWebClientConfigData.getQueryByText().getUri())
                .accept(MediaType.valueOf(orderQueryWebClientConfigData.getQueryByText().getAccept()))
                .header(CORRELATION_ID_HEADER, MDC.get(CORRELATION_ID_KEY))
                .body(BodyInserters.fromPublisher(Mono.just(requestModel), createParameterizedTypeReference()))
                .retrieve()
                .onStatus(
                        httpStatus -> httpStatus.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not authenticated!")))
                .onStatus(
                        HttpStatus::is4xxClientError,
                        clientResponse -> Mono.just(
                                new ElasticQueryWebClientException(clientResponse.statusCode().getReasonPhrase())))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        clientResponse -> Mono.just(new Exception(clientResponse.statusCode().getReasonPhrase())));
    }


    private <T> ParameterizedTypeReference<T> createParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
