package manage_student_system_v2.vutran.my_project.demo.Controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.PermissionCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.ApiResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.PermissionResponse;
import manage_student_system_v2.vutran.my_project.demo.Service.PermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/permissions")
public class PermissionController {

    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionCreationRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.createPermission(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getListPermission(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getListPermission())
                .build();
    }

    @DeleteMapping("/{idPermission}")
    ApiResponse<String> deletePermission(@PathVariable("idPermission") String id){
        return ApiResponse.<String>builder()
                .result(permissionService.deletePermission(id))
                .build();
    }

}
