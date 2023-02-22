package huberts.spring.forumapp.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.ContainerIT;
import huberts.spring.forumapp.exception.*;
import huberts.spring.forumapp.jwt.JwtKey;
import huberts.spring.forumapp.user.dto.PasswordDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class UserControllerTest extends ContainerIT {

    private static final String USERNAME_USER_JWT = "userJwt";
    private static final String USERNAME_MODERATOR_JWT = "moderatorJwt";
    private static final String USERNAME_ADMIN_JWT = "adminJwt";
    private static final String USERNAME_USER = "user";

    private static final String PASSWORD_NEW = "testPassword";
    private static final String PASSWORD_EMPTY = "";

    private static final String LIST_OF_STUFF_USERS_ENDPOINT = "/stuff";

    private static final String USER_ENDPOINT = "/user";
    private static final String USER_NOT_EXIST_PROFILE_ENDPOINT = "/userNotExist";

    private static final String CURRENT_USER_PROFILE_ENDPOINT = "/user/profile";

    private static final String CHANGE_PASSWORD_ENDPOINT = "/user/change-password";

    private static final String DELETE_CURRENT_USER_ENDPOINT = "/user/delete-account";

    private static final String GET_EVERY_USER = "/moderator/get-all";

    private static final String BAN_USER_ENDPOINT = "/moderator/users/userToBan/ban";
    private static final String BAN_USER_DOESNT_EXIST_ENDPOINT = "/moderator/users/userDoesntExist/ban";
    private static final String BAN_USER_BANNED_ENDPOINT = "/moderator/users/userToCheckBan/ban";

    private static final String UNBAN_USER_ENDPOINT = "/moderator/users/userToUnban/unban";
    private static final String UNBAN_USER_DOESNT_EXIST_ENDPOINT = "/moderator/users/userDoesntExist/unban";
    private static final String UNBAN_USER_UNBANNED_ENDPOINT = "/moderator/users/userToCheckUnban/unban";

    private static final String CHANGE_USERS_PASSWORD_ENDPOINT = "/moderator/users/userToChangePassword/password";
    private static final String CHANGE_USERS_PASSWORD_USER_DOESNT_EXIST_ENDPOINT = "/moderator/users/userDoesntExist/password";

    private static final String DELETE_USER_ENDPOINT = "/admin/users/userToDeleteByAdmin/delete";
    private static final String DELETE_USER_DOESNT_EXIST_ENDPOINT = "/admin/users/userDoesntExist/delete";

    private static final String CHANGE_ROLE_ENDPOINT = "/admin/users/userToChangeRole/change-role/ROLE_MODERATOR";
    private static final String CHANGE_ROLE_DUPLICATE_ENDPOINT = "/admin/users/userWithSameRole/change-role/ROLE_USER";
    private static final String CHANGE_ROLE_USER_DOESNT_EXIST_ENDPOINT = "/admin/users/userDoesntExist/change-role/ROLE_USER";
    private static final String CHANGE_ROLE_DOESNT_EXIST_ENDPOINT = "/admin/users/userToChangeRole/change-role/ROLE_INVALID";

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String INVALID_JWT = "wrong_token_123";

    private static final String USERNAME_JSON_PATH = "$.username";
    private static final String USERNAME_1_JSON_PATH = "$.[0].username";
    private static final String USERNAME_2_JSON_PATH = "$.[1].username";
    private static final String USERNAME_3_JSON_PATH = "$.[2].username";
    private static final String BLOCKED_JSON_PATH = "$.blocked";


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("should return user")
    @Test
    void shouldReturnUserByUsername() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_ENDPOINT))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(USERNAME_JSON_PATH).value(USERNAME_USER));
    }

    @DisplayName("should not return user when user doesnt exist")
    @Test
    void shouldNotReturnUserThatDoesntExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_NOT_EXIST_PROFILE_ENDPOINT))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserDoesntExistException));
    }

    @DisplayName("return list of users with moderator and admin role")
    @Test
    void shouldReturnEveryStaffUser_MembersWithRoleModeratorAndAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(LIST_OF_STUFF_USERS_ENDPOINT))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(USERNAME_1_JSON_PATH).value(USERNAME_ADMIN_JWT))
                .andExpect(MockMvcResultMatchers.jsonPath(USERNAME_2_JSON_PATH).value(USERNAME_MODERATOR_JWT));
    }

    @DisplayName("return user by jwt authentication")
    @Test
    void shouldReturnCurrentUser() throws Exception {
        String token = JwtKey.getUserJwt(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.get(CURRENT_USER_PROFILE_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(USERNAME_JSON_PATH).value(USERNAME_USER_JWT));
    }

    @DisplayName("doesnt return user when invalid jwt authentication")
    @Test
    void shouldNotReturnCurrentUser_JwtIsWrong() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(CURRENT_USER_PROFILE_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, INVALID_JWT))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @DisplayName("should change password by given path variable and requested password")
    @Test
    void shouldChangePasswordForCurrentUser() throws Exception {
        String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);

        PasswordDTO newPassword = new PasswordDTO(PASSWORD_NEW);

        String jsonPassword = objectMapper.writeValueAsString(newPassword);

        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGE_PASSWORD_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, userToken)
                        .contentType(APPLICATION_JSON)
                        .content(jsonPassword))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(USERNAME_JSON_PATH).value(USERNAME_USER_JWT));
    }

    @DisplayName("Should not change password when invalid jwt authentication and password is empty")
    @Test
    void shouldNotChangePasswordForCurrentUser_WrongParam_WrongJWT() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGE_PASSWORD_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, INVALID_JWT))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("should not change password when invalid jwt authentication")
    @Test
    void shouldNotChangePasswordForCurrentUser_GoodParam_WrongJWT() throws Exception {
        PasswordDTO newPassword = new PasswordDTO(PASSWORD_NEW);
        String jsonPassword = objectMapper.writeValueAsString(newPassword);

        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGE_PASSWORD_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, INVALID_JWT)
                        .contentType(APPLICATION_JSON)
                        .content(jsonPassword))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @DisplayName("should not change password when password is blank")
    @Test
    void shouldNotChangePasswordForCurrentUser_WrongParam_GoodJwt() throws Exception {
        String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);

        PasswordDTO password = new PasswordDTO(PASSWORD_EMPTY);
        String jsonPassword = objectMapper.writeValueAsString(password);

        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGE_PASSWORD_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, userToken)
                        .contentType(APPLICATION_JSON)
                        .content(jsonPassword))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameOrPasswordIsBlankOrEmpty));

    }

    @DisplayName("should delete user by given path variable")
    @Test
    void shouldDeleteCurrentUser() throws Exception {
        String userToken = JwtKey.getUserToDeleteJwt(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_CURRENT_USER_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, userToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("should not delete user when invalid jwt authentication")
    @Test
    void shouldNotDeleteCurrentUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_CURRENT_USER_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, INVALID_JWT))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @DisplayName("should return a list of all users when requested by a moderator")
    @Test
    void shouldReturnEveryUser() throws Exception {
        String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.get(GET_EVERY_USER)
                        .header(AUTHORIZATION_HEADER, moderatorToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(USERNAME_1_JSON_PATH).value(USERNAME_USER_JWT))
                .andExpect(MockMvcResultMatchers.jsonPath(USERNAME_2_JSON_PATH).value(USERNAME_ADMIN_JWT))
                .andExpect(MockMvcResultMatchers.jsonPath(USERNAME_3_JSON_PATH).value(USERNAME_MODERATOR_JWT));
    }

    @DisplayName("should ban user by given path variable")
    @Test
    void shouldBanUser() throws Exception {
        String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.patch(BAN_USER_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, moderatorToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(BLOCKED_JSON_PATH).value(true));
    }

    @DisplayName("should not ban user when invalid jwt authentication")
    @Test
    void shouldNotBanUser_InvalidJwt() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(BAN_USER_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, INVALID_JWT))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @DisplayName("should not ban user when user doesnt exist")
    @Test
    void shouldNotBanUser_UserDoesntExist() throws Exception {
        String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.patch(BAN_USER_DOESNT_EXIST_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, moderatorToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserDoesntExistException));
    }

    @DisplayName("should not ban user when given user is already banned")
    @Test
    void shouldNotBanUser_UserIsBanned() throws Exception {
        String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.patch(BAN_USER_BANNED_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, moderatorToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserBlockException));
    }

    @DisplayName("should unban user by given path variable")
    @Test
    void shouldUnbanUser() throws Exception {
        String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.patch(UNBAN_USER_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, moderatorToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(BLOCKED_JSON_PATH).value(false));
    }

    @DisplayName("should not unban user when doesnt exist")
    @Test
    void shouldNotUnbanUser_UserDoesntExist() throws Exception {
        String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.patch(UNBAN_USER_DOESNT_EXIST_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, moderatorToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserDoesntExistException));
    }

    @DisplayName("should not unban user when user is not banned")
    @Test
    void shouldNotUnbanUser_UserIsUnbanned() throws Exception {
        String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.patch(UNBAN_USER_UNBANNED_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, moderatorToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserBlockException));
    }

    @DisplayName("should change password by user in path variable and requested password")
    @Test
    void shouldChangePassword() throws Exception {
        String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

        PasswordDTO password = new PasswordDTO(PASSWORD_NEW);
        String jsonPassword = objectMapper.writeValueAsString(password);

        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGE_USERS_PASSWORD_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, moderatorToken)
                        .contentType(APPLICATION_JSON)
                        .content(jsonPassword))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("should not change password when user doesnt exist")
    @Test
    void shouldNotChangePassword_UserDoesntExist() throws Exception {
        String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

        PasswordDTO password = new PasswordDTO(PASSWORD_NEW);
        String jsonPassword = objectMapper.writeValueAsString(password);

        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGE_USERS_PASSWORD_USER_DOESNT_EXIST_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, moderatorToken)
                        .contentType(APPLICATION_JSON)
                        .content(jsonPassword))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserDoesntExistException));
    }

    @DisplayName("should not change password when password is blank")
    @Test
    void shouldNotChangePassword_PasswordIsBlank() throws Exception {
        String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

        PasswordDTO password = new PasswordDTO(PASSWORD_EMPTY);
        String jsonPassword = objectMapper.writeValueAsString(password);

        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGE_USERS_PASSWORD_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, moderatorToken)
                        .contentType(APPLICATION_JSON)
                        .content(jsonPassword))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameOrPasswordIsBlankOrEmpty));
    }

    @DisplayName("should not change password when invalid jwt authentication")
    @Test
    void shouldNotChangePassword_WhenInvalidAuthentication() throws Exception {
        PasswordDTO password = new PasswordDTO(PASSWORD_NEW);
        String jsonPassword = objectMapper.writeValueAsString(password);

        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGE_USERS_PASSWORD_USER_DOESNT_EXIST_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, INVALID_JWT)
                        .contentType(APPLICATION_JSON)
                        .content(jsonPassword))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @DisplayName("should delete user by given path variable and authenticated admin")
    @Test
    void shouldDeleteUser() throws Exception {
        String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_USER_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("should not delete user when user doesnt exist")
    @Test
    void shouldNotDeleteUser_UserDoesntExist() throws Exception {
        String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_USER_DOESNT_EXIST_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, adminToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserDoesntExistException));
    }

    @DisplayName("should not delete user when invalid jwt authentication")
    @Test
    void shouldNotDeleteUser_WrongJWT() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_USER_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, INVALID_JWT))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @DisplayName("should change user role by given path variable and authenticated admin")
    @Test
    void shouldChangeUserRole() throws Exception {
        String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGE_ROLE_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("should not change user role when user already has that role")
    @Test
    void shouldNotChangeUserRole_SameRole() throws Exception {
        String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGE_ROLE_DUPLICATE_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, adminToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RoleException));
    }

    @DisplayName("should not change user role when user doesnt exist")
    @Test
    void shouldNotChangeUserRole_UserDoesntExist() throws Exception {
        String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGE_ROLE_USER_DOESNT_EXIST_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, adminToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserDoesntExistException));
    }

    @DisplayName("should not change user role when role doesnt exist")
    @Test
    void shouldNotChangeUserRole_RoleDoesntExist() throws Exception {
        String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGE_ROLE_DOESNT_EXIST_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, adminToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RoleDoesntExistException));
    }

    @DisplayName("should not change user role when invalid jwt authentication")
    @Test
    void shouldNotChangeUserRole_InvalidJWT() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(CHANGE_ROLE_ENDPOINT)
                        .header(AUTHORIZATION_HEADER, INVALID_JWT))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}