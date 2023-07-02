package com.food.ordering.system.order.query.service.api;

import com.food.ordering.system.order.query.service.common.model.OrderQueryServiceResponseListModel;
import com.food.ordering.system.order.query.service.model.OrderQueryServiceResponseModelV2;
import com.food.ordering.system.order.query.service.security.OrderQueryUser;
import com.food.ordering.system.order.query.service.business.OrderQueryService;
import com.food.ordering.system.order.query.service.common.model.OrderQueryServiceRequestModel;
import com.food.ordering.system.order.query.service.common.model.OrderQueryServiceResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(value = "/documents", produces = "application/vnd.api.v1+json")
public class OrderDocumentController {
    private static final Logger LOG = LoggerFactory.getLogger(OrderDocumentController.class);

    private final OrderQueryService orderQueryService;

    public OrderDocumentController(OrderQueryService queryService) {
        this.orderQueryService = queryService;
    }

    @Value("${server.port}")
    private String port;

    @PostAuthorize("hasPermission(returnObject, 'READ')")
    @Operation(summary = "Get all order documents.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response.", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = OrderQueryServiceResponseModel.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("")
    public @ResponseBody
    ResponseEntity<List<OrderQueryServiceResponseModel>> getAllDocuments() {
        List<OrderQueryServiceResponseModel> response = orderQueryService.getAllDocuments();
        LOG.info("order search returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasPermission(#id, 'OrderQueryServiceResponseModel','READ')")
    @Operation(summary = "Get order document by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response.", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = OrderQueryServiceResponseModel.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<OrderQueryServiceResponseModel>
    getDocumentById(@PathVariable @NotEmpty String id) {
        OrderQueryServiceResponseModel orderQueryServiceResponseModel = orderQueryService.getDocumentById(id);
        LOG.debug("order search returned document with id {} on port {}", id, port);
        return ResponseEntity.ok(orderQueryServiceResponseModel);
    }

    @Operation(summary = "Get order document by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response.", content = {
                    @Content(mediaType = "application/vnd.api.v2+json",
                            schema = @Schema(implementation = OrderQueryServiceResponseModelV2.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping(value = "/{id}", produces = "application/vnd.api.v2+json")
    public @ResponseBody
    ResponseEntity<OrderQueryServiceResponseModelV2>
    getDocumentByIdV2(@PathVariable @NotEmpty String id) {
        OrderQueryServiceResponseModel orderQueryServiceResponseModel = orderQueryService.getDocumentById(id);
        OrderQueryServiceResponseModelV2 responseModelV2 = getV2Model(orderQueryServiceResponseModel);
        LOG.debug("order search returned document with id {} on port {}", id, port);
        return ResponseEntity.ok(responseModelV2);
    }


    @PreAuthorize("hasRole('APP_USER_ROLE') || hasRole('APP_SUPER_USER_ROLE') || hasAuthority('SCOPE_APP_USER_ROLE')")
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    @Operation(summary = "Get order document by text.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response.", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = OrderQueryServiceResponseModel.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PostMapping("/get-document-by-text")
    public @ResponseBody
    ResponseEntity<OrderQueryServiceResponseListModel>
    getDocumentByText(@RequestBody @Valid OrderQueryServiceRequestModel orderQueryServiceRequestModel,
                      @AuthenticationPrincipal OrderQueryUser principal,
                      @RegisteredOAuth2AuthorizedClient("keycloak")
                              OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        LOG.info("User {} querying documents for text {}", principal.getUsername(),
                orderQueryServiceRequestModel.getText());

        OrderQueryServiceResponseListModel response =
                orderQueryService.getDocumentsByText(orderQueryServiceRequestModel.getText(),
                        oAuth2AuthorizedClient.getAccessToken().getTokenValue());
        LOG.info("Elasticsearch returned {} of documents on port {}",
                response.getQueryResponseModels().size(), port);
        return ResponseEntity.ok(response);
    }

    private OrderQueryServiceResponseModelV2 getV2Model(OrderQueryServiceResponseModel responseModel) {
        OrderQueryServiceResponseModelV2 responseModelV2 = OrderQueryServiceResponseModelV2.builder()
                .id(Long.parseLong(responseModel.getId()))
                .userId(responseModel.getUserId())
                .text(responseModel.getText())
                .text2("Version 2 text")
                .build();
        responseModelV2.add(responseModel.getLinks());
        return responseModelV2;

    }


}
