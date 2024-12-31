package manage_student_system_v2.vutran.my_project.demo.Service;

import lombok.extern.slf4j.Slf4j;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.UserCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.UserResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.User;
import manage_student_system_v2.vutran.my_project.demo.Exception.AppException;
import manage_student_system_v2.vutran.my_project.demo.Exception.ErrorCode;
import manage_student_system_v2.vutran.my_project.demo.Repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDate;
import java.util.Optional;


@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
public class UserServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    private LocalDate dob;
    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;
    private User user;

    @BeforeEach
    void initData(){
        dob = LocalDate.of(2000, 5, 20);
        userCreationRequest = UserCreationRequest.builder()
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .dob(dob)
                .password("12345678")
                .build();
        userResponse = UserResponse.builder()
                .id("a7ebc82b-a64b-4e51-a176-b4e41fc82284")
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .dob(dob)
                .build();

        user = User.builder()
                .id("a7ebc82b-a64b-4e51-a176-b4e41fc82284")
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .dob(dob)
                .build();
    }


    @Test
    @WithMockUser(username = "tranvu123456")
    void getMyInfo_valid_success(){
        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(Optional.of(user));

        var response =userService.getMyInfo();

        Assertions.assertThat(response.getUsername()).isEqualTo("tranvu123456");
        Assertions.assertThat(response.getId()).isEqualTo("a7ebc82b-a64b-4e51-a176-b4e41fc82284");
    }

    @Test
    @WithMockUser(username = "tranvu123456") // given
    void getMyInfo_Invalid_fail(){
        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        // when
        var exception =assertThrows(AppException.class, ()-> userService.getMyInfo());

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(105);
    }
}
