package manage_student_system_v2.vutran.my_project.demo.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.RoleCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.ApiResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.RoleResponse;
import manage_student_system_v2.vutran.my_project.demo.Service.RoleService;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleCreationRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getListRole() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getListRole())
                .build();
    }

    @DeleteMapping("/{roleId}")
    ApiResponse<Void> deleteRole(@PathVariable("roleId") String id) {
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder().build();
    }

    @PutMapping
    ApiResponse<RoleResponse> updateRole(@RequestBody RoleCreationRequest roleCreationRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.updateRole(roleCreationRequest))
                .build();
    }
}
