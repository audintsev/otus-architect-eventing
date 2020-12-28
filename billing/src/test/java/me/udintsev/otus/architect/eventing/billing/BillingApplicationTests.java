package me.udintsev.otus.architect.eventing.billing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BillingApplicationTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void depositCreatesAccount() throws Exception {
        AccountController.Request depositRequest = new AccountController.Request(10);

        mvc.perform(
                post(AccountController.API_ROOT + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "john.doe@example.com")
                        .content(objectMapper.writeValueAsString(depositRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").value("john.doe@example.com"))
                .andExpect(jsonPath("amount").value(10));

        mvc.perform(
                get(AccountController.API_ROOT)
                        .header("X-User-Id", "john.doe@example.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").value("john.doe@example.com"))
                .andExpect(jsonPath("amount").value(10));
    }
}
