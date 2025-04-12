package manage_student_system_v2.vutran.my_project.demo.Controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.ChangePasswordRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.UserCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.UserUpdateRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.*;
import manage_student_system_v2.vutran.my_project.demo.Mapper.UserMapper;
import manage_student_system_v2.vutran.my_project.demo.Service.UserService;

@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    UserService userService;
    UserMapper userMapper;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest userCreationRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(userCreationRequest))
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getListUser() {
        log.info("In controller: method get list User");
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getListUser())
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateInfoUser(
            @PathVariable("userId") String id, @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        log.info("In controller: method update Info User");
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(id, userUpdateRequest))
                .build();
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable("userId") String id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(id))
                .build();
    }

    @PostMapping("/change-pass/{username}")
    public ApiResponse<ChangePassResponse> changePassword(
            @PathVariable("username") String username, @RequestBody @Valid ChangePasswordRequest request) {
        return ApiResponse.<ChangePassResponse>builder()
                .result(userService.changePassword(username, request))
                .build();
    }
}
