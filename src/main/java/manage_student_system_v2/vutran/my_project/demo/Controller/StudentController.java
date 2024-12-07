package manage_student_system_v2.vutran.my_project.demo.Controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.StudentCreateRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.ApiResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.StudentResponse;
import manage_student_system_v2.vutran.my_project.demo.Service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class StudentController {

    StudentService studentService;

    @PostMapping
    ApiResponse<StudentResponse> createStudent(@RequestBody @Valid StudentCreateRequest request){
        return ApiResponse.<StudentResponse>builder()
                .result(studentService.createStudent(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<StudentResponse>> getListStudent(){
        return ApiResponse.<List<StudentResponse>>builder()
                .result(studentService.getListStudent())
                .build();
    }

    @GetMapping("/info-student")
    ApiResponse<StudentResponse> getInfoStudent(){
        return ApiResponse.<StudentResponse>builder().build();
    }

}
