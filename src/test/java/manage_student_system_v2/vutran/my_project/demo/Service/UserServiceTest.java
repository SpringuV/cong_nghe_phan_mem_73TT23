package manage_student_system_v2.vutran.my_project.demo.Service;

import lombok.extern.slf4j.Slf4j;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.UserCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.RoleResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.UserResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.User;
import manage_student_system_v2.vutran.my_project.demo.Exception.AppException;
import manage_student_system_v2.vutran.my_project.demo.Mapper.UserMapper;
import manage_student_system_v2.vutran.my_project.demo.Repository.RoleRepository;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;


@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
public class UserServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;
    private LocalDate dob;
    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;
    private User user;
    private String userId;
    private String userName;

    @BeforeEach
    void initData(){
        userName = "tranvu123456";
        userId = "a7ebc82b-a64b-4e51-a176-b4e41fc82284";
        dob = LocalDate.of(2000, 5, 20);
        userCreationRequest = UserCreationRequest.builder()
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .dob(dob)
                .password("12345678")
                .build();
        RoleResponse roleResponse = RoleResponse.builder().name("USER").build();
        // chuẩn bị dữ liệu cho userResponse
        userResponse = UserResponse.builder()
                .id("a7ebc82b-a64b-4e51-a176-b4e41fc82284")
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .roles(new HashSet<>(Collections.singletonList(roleResponse)))
                .dob(dob)
                .build();
        user = User.builder()
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .dob(dob)
                .build();
    }


    @Test
    @WithMockUser(username = "tranvu123456")
    void getMyInfo_valid_success(){
        log.info("Test getMyInfo_valid_success bắt đầu với username: {}", "tranvu123456");
        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(Optional.of(user));
        log.info("Mock userRepository trả về người dùng với username: {}", "tranvu123456");

        var response =userService.getMyInfo();
        log.info("Kết quả trả về: {}", response);

        Assertions.assertThat(response.getUsername()).isEqualTo("tranvu123456");
        Assertions.assertThat(response.getId()).isEqualTo("a7ebc82b-a64b-4e51-a176-b4e41fc82284");
        log.info("Test thành công với userId: {} và username: {}", response.getId(), response.getUsername());
    }

    @Test
    @WithMockUser(username = "tranvu123456") // given
    void getMyInfo_Invalid_fail(){
        log.info("Test getMyInfo_Invalid_fail bắt đầu với username: {}", "tranvu123456");

        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        log.info("Mock userRepository trả về empty cho username: {}", "tranvu123456");

        // when
        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());
        log.error("Lỗi xảy ra khi không tìm thấy người dùng: {}", exception.getMessage());

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(105);
        log.info("Test thất bại với mã lỗi: {}", exception.getErrorCode().getCode());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // mock user với quyền admin
    void testGetUserByUserName_withRoleAdmin_success(){
        log.info("Kiểm tra getUser với userName: {}", userName);
        //giả lập dữ liệu trả về từ repo
        Mockito.when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        var response = userService.getUser(userName);
        log.info("Kết quả trả về từ getUser: {}", response);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUsername()).isEqualTo("tranvu123456");
        Assertions.assertThat(response.getRoles().iterator().next().getName()).isEqualTo("USER");
        Assertions.assertThat(response.getLastName()).isEqualTo("messi");
        log.info("Test thành công với userId: {} và username: {}", response.getId(), response.getUsername());
    }

    @Test
    void createUser_valid_Success(){
        log.info("Test createUser bắt đầu với username: {}", userCreationRequest.getUsername());
        // mock service để trả về userResponse khi gọi createUser
        Mockito.when(userRepository.existsByUsername(userCreationRequest.getUsername())).thenReturn(false);
        Mockito.when(userMapper.toUser(userCreationRequest)).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        log.info("Mock thành công: userRepository trả về false cho kiểm tra username '{}', userMapper chuyển đổi từ UserCreationRequest thành User", userCreationRequest.getUsername());
        // Thực hiện tạo người dùng
        var response = userService.createUser(userCreationRequest);
        log.info("Kết quả trả về sau khi tạo người dùng: {}", response);

        // Kiểm tra kết quả trả về
        Assertions.assertThat(response.getUsername()).isEqualTo("tranvu123456");
        Assertions.assertThat(response.getId()).isEqualTo("a7ebc82b-a64b-4e51-a176-b4e41fc82284");
        log.info("===Test thành công với username: {}===", response.getUsername());

    }
}
