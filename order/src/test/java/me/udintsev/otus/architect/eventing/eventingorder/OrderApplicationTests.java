package me.udintsev.otus.architect.eventing.eventingorder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderApplicationTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldCreateGetAndListOrders() throws Exception {
        // Create
        OrderController.CreateOrderRequest createOrderRequest = new OrderController.CreateOrderRequest(List.of(
                new OrderItem(10, 1),
                new OrderItem(11, 5)
        ));

        var responseContent = mvc.perform(
                post(OrderController.API_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("userId").isNumber())
                .andExpect(jsonPath("status").value("created"))
                .andExpect(jsonPath("items").isArray())
                .andExpect(jsonPath("items.length()").value(2))
                .andExpect(jsonPath("items[0].itemId").isNumber())
                .andExpect(jsonPath("items[0].quantity").isNumber())
                .andReturn().getResponse().getContentAsByteArray();

        Order order = objectMapper.readValue(responseContent, Order.class);

        // Get
        mvc.perform(get(OrderController.API_ROOT + "/{orderId}", order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(order.getId()))
                .andExpect(jsonPath("userId").value(order.getUserId()))
                .andExpect(jsonPath("items[0].itemId").value(Matchers.anyOf(
                        Matchers.equalTo(10), Matchers.equalTo(11))));

        // List
        mvc.perform(get(OrderController.API_ROOT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(order.getId()));
    }
}
