package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.appUser;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest2 {
    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setUp() {
        userController = new UserController();
        TestUtilities.injectObjects(userController, "userRepository", userRepo);
        TestUtilities.injectObjects(userController, "cartRepository",cartRepository);
        TestUtilities.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {

        when(encoder.encode("mertPassword")).thenReturn("thisIsHashed");
        CreateUserRequest mert = new CreateUserRequest();
        mert.setUsername("mert");
        mert.setPassword("mertPassword");
        mert.setConfirmedPassword("mertPassword");
        final ResponseEntity<appUser> response = userController.createUser(mert);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        appUser union = response.getBody();
        assertNotNull(union);
        assertEquals(0,union.getId());
        assertEquals("mert", union.getUsername());
        assertEquals("thisIsHashed", union.getPassword());
    }
}


