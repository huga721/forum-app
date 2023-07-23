package huberts.spring.forumapp.warning;

import huberts.spring.forumapp.exception.user.UserBlockException;
import huberts.spring.forumapp.exception.user.UserDoesntExistException;
import huberts.spring.forumapp.exception.warning.WarningDoesntExistException;
import huberts.spring.forumapp.user.User;
import huberts.spring.forumapp.user.UserRepository;
import huberts.spring.forumapp.warning.dto.WarningDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class WarningServiceTest {

    private final static String USERNAME = "user";
    private final static String NOT_BANNED_MESSAGE = "Not banned";
    private final static String BANNED_MESSAGE = "Banned";

    @Mock
    private WarningRepository warningRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private WarningService warningService;

    private User user;
    private Warning warning;

    @BeforeEach
    void setUp() {
        List<Warning> warnings = new ArrayList<>();
        user = User.builder()
                .username(USERNAME)
                .warnings(warnings)
                .blocked(false)
                .build();
        warning = Warning.builder()
                .user(user)
                .build();
    }

    @DisplayName("createWarning method")
    @Nested
    class CreateWarningTests {

        @DisplayName("Should create warning")
        @Test
        void shouldCreateWarning() {
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

            WarningDTO warningCreated = warningService.createWarning(1L);

            assertEquals(warningCreated.status(), NOT_BANNED_MESSAGE);
            assertEquals(warningCreated.username(), USERNAME);
        }

        @DisplayName("Should throw UserDoesntExistException when user doesn't exist")
        @Test
        void shouldThrowUserDoesntExistException_WhenUserDoesntExist() {
            assertThrows(UserDoesntExistException.class, () -> warningService.createWarning(1L));
        }

        @DisplayName("Should ban user when user has 4 warnings and receive 5th")
        @Test
        void shouldBanUser_WhenUserHas4WarningAndReceive5th() {
            List<Warning> warnings = new ArrayList<>();
            warnings.add(warning);
            warnings.add(warning);
            warnings.add(warning);
            warnings.add(warning);
            user.setWarnings(warnings);

            when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
            WarningDTO warningCreated = warningService.createWarning(1L);

            assertEquals(USERNAME, warningCreated.username());
            assertEquals(BANNED_MESSAGE, warningCreated.status());
            assertTrue(user.isBlocked());
        }

        @DisplayName("Should throw UserBlockException when user to warn is already banned")
        @Test
        void shouldThrowUserBlockException_WhenUserToWarnIsAlreadyBanned() {
            user.setBlocked(true);
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
            assertThrows(UserBlockException.class, () -> warningService.createWarning(1L));
        }
    }

    @DisplayName("getWarningById method")
    @Nested
    class GetWarningByIdTests {

        @DisplayName("Should get warning")
        @Test
        void shouldGetWarning() {
           when(warningRepository.findById(any(Long.class))).thenReturn(Optional.of(warning));
           WarningDTO warningFound = warningService.getWarningById(1L);

           assertEquals(NOT_BANNED_MESSAGE, warningFound.status());
           assertEquals(USERNAME, warningFound.username());
        }

        @DisplayName("Should throw WarningDoesntExistException when warning with given id doesn't exist")
        @Test
        void shouldThrowWarningDoesntExistException_WhenWarningWithGivenIdDoesntExist() {
            assertThrows(WarningDoesntExistException.class, () -> warningService.getWarningById(1L));
        }
    }

    @DisplayName("getAllWarnings method")
    @Nested
    class GetAllWarningsTests {

        @DisplayName("Should return list of all warnings")
        @Test
        void shouldReturnAllWarnings() {
            when(warningRepository.findAll()).thenReturn(List.of(warning, warning, warning));
            List<WarningDTO> warnings = warningService.getAllWarnings();

            assertEquals(3, warnings.size());
            verify(warningRepository, times(1)).findAll();
        }

        @DisplayName("Should return empty warning list")
        @Test
        void shouldReturnEmptyWarningList() {
            when(warningRepository.findAll()).thenReturn(List.of());
            List<WarningDTO> warnings = warningService.getAllWarnings();

            assertTrue(warnings.isEmpty());
            verify(warningRepository, times(1)).findAll();
        }
    }

    @DisplayName("deleteWarning method")
    @Nested
    class DeleteWarningTests {

        @DisplayName("Should delete user last warning")
        @Test
        void shouldDeleteUserLastWarning() {
            List<Warning> warnings = new ArrayList<>();
            warnings.add(warning);
            warnings.add(warning);
            user.setWarnings(warnings);

            when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
            warningService.deleteWarning(1L);

            assertEquals(1, user.getWarnings().size());
            verify(warningRepository, times(1)).delete(any(Warning.class));
        }

        @DisplayName("Should throw UserDoesntExistException when user doesn't exist")
        @Test
        void shouldThrowUserDoesntExistException_WhenUserDoesntExist() {
            assertThrows(UserDoesntExistException.class, () -> warningService.deleteWarning(1L));
        }

        @DisplayName("Should throw UserBlockException when deleting warning of blocked user")
        @Test
        void shouldThrowUserBlockException_WhenDeletingWarningOfBlockedUser() {
            user.setBlocked(true);
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
            assertThrows(UserBlockException.class, () -> warningService.deleteWarning(1L));
        }

        @DisplayName("Should throw UserBlockException when user doesn't have any warning")
        @Test
        void shouldThrowUserBlockException_WhenUserDoesntHaveAnyWarning() {
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
            assertThrows(UserBlockException.class, () -> warningService.deleteWarning(1L));
        }
    }
}