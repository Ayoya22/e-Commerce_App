package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.appUser;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private PasswordEncoder encoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    private MockHttpServletRequest request(){
//        return new MockHttpServletRequest();
//    }

    @Autowired
    private MockHttpServletRequest request;

    private appUser user;

    private CreateUserRequest userRequest;


    @BeforeEach
    public void init() throws Exception {
        userRequest = new CreateUserRequest();
        userRequest.setUsername("Mert");
        userRequest.setPassword("testPassword");
        userRequest.setConfirmedPassword("testPassword");

        MvcResult entityResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/user/create").content(objectMapper.writeValueAsString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        user = objectMapper.readValue(entityResult.getResponse().getContentAsString(), appUser.class);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/login").content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk()).andReturn();
        request.addParameter("Authorization", result.getResponse().getHeader("Authorization"));

    }

    @Test
    @DisplayName("Methods to test user fetching controllers using unique username and id")
    public void testGetUserWithUsernameAndId() throws Exception {
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{username}", "Mert")
                .accept(MediaType.APPLICATION_JSON).header("Authorization", request.getParameter("Authorization")))
                .andExpect(status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/{username}", "Mert").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{username}", "DemoInput")
                .accept(MediaType.APPLICATION_JSON).header("Authorization",request.getParameter("Authorization")))
                .andExpect(status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/id/{id}", user.getId()).accept(MediaType.APPLICATION_JSON)
                .header("Authorization", request.getParameter("Authorization"))).andExpect(status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/id/{id}", user.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/id/{id}", 100).accept(MediaType.APPLICATION_JSON)
                .header("Authorization", request.getParameter("Authorization"))).andExpect(status().isNotFound());
    }

}
