package com.food.ordering.system.order.query.web.client.api;

import com.food.ordering.system.order.query.web.client.common.model.OrderQueryWebClientRequestModel;
import com.food.ordering.system.order.query.web.client.common.model.OrderQueryWebClientResponseModel;
import com.food.ordering.system.order.query.web.client.service.OrderQueryWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class OrderQueryController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderQueryController.class);

    private final OrderQueryWebClient orderQueryWebClient;

    public OrderQueryController(OrderQueryWebClient webClient) {
        this.orderQueryWebClient = webClient;
    }

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("orderQueryWebClientRequestModel",
                OrderQueryWebClientRequestModel.builder().build());
        return "home";
    }

    @PostMapping("/query-by-text")
    public String queryByText(@Valid OrderQueryWebClientRequestModel requestModel,
                              Model model) {
        LOG.info("Querying with text {}", requestModel.getText());
        List<OrderQueryWebClientResponseModel> responseModels = orderQueryWebClient.getDataByText(requestModel);
        model.addAttribute("orderQueryWebClientResponseModels",
                responseModels);
        model.addAttribute("searchText", requestModel.getText());
        model.addAttribute("orderQueryWebClientRequestModel",
                OrderQueryWebClientRequestModel.builder().build());
        return "home";
    }

}
