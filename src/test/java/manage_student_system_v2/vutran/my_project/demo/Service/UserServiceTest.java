package manage_student_system_v2.vutran.my_project.demo.Service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import lombok.extern.slf4j.Slf4j;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.ChangePasswordRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.UserCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.UserUpdateRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.ChangePassResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.RoleResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.UserResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.User;
import manage_student_system_v2.vutran.my_project.demo.Exception.AppException;
import manage_student_system_v2.vutran.my_project.demo.Mapper.UserMapper;
import manage_student_system_v2.vutran.my_project.demo.Repository.RoleRepository;
import manage_student_system_v2.vutran.my_project.demo.Repository.UserRepository;

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

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private LocalDate dob;
    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;
    private User user;
    private String userId;
    private String userName;
    private UserUpdateRequest userUpdateRequest;
    private UserResponse userUpdateResponse;

    @BeforeEach
    void initData() {}

    @Test
    @WithMockUser(username = "tranvu123456")
    void getMyInfo_valid_success() {
        // arrange
        User user = User.builder()
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .dob(LocalDate.of(2000, 5, 20))
                .build();
        RoleResponse roleResponse = RoleResponse.builder().name("USER").build();
        UserResponse userResponse = UserResponse.builder()
                .id("a7ebc82b-a64b-4e51-a176-b4e41fc82284")
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .roles(new HashSet<>(Collections.singletonList(roleResponse)))
                .dob(LocalDate.of(2000, 5, 20))
                .build();

        log.info("Test getMyInfo_valid_success bắt đầu với username: {}", "tranvu123456");

        // Sửa dấu ngoặc đơn bị thiếu ở đây
        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(user));
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(userResponse);
        log.info("Mock userRepository trả về người dùng với username: {}", "tranvu123456");

        var response = userService.getMyInfo();
        log.info("Kết quả trả về: {}", response);

        Assertions.assertThat(response.getUsername()).isEqualTo("tranvu123456");
        Assertions.assertThat(response.getId()).isEqualTo("a7ebc82b-a64b-4e51-a176-b4e41fc82284");
        log.info("Test thành công với userId: {} và username: {}", response.getId(), response.getUsername());
    }

    @Test
    @WithMockUser(username = "tranvu123456") // given
    void getMyInfo_Invalid_fail() {
        log.info("Test getMyInfo_Invalid_fail bắt đầu với username: {}", "tranvu123456");

        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());
        log.info("Mock userRepository trả về empty cho username: {}", "tranvu123456");

        // when
        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());
        log.error("Lỗi xảy ra khi không tìm thấy người dùng: {}", exception.getMessage());

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(105);
        log.info("Test thất bại với mã lỗi: {}", exception.getErrorCode().getCode());
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void testGetUserByUserName_withRoleAdmin_fail() {
        // arrange
        String userNameFail = "tranvu12345";

        log.info("Kiểm tra getUser với userName không tồn tại: {}", userNameFail);
        // giả lập dữ liệu từ repo va trả về null
        Mockito.when(userRepository.findByUsername(userNameFail)).thenReturn(Optional.empty());

        // Kiểm tra xem có ngoại lệ AppException với mã lỗi USER_NOT_FOUND không
        var exception = assertThrows(AppException.class, () -> userService.getUser(userNameFail));
        log.error("Mã lỗi: {}", exception.getErrorCode());
        log.error("Lỗi xảy ra: {}", exception.getMessage());

        // Xác nhận mã lỗi USER_NOT_FOUND (103)
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(103);
        log.info(
                "Test thất bại đúng như mong đợi với mã lỗi: {}",
                exception.getErrorCode().getCode());
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"}) // mock user với quyền admin
    void testGetUserByUserName_withRoleAdmin_success() {
        // arrange
        String userName = "tranvu123456";
        User user = User.builder()
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .dob(LocalDate.of(2000, 5, 20))
                .build();
        RoleResponse roleResponse = RoleResponse.builder().name("USER").build();
        UserResponse userResponse = UserResponse.builder()
                .id("a7ebc82b-a64b-4e51-a176-b4e41fc82284")
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .roles(new HashSet<>(Collections.singletonList(roleResponse)))
                .dob(LocalDate.of(2000, 5, 20))
                .build();

        // ACT
        log.info("Kiểm tra getUser với userName: {}", userName);
        // giả lập dữ liệu trả về từ repo
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
    void createUser_valid_Success() {
        // arrange
        UserCreationRequest userCreationRequest = UserCreationRequest.builder()
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .dob(LocalDate.of(2000, 5, 20))
                .password("12345678")
                .build();
        User user = User.builder()
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .password("12345678")
                .dob(LocalDate.of(2000, 5, 20))
                .build();
        RoleResponse roleResponse = RoleResponse.builder().name("USER").build();
        UserResponse userResponse = UserResponse.builder()
                .id("a7ebc82b-a64b-4e51-a176-b4e41fc82284")
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .roles(new HashSet<>(Collections.singletonList(roleResponse)))
                .dob(LocalDate.of(2000, 5, 20))
                .build();
        // ACT
        log.info("Test createUser bắt đầu với username: {}", userCreationRequest.getUsername());
        // mock service để trả về userResponse khi gọi createUser
        Mockito.when(userRepository.existsByUsername(userCreationRequest.getUsername()))
                .thenReturn(false);
        Mockito.when(userMapper.toUser(userCreationRequest)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(user.getPassword()))
                .thenReturn("$2a$12$vE1Pnk5jy0rhHx25esIh6.cx6.8./iCYPcDHYwNJOHz/gOF2W9G8.");
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        log.info(
                "Mock thành công: userRepository trả về false cho kiểm tra username '{}', userMapper chuyển đổi từ UserCreationRequest thành User",
                userCreationRequest.getUsername());
        // Thực hiện tạo người dùng
        var response = userService.createUser(userCreationRequest);
        log.info("Kết quả trả về sau khi tạo người dùng: {}", response);

        // Kiểm tra kết quả trả về
        Assertions.assertThat(response.getUsername()).isEqualTo("tranvu123456");
        Assertions.assertThat(response.getId()).isEqualTo("a7ebc82b-a64b-4e51-a176-b4e41fc82284");
        log.info("===Test thành công với username: {}===", response.getUsername());
    }

    @Test
    void createUser_invalid_UsernameExist_fail() {
        // arrange
        UserCreationRequest userCreationRequest = UserCreationRequest.builder()
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .dob(LocalDate.of(2000, 5, 20))
                .password("12345678")
                .build();

        // gia lap với người dùng tồn tại
        Mockito.when(userRepository.existsByUsername(userCreationRequest.getUsername()))
                .thenReturn(true);
        var exception = assertThrows(AppException.class, () -> userService.createUser(userCreationRequest));
        log.error("Mã lỗi: {}", exception.getErrorCode());
        log.error("Lỗi xảy ra: {}", exception.getMessage());
        // Xác nhận mã lỗi USER_EXISTED (102)
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(102);
        log.info(
                "Test thất bại đúng như mong đợi với mã lỗi: {}",
                exception.getErrorCode().getCode());
    }

    @Test
    void changePassword_success() {
        // arrange
        ChangePasswordRequest request = new ChangePasswordRequest("1234", "12345");
        String userName = "tranvu123456";
        User user = User.builder()
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .password("1234")
                .dob(LocalDate.of(2000, 5, 20))
                .build();

        // ACT
        log.info("Bắt đầu test changePassword_success");

        // Giả lập user trong DB với mật khẩu cũ đã được mã hóa
        user.setPassword(passwordEncoder.encode("1234"));
        Mockito.when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
                .thenReturn(true); // Mật khẩu cũ hợp lệ voi database

        // Giả lập lưu user mới với mật khẩu đã mã hóa
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        ChangePassResponse response = userService.changePassword("tranvu123456", request);

        // Kiểm tra phản hồi
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getResponse()).isEqualTo("Password changed successfully !");
        log.info("Test thành công: {}", response.getResponse());
    }

    @Test
    void changePassword_fail_oldPassword() {
        // arrange
        String userName = "tranvu123456";
        User user = User.builder()
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .password("1234")
                .dob(LocalDate.of(2000, 5, 20))
                .build();

        // ACT (thực thi)
        log.info("Bắt đầu test changePassword_fail_oldPassword");
        // giả lập với mật khẩu cũ sai
        ChangePasswordRequest request = new ChangePasswordRequest("123", "12345");
        user.setPassword(passwordEncoder.encode("1234")); // giả lập mật khẩu trong db là 1234
        Mockito.when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));
        // mat khau cu sai
        Mockito.when(passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
                .thenReturn(false);
        var exception = assertThrows(AppException.class, () -> userService.changePassword("tranvu123456", request));
        log.error("Mã lỗi: {}", exception.getErrorCode());
        log.error("Lỗi xảy ra: {}", exception.getMessage());
        // Xác nhận mã lỗi INVALID_OLD_PASSWORD (112)
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(112);
        log.info(
                "Test thất bại đúng như mong đợi với mã lỗi: {} và thông báo: {}",
                exception.getErrorCode().getCode(),
                exception.getMessage());
    }

    @Test
    void changePassword_fail_username() {
        // arrange
        String userNameFail = "tranvu12345";
        ChangePasswordRequest request = new ChangePasswordRequest("123", "12345");
        // ACT
        log.info("Bắt đầu test changePassword_fail_username");
        // giả lập với username sai
        Mockito.when(userRepository.findByUsername(userNameFail)).thenReturn(Optional.empty());
        var exception = assertThrows(AppException.class, () -> userService.changePassword(userNameFail, request));
        log.error("Mã lỗi: {}", exception.getErrorCode());
        log.error("Lỗi xảy ra: {}", exception.getMessage());
        // Xác nhận mã lỗi USER_NOT_FOUND (103)
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(103);
        log.info(
                "Test thất bại đúng như mong đợi với mã lỗi: {}",
                exception.getErrorCode().getCode());
    }

    @Test
    void testUpdateUser_success() {
        // arrange
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .dob(LocalDate.of(2002, 5, 19))
                .firstName("Van A")
                .lastName("Nguyen")
                .username("tranvu123456")
                .roles(List.of("USER"))
                .build();
        String userId = "a7ebc82b-a64b-4e51-a176-b4e41fc82284";
        User user = User.builder()
                .id("a7ebc82b-a64b-4e51-a176-b4e41fc82284")
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .password("1234")
                .dob(LocalDate.of(2000, 5, 20))
                .build();
        RoleResponse roleResponse = RoleResponse.builder().name("USER").build();
        UserResponse userUpdateResponse = UserResponse.builder()
                .id("a7ebc82b-a64b-4e51-a176-b4e41fc82284")
                .username("tranvu123456")
                .firstName("Van A")
                .lastName("Nguyen")
                .roles(new HashSet<>(Collections.singletonList(roleResponse)))
                .dob(LocalDate.of(2002, 5, 19))
                .build();

        // ACT
        log.info("Bắt đầu test testUpdateUser_success");
        // Thiết lập hành vi mong đợi của mocks
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(roleRepository.findAllById(userUpdateRequest.getRoles()))
                .thenReturn(Collections.emptyList()); // Giả lập tìm thấy roles
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(userUpdateResponse);

        // Thực hiện update
        var response = userService.updateUser(userId, userUpdateRequest);
        log.info("Kết quả trả về sau khi tạo người dùng: {}", response);

        // Kiểm tra kết quả trả về
        Assertions.assertThat(response.getUsername()).isEqualTo("tranvu123456");
        Assertions.assertThat(response.getId()).isEqualTo("a7ebc82b-a64b-4e51-a176-b4e41fc82284");
        Assertions.assertThat(response.getLastName()).isEqualTo("Nguyen");
        log.info("===Test thành công với username: {}===", response.getUsername());

        // Xác minh tương tác với mocks, kiểm tra các phương thức chỉ được gọi hoặc cập nhật đúng 1 lần
        Mockito.verify(userRepository, Mockito.times(1)).findById(userId); // số 1 là số lần tương tác
        Mockito.verify(userMapper, Mockito.times(1)).updateUser(user, userUpdateRequest);
        Mockito.verify(roleRepository, Mockito.times(1)).findAllById(userUpdateRequest.getRoles());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(userMapper, Mockito.times(1)).toUserResponse(user);
    }
    /*
    	 *
    	 * Các dòng Mockito.when(...) không trực tiếp thiết lập hành vi trả về cho phương thức userService.updateUser().
    	 *  Thay vào đó, chúng thiết lập hành vi trả về cho các dependency (các đối tượng mà userService phụ thuộc vào, như userRepository, roleRepository, và userMapper)
    	 *  khi các phương thức của chúng được gọi bên trong phương thức userService.updateUser().
    Khi bạn gọi userService.updateUser(userId, userUpdateRequest), phương thức này sẽ tương tác với các dependency đã được mock.
    *  Mockito sẽ đảm bảo rằng khi các phương thức của các mock này được gọi với các tham số đã được thiết lập trong Mockito.when(...),
    *  chúng sẽ trả về các giá trị giả lập mà bạn đã định nghĩa. Điều này cho phép bạn kiểm soát các đầu vào và đầu ra của các dependency,
    *  giúp bạn kiểm thử logic của userService.updateUser() một cách độc lập.

    Sau đó, bạn sử dụng Assertions.assertThat(response...) để kiểm tra xem kết quả trả về (response) từ phương thức userService.updateUser() có đúng như mong đợi hay không,
    *  dựa trên các hành vi giả lập mà bạn đã thiết lập cho các dependency.
    	 * */

    @Test
    void testUpdateUser_fail_dob() {
        // arrange
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .dob(LocalDate.of(2018, 5, 19))
                .firstName("Van A")
                .lastName("Nguyen")
                .username("tranvu123456")
                .roles(List.of("USER"))
                .build();
        String userId = "a7ebc82b-a64b-4e51-a176-b4e41fc82284";
        User user = User.builder()
                .id("a7ebc82b-a64b-4e51-a176-b4e41fc82284")
                .username("tranvu123456")
                .firstName("ronaldo")
                .lastName("messi")
                .password("1234")
                .dob(LocalDate.of(2000, 5, 20))
                .build();
        RoleResponse roleResponse = RoleResponse.builder().name("USER").build();
        UserResponse userUpdateResponse = UserResponse.builder()
                .id("a7ebc82b-a64b-4e51-a176-b4e41fc82284")
                .username("tranvu123456")
                .firstName("Van A")
                .lastName("Nguyen")
                .roles(new HashSet<>(Collections.singletonList(roleResponse)))
                .dob(LocalDate.of(2002, 5, 19))
                .build();

        // ACT
        log.info("Bắt đầu test testUpdateUser_fail_dob");
        // Thiết lập hành vi mong đợi của mocks
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(roleRepository.findAllById(userUpdateRequest.getRoles()))
                .thenReturn(Collections.emptyList()); // Giả lập tìm thấy roles
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(userUpdateResponse);
        // thực hiện service
        var exception = assertThrows(AppException.class, () -> userService.updateUser(userId, userUpdateRequest));
        log.error("Mã lỗi: {}", exception.getErrorCode());
        log.error("Lỗi xảy ra: {}", exception.getMessage());
        // Xác nhận mã lỗi INVALID_DOB (111)
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(111);
        log.info(
                "Test thất bại đúng như mong đợi với mã lỗi: {}",
                exception.getErrorCode().getCode());
    }
}
