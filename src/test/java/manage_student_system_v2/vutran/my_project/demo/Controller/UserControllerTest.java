package manage_student_system_v2.vutran.my_project.demo.Controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.UserCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.UserUpdateRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.UserResponse;
import manage_student_system_v2.vutran.my_project.demo.Exception.AppException;
import manage_student_system_v2.vutran.my_project.demo.Exception.ErrorCode;
import manage_student_system_v2.vutran.my_project.demo.Service.UserService;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;
    private LocalDate dob;
    private final String userId = "a7ebc82b-a64b-4e51-a176-b4e41fc82284";
    private final String userIdFail = "a7ebc82b";

    // nhiều test case sử dụng nên khởi tạo chung
    // chuan bi du lieu dau vao
    @BeforeEach
    void initData() {
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
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // given, là hàm init
        // convert object to string
        log.info("Test createUser_validRequest_success bắt đầu với userCreationRequest: {}", userCreationRequest);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);
        log.info("Đang thực hiện POST Request tới /users để tạo người dùng mới");
        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                // then
                .andExpect(status().isOk()) // status
                .andExpect(jsonPath("code").value(100)) // trang thái code return
                .andExpect(jsonPath("result.id").value("a7ebc82b-a64b-4e51-a176-b4e41fc82284"));
        log.info("Test tạo người dùng thành công với id: {}", userResponse.getId());
    }

    @Test
    void createUser_UsernameInvalid_fail() throws Exception {
        // given
        log.info("Test createUser_UsernameInvalid_fail bắt đầu với username không hợp lệ");
        userCreationRequest.setUsername("tranvu"); // lỗi do - less than 8 characters
        // convert object to string
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);

        // when, then
        log.info("Đang thực hiện POST Request tới /users với username không hợp lệ");
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Username at least 8 characters"));
        log.info("Test xác nhận lỗi với thông báo: 'Username at least 8 characters'");
    }

    @Test
    void getUser_success_withRoleAdmin() throws Exception {
        // given userId (userAdmin)
        Mockito.when(userService.getUser(ArgumentMatchers.anyString())).thenReturn(userResponse);
        log.info("Mock với userService.getUser để trả về userResponse: {}", userResponse);
        // when
        log.info("Đang thực hiện GET Request tới /users/{} với JWT chứa role ADMIN", userId);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .jwt(jwt -> jwt.claim("roles", "ADMIN")))) // add role admin into jwt
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(100))
                .andExpect(jsonPath("result.id").value(userId))
                .andExpect(jsonPath("result.username").value("tranvu123456"));
        log.info("Test thành công với userId: {}", userId);
    }

    @Test
    void getUser_withRoleAdmin_fail() throws Exception {
        // given userId
        log.info("Test getUser_withRoleAdmin_fail bắt đầu với userId không tồn tại: {}", userIdFail);
        Mockito.when(userService.getUser(userIdFail)).thenThrow(new AppException(ErrorCode.USER_NOT_FOUND));
        log.info("Mock với userService.getUser() sẽ ném ngoại lệ: USER_NOT_FOUND với userId: {}", userIdFail);
        // when
        log.info("Đang thực hiện GET Request tới /users/{} với JWT chứa role ADMIN ", userIdFail);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", userIdFail)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .jwt(jwt -> jwt.claim("roles", "ADMIN")))) // add role admin into jwt
                // then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value(103))
                .andExpect(jsonPath("result").value("User not found !"));
        log.info("Test thất bại với thông báo lỗi: 'User not found !'");
    }

    @Test
    void testUpdateUser_fail_dob() throws Exception {
        // arrange
        String userId = "a7ebc82b-a64b-4e51-a176-b4e41fc82284";
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .dob(LocalDate.of(2018, 5, 19))
                .firstName("Van A")
                .lastName("Nguyen")
                .username("tranvu123456")
                .roles(List.of("USER"))
                .build();
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequest)))
                .andExpect(status().isBadRequest()) // mong doi loi 400 do validation
                .andExpect(jsonPath("$.errors[0].message").value("INVALID_DOB"));
        //                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("INVALID_DOB")));
    }
}
