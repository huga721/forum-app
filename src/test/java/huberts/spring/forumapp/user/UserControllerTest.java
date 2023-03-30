package huberts.spring.forumapp.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.ContainerIT;
import huberts.spring.forumapp.exception.role.RoleDoesntExistException;
import huberts.spring.forumapp.exception.role.RoleException;
import huberts.spring.forumapp.exception.user.UserAdminDeleteException;
import huberts.spring.forumapp.exception.user.UserBlockException;
import huberts.spring.forumapp.exception.user.UserDoesntExistException;
import huberts.spring.forumapp.jwt.JwtKey;
import huberts.spring.forumapp.user.dto.PasswordDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UserControllerTest extends ContainerIT {

    private static final String USER_JWT = "userJwt";
    private static final String MODERATOR_JWT = "moderatorJwt";
    private static final String ADMIN_JWT = "adminJwt";
    private static final String USER = "user";
    private static final String PASSWORD_NEW = "testPassword";
    private static final String EMPTY = "";

    private static final String STAFF_ENDPOINT = "/staff";
    private static final String USER_ENDPOINT = "/user";
    private static final String USER_NOT_EXIST_ENDPOINT = "/userNotExist";
    private static final String CURRENT_USER_PROFILE_ENDPOINT = "/profile";
    private static final String CHANGE_PASSWORD_ENDPOINT = "/change-password";
    private static final String DELETE_CURRENT_USER_ENDPOINT = "/delete";
    private static final String GET_EVERY_USER_ENDPOINT = "/moderator/all";
    private static final String BAN_USER_ENDPOINT = "/moderator/ban/userToBan";
    private static final String BAN_USER_DOESNT_EXIST_ENDPOINT = "/moderator/ban/userDoesntExist";
    private static final String BAN_USER_BANNED_ENDPOINT = "/moderator/ban/userToCheckBan";
    private static final String UNBAN_USER_ENDPOINT = "/moderator/unban/userToUnban";
    private static final String UNBAN_USER_DOESNT_EXIST_ENDPOINT = "/moderator/unban/userDoesntExist";
    private static final String UNBAN_USER_UNBANNED_ENDPOINT = "/moderator/unban/userToCheckUnban";
    private static final String CHANGE_USERS_PASSWORD_ENDPOINT = "/moderator/change-password/userToChangePassword";
    private static final String CHANGE_USERS_PASSWORD_USER_DOESNT_EXIST_ENDPOINT = "/moderator/change-password/userDoesntExist";
    private static final String DELETE_USER_ENDPOINT = "/admin/delete/userToDeleteByAdmin";
    private static final String DELETE_USER_DOESNT_EXIST_ENDPOINT = "/admin/delete/userDoesntExist";
    private static final String DELETE_USER_MODERATOR_ENDPOINT = "/admin/delete/moderatorJwt";
    private static final String CHANGE_ROLE_ENDPOINT = "/admin/edit/userToChangeRole/role/ROLE_MODERATOR";
    private static final String CHANGE_ROLE_DUPLICATE_ENDPOINT = "/admin/edit/userWithSameRole/role/ROLE_USER";
    private static final String CHANGE_ROLE_USER_DOESNT_EXIST_ENDPOINT = "/admin/edit/userDoesntExist/role/ROLE_USER";
    private static final String CHANGE_ROLE_DOESNT_EXIST_ENDPOINT = "/admin/edit/userToChangeRole/role/ROLE_INVALID";

    private static final String USERNAME_JSON_PATH = "$.username";
    private static final String USERNAME_ARRAY_0_JSON_PATH = "$.[0].username";
    private static final String USERNAME_ARRAY_1_JSON_PATH = "$.[1].username";
    private static final String USERNAME_ARRAY_2_JSON_PATH = "$.[2].username";
    private static final String BLOCKED_JSON_PATH = "$.blocked";

    private static final String AUTHORIZATION = "Authorization";
    private static final String INVALID_TOKEN = "wrong_token_123";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private PasswordDTO newPassword;
    private PasswordDTO emptyPassword;

    @BeforeEach
    void setUp() {
        newPassword = new PasswordDTO(PASSWORD_NEW);
        emptyPassword = new PasswordDTO(EMPTY);
    }

    @DisplayName("get /staff endpoint")
    @Nested
    class StaffEndpointTests {

        @DisplayName("Should return list of users with moderator and admin role")
        @Test
        void shouldReturnListOfUsersWithModeratorAndAdminRole() throws Exception {
            mockMvc.perform(get(STAFF_ENDPOINT))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(USERNAME_ARRAY_0_JSON_PATH).value(ADMIN_JWT))
                    .andExpect(jsonPath(USERNAME_ARRAY_1_JSON_PATH).value(MODERATOR_JWT));
        }
    }

    @DisplayName("get /{username} endpoint")
    @Nested
    class UsernameEndpointTests {

        @DisplayName("Should return user, HTTP status 200")
        @Test
        void shouldReturnUser() throws Exception {
            mockMvc.perform(get(USER_ENDPOINT))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(USERNAME_JSON_PATH).value(USER));
        }

        @DisplayName("Should throw UserDoesntExistException when user doesn't exist, HTTP status 400")
        @Test
        void shouldThrowUserDoesntExistException_WhenUserDoesntExist() throws Exception {
            mockMvc.perform(get(USER_NOT_EXIST_ENDPOINT))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserDoesntExistException));
        }
    }

    @DisplayName("get /profile endpoint")
    @Nested
    class ProfileEndpointTests {

        @DisplayName("Should return current user, HTTP status 200")
        @Test
        void shouldReturnCurrentUser() throws Exception {
            String moderatorToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(get(CURRENT_USER_PROFILE_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(USERNAME_JSON_PATH).value(ADMIN_JWT));
        }

        @DisplayName("Should not return current user when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotReturnCurrentUser_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(get(CURRENT_USER_PROFILE_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("patch /change-password endpoint")
    @Nested
    class ChangePasswordEndpointTests {

        @DisplayName("Should change password, HTTP status 200")
        @Test
        void shouldChangePassword() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            String jsonPassword = objectMapper.writeValueAsString(newPassword);

            mockMvc.perform(patch(CHANGE_PASSWORD_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(APPLICATION_JSON)
                            .content(jsonPassword))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(USERNAME_JSON_PATH).value(USER_JWT));
        }

        @DisplayName("Should not change password when invalid JWT authentication and password is empty, HTTP status 400")
        @Test
        void shouldNotChangePasswordForCurrentUser_WrongParam_WrongJWT() throws Exception {
            mockMvc.perform(patch(CHANGE_PASSWORD_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(400));
        }

        @DisplayName("Should not change password when invalid JWT authentication, HTTP status 401")
        @Test
        void shouldNotChangePasswordForCurrentUser_GoodParam_WrongJWT() throws Exception {
            String jsonPassword = objectMapper.writeValueAsString(newPassword);

            mockMvc.perform(patch(CHANGE_PASSWORD_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(APPLICATION_JSON)
                            .content(jsonPassword))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should throw MethodArgumentNotValidException when password is empty, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenPasswordIsEmpty() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            String jsonPassword = objectMapper.writeValueAsString(emptyPassword);

            mockMvc.perform(patch(CHANGE_PASSWORD_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(APPLICATION_JSON)
                            .content(jsonPassword))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }
    }

    @DisplayName("delete /delete endpoint")
    @Nested
    class DeleteEndpointTests {

        @DisplayName("Should delete user, HTTP status 200")
        @Test
        void shouldDeleteCurrentUser() throws Exception {
            String userToken = JwtKey.getUserToDeleteJwt(mockMvc, objectMapper);

            mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_CURRENT_USER_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(200));
        }

        @DisplayName("Should not delete user when invalid JWT, HTTP status 401")
        @Test
        void shouldNotDeleteCurrentUser() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_CURRENT_USER_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not delete authenticated moderator, HTTP status 403")
        @Test
        void shouldNotDeleteAuthenticatedModerator() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_CURRENT_USER_ENDPOINT)
                    .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(403))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserAdminDeleteException));
        }
    }

    @DisplayName("get /moderator/all endpoint")
    @Nested
    class ModeratorAllTests {

        @DisplayName("Should return every user")
        @Test
        void shouldReturnEveryUser() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(get(GET_EVERY_USER_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(USERNAME_ARRAY_0_JSON_PATH).value(USER_JWT))
                    .andExpect(jsonPath(USERNAME_ARRAY_1_JSON_PATH).value(ADMIN_JWT))
                    .andExpect(jsonPath(USERNAME_ARRAY_2_JSON_PATH).value(MODERATOR_JWT));
        }

        @DisplayName("Should not return every user when requested by user role, HTTP status 403")
        @Test
        void shouldNotReturnEveryUser_WhenRequestedByUserRole() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);

            mockMvc.perform(get(GET_EVERY_USER_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not return every user when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotReturnEveryUser_WhenJwtIsWrong() throws Exception {
            mockMvc.perform(get(GET_EVERY_USER_ENDPOINT)
                    .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("patch /moderator/ban/{username} endpoint")
    @Nested
    class ModeratorBanUsernameTests {

        @DisplayName("Should ban user, HTTP status 200")
        @Test
        void shouldBanUser() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(BAN_USER_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(BLOCKED_JSON_PATH).value(true));
        }

        @DisplayName("Should not ban user when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotBanUser_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(patch(BAN_USER_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not ban user when requested by user role, HTTP status 403")
        @Test
        void shouldNotBanUser_WhenRequestedByUserRole() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(BAN_USER_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not ban user when user doesn't exist, HTTP status 400")
        @Test
        void shouldNotBanUser_WhenUserDoesntExist() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(BAN_USER_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserDoesntExistException));
        }

        @DisplayName("Should not ban user when user is already banned, HTTP status 400")
        @Test
        void shouldNotBanUser_WhenUserIsAlreadyBanned() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(BAN_USER_BANNED_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserBlockException));
        }
    }

    @DisplayName("patch /moderator/unban/{username} endpoint")
    @Nested
    class ModeratorUnbanUsernameTests {

        @DisplayName("Should unban user, HTTP status 200")
        @Test
        void shouldUnbanUser() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(UNBAN_USER_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(BLOCKED_JSON_PATH).value(false));
        }

        @DisplayName("Should throw UserDoesntExistException when user to unban doesn't exist, HTTP status 400")
        @Test
        void shouldNotUnbanUser_UserDoesntExist() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(UNBAN_USER_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserDoesntExistException));
        }

        @DisplayName("Should not unban user when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotUnbanUser_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(patch(UNBAN_USER_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not unban user when requested by user role, HTTP status 403")
        @Test
        void shouldNotUnbanUser_WhenRequestedByUserRole() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(UNBAN_USER_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should throw UserBlockException when user is not banned, HTTP status 400")
        @Test
        void shouldNotUnbanUser_UserIsUnbanned() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(UNBAN_USER_UNBANNED_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserBlockException));
        }
    }

    @DisplayName("patch /moderator/change-password/{username} endpoint")
    @Nested
    class ModeratorChangePasswordUsernameEndpointTests {

        @DisplayName("Should change password")
        @Test
        void shouldChangePassword() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            String jsonPassword = objectMapper.writeValueAsString(newPassword);

            mockMvc.perform(patch(CHANGE_USERS_PASSWORD_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(APPLICATION_JSON)
                            .content(jsonPassword))
                    .andExpect(status().isOk());
        }

        @DisplayName("Should throw UserDoesntExistException when user doesn't exist, HTTP status 400")
        @Test
        void shouldThrowUserDoesntExistException_WhenUserDoesntExist() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            String jsonPassword = objectMapper.writeValueAsString(newPassword);

            mockMvc.perform(patch(CHANGE_USERS_PASSWORD_USER_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(APPLICATION_JSON)
                            .content(jsonPassword))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserDoesntExistException));
        }

        @DisplayName("Should throw MethodArgumentNotValidException when password is empty, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenPasswordIsEmpty() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            String jsonPassword = objectMapper.writeValueAsString(emptyPassword);

            mockMvc.perform(patch(CHANGE_USERS_PASSWORD_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(APPLICATION_JSON)
                            .content(jsonPassword))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @DisplayName("Should not change password when JWT is invalid, HTTP status 401")
        @Test
        void shouldNotChangePassword_WhenInvalidAuthentication() throws Exception {
            String jsonPassword = objectMapper.writeValueAsString(newPassword);

            mockMvc.perform(patch(CHANGE_USERS_PASSWORD_USER_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(APPLICATION_JSON)
                            .content(jsonPassword))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not change password when requested by user role, HTTP status 403")
        @Test
        void shouldNotChangePassword_WhenRequestedByUserRole() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            String jsonPassword = objectMapper.writeValueAsString(newPassword);

            mockMvc.perform(patch(CHANGE_USERS_PASSWORD_USER_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(APPLICATION_JSON)
                            .content(jsonPassword))
                    .andExpect(status().is(403));
        }
    }

    @DisplayName("delete /admin/delete/{username} endpoint")
    @Nested
    class AdminDeleteUsernameEndpointTests {

        @DisplayName("Should delete user")
        @Test
        void shouldDeleteUser() throws Exception {
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_USER_ENDPOINT)
                            .header(AUTHORIZATION, adminToken))
                    .andExpect(status().isOk());
        }

        @DisplayName("Should throw UserDoesntExistException when user deleting user doesn't exist, HTTP status 400")
        @Test
        void shouldThrowUserDoesntExistException_WhenUserDoesntExist() throws Exception {
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_USER_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, adminToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserDoesntExistException));
        }

        @DisplayName("Should throw UserAdminDeleteException when deleting user is moderator, HTTP status 403")
        @Test
        void shouldThrowUserAdminDeleteException_WhenDeletingUserIsModerator() throws Exception {
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_USER_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, adminToken))
                    .andExpect(status().is(403))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserAdminDeleteException));
        }

        @DisplayName("Should not delete user when JWT is invalid, HTTP status 401")
        @Test
        void shouldNotDeleteUser_WhenJWTIsInvalid() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_USER_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not delete user when requested by moderator, HTTP status 403")
        @Test
        void shouldNotDeleteUser_WhenRequestedByModerator() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_USER_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(403));
        }
    }

    @DisplayName("delete /admin/edit/{username}/role/{roleName} endpoint")
    @Nested
    class AdminEditRoleEndpointTests {

        @DisplayName("Should change role, HTTP status 200")
        @Test
        void shouldChangeRole() throws Exception {
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_ROLE_ENDPOINT)
                            .header(AUTHORIZATION, adminToken))
                    .andExpect(status().is(200));
        }

        @DisplayName("Should throw RoleException when user already has that role, HTTP status 400")
        @Test
        void shouldThrowRoleException_WhenUserAlreadyHasThatRole() throws Exception {
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_ROLE_DUPLICATE_ENDPOINT)
                            .header(AUTHORIZATION, adminToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof RoleException));
        }

        @DisplayName("Should throw UserDoesntExistException when user to change role doesn't exist, HTTP status 400")
        @Test
        void shouldThrowUserDoesntExistException_WhenUserToChangeRoleDoesntExist() throws Exception {
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_ROLE_USER_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, adminToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserDoesntExistException));
        }

        @DisplayName("Should throw RoleDoesntExistException when role to change doesn't exist, HTTP status 400")
        @Test
        void shouldNotChangeUserRole_RoleDoesntExist() throws Exception {
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_ROLE_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, adminToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof RoleDoesntExistException));
        }

        @DisplayName("Should not change role when JWT is invalid, HTTP status 401")
        @Test
        void shouldNotChangeRole_WhenJWTIsInvalid() throws Exception {
            mockMvc.perform(patch(CHANGE_ROLE_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not change role when requested by moderator, HTTP status 401")
        @Test
        void shouldNotChangeRole_WhenRequestedByModerator() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_ROLE_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(403));
        }
    }
}