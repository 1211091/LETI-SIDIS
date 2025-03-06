package leti.sidis.usersapi;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import leti.sidis.users.api.CreateUserRequest;
import leti.sidis.users.api.UserView;
import leti.sidis.users.auth.AuthRequest;
import leti.sidis.users.services.UserService;
import leti.sidis.usersapi.testutils.JsonHelper;
import leti.sidis.usersapi.testutils.UserTestDataFactory;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.junit.jupiter.api.AfterEach;

import java.util.Collections;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;
import static org.assertj.core.api.BDDAssumptions.given;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestAuthApi {

    @Autowired
    private MockMvc mockMvc;
/*    @Mock
    private UserApi userApi;*/
    @Mock
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    //TODO: definir portas
    //private final Dotenv dotenv = Dotenv.load();
    private final String port = "8080";

    @Test
    void testRegisterUser_Successful() throws Exception {

        CreateUserRequest request = new CreateUserRequest();
            request.setUsername("test_user53@email.com");
            request.setFullName("Test User");
            request.setPassword("test_password");
            request.setRePassword("test_password");
            request.setAuthorities(Collections.singleton("NEW_CUSTOMER"));

        String requestBody = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("http://localhost:" + port + "/api/public/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk())
            .andReturn();

        JsonNode responseJson = objectMapper.readTree(result.getResponse().getContentAsString());
        String idValue = responseJson.get("id").asText();
        cleanup(UUID.fromString(idValue));
    }


    void cleanup(UUID createdUserId) {
        userService.delete(createdUserId);
    }

    //




   /* @Test
    void testLoginSuccess() throws Exception {
        final UserView userView = userTestDataFactory
                .createUser(String.format("test.user.%d@nix.io", currentTimeMillis()), "Test User", password);

        final AuthRequest request = new AuthRequest(userView.getUsername(), password);

        final MvcResult createResult = this.mockMvc
                .perform(post("/api/public/login").contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, request)))
                .andExpect(status().isOk()).andExpect(header().exists(HttpHeaders.AUTHORIZATION)).andReturn();

        final UserView authUserView = JsonHelper.fromJson(objectMapper, createResult.getResponse().getContentAsString(),
                UserView.class);
        assertEquals(userView.getId(), authUserView.getId(), "User ids must match!");
    }

    @Test
    void testLoginFail() throws Exception {
        final UserView userView = userTestDataFactory
                .createUser(String.format("test.user.%d@nix.io", currentTimeMillis()), "Test User", password);

        final AuthRequest request = new AuthRequest(userView.getUsername(), "zxc");

        this.mockMvc
                .perform(post("/api/public/login").contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, request)))
                .andExpect(status().isUnauthorized()).andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andReturn();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        final CreateUserRequest goodRequest = new CreateUserRequest(
                String.format("test.user.%d@nix.com", currentTimeMillis()), "Test User A", password);

        final MvcResult createResult = this.mockMvc.perform(post("/api/public/register")
                        .contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(objectMapper, goodRequest)))
                .andExpect(status().isOk()).andReturn();

        final UserView userView = JsonHelper.fromJson(objectMapper, createResult.getResponse().getContentAsString(),
                UserView.class);
        assertNotNull(userView.getId(), "User id must not be null!");
        assertEquals(goodRequest.getFullName(), userView.getFullName(), "User fullname  update isn't applied!");
    }

    @Test
    void testRegisterFail() throws Exception {
        final CreateUserRequest badRequest = new CreateUserRequest("invalid.username", "", "");

        this.mockMvc
                .perform(post("/api/public/register").contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, badRequest)))
                .andExpect(status().isBadRequest()).andExpect(content().string(containsString("must not be blank")));
    }*/

}
