package com.example.joypadjourney.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.joypadjourney.model.Request.RegisterCustomerRequest;
import com.example.joypadjourney.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService; // Mock service dependency

    @Test
    void testRegister() throws Exception {
        // Mock the behavior of the customerService
        Mockito.doNothing().when(customerService).register(Mockito.any());

        // Prepare request object
        RegisterCustomerRequest request = new RegisterCustomerRequest();
        request.setUsername("test");
        request.setEmail("dump@gmail.com");
        request.setPassword("hah");
        request.setFirstName("sp");
        request.setLastName("gtw");
        request.setPhoneNum("082217287504");

        // Perform the test
        mockMvc.perform(post("/api/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
