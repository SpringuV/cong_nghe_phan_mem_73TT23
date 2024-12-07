package manage_student_system_v2.vutran.my_project.demo.Controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.DepartmentCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.ApiResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.DepartmentResponse;
import manage_student_system_v2.vutran.my_project.demo.Service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentController {

    DepartmentService departmentService;

    @PostMapping
    ApiResponse<DepartmentResponse> createDepartment(@RequestBody DepartmentCreationRequest departmentCreationRequest){
        return ApiResponse.<DepartmentResponse>builder()
                .result(departmentService.createDepartment(departmentCreationRequest))
                .build();
    }

    @GetMapping
    ApiResponse<Set<DepartmentResponse>> getListDepartment(){
        return ApiResponse.<Set<DepartmentResponse>>builder()
                .result(departmentService.getListDepartment())
                .build();
    }

    @DeleteMapping("/{departmentId}")
    ApiResponse<String> deleteDepartment(@PathVariable("departmentId") String idDepartment){
        return ApiResponse.<String>builder()
                .result(departmentService.deleteDepartment(idDepartment))
                .build();
    }

}
