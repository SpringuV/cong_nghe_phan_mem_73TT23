package manage_student_system_v2.vutran.my_project.demo.Controller;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.UserCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.UserResponse;
import manage_student_system_v2.vutran.my_project.demo.Exception.AppException;
import manage_student_system_v2.vutran.my_project.demo.Exception.ErrorCode;
import manage_student_system_v2.vutran.my_project.demo.Service.UserService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;


    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;
    private LocalDate dob;
    private final String userId =  "a7ebc82b-a64b-4e51-a176-b4e41fc82284";
    private final String userIdFail = "a7ebc82b";

    // nhiều test case sử dụng nên khởi tạo chung
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
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // given, là hàm init
        // convert object to string
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);

        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);
        // when
        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                // then
                .andExpect(MockMvcResultMatchers.status().isOk()) // status
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(100)) // trang thái code return
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("a7ebc82b-a64b-4e51-a176-b4e41fc82284")
        );
    }

    @Test
    void createUser_UsernameInvalid_fail() throws Exception {
        // given
        userCreationRequest.setUsername("tranvu");
        // convert object to string
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content =objectMapper.writeValueAsString(userCreationRequest);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                // then
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username at least 8 characters"));
    }

    @Test
    void getUser_success_withRoleAdmin() throws Exception {
        // given userId

        Mockito.when(userService.getUser(ArgumentMatchers.anyString())).thenReturn(userResponse);
        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .jwt(jwt -> jwt.claim("roles", "ADMIN")))) // add role admin into jwt
                // then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("result.username").value("tranvu123456"));
    }

    @Test
    void getUser_withRoleAdmin_fail() throws Exception {
        // given userId
        Mockito.when(userService.getUser(userIdFail)).thenThrow(new AppException(ErrorCode.USER_NOT_FOUND));
        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", userIdFail)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .jwt(jwt -> jwt.claim("roles", "ADMIN")))) // add role admin into jwt
                // then
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(103))
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("User not found !"));
    }
}
