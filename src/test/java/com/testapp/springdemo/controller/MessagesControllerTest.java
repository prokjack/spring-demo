package com.testapp.springdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testapp.springdemo.model.DigestPayload;
import com.testapp.springdemo.service.ConcurrentMapStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({MessagesController.class, ConcurrentMapStorageService.class})
class MessagesControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }



    @Test
    void postMessage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/messages")
                .content(asJsonString(DigestPayload.builder().message("foo").build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.digest").value("2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae"));
    }

    @Test
    void postMessage_should_return_bad_request_if_message_not_provided() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/messages")
                .content(asJsonString(DigestPayload.builder().build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMessageByHash() throws Exception {
        postMessage();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/messages/2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("foo"));;
    }

    @Test
    void getMessageByHash_should_return_404_if_message_not_exist() throws Exception {
        postMessage();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/messages/aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}