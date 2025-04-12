package manage_student_system_v2.vutran.my_project.demo.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.StudentCreateRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.StudentDeleteRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.StudentUpdateRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.ApiResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.StudentUpdateResponse;
import manage_student_system_v2.vutran.my_project.demo.Service.StudentService;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StudentController {

    StudentService studentService;

    @PostMapping
    ApiResponse<StudentUpdateResponse> createStudent(@RequestBody StudentCreateRequest request) {
        return ApiResponse.<StudentUpdateResponse>builder()
                .result(studentService.createStudent(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<StudentUpdateResponse>> getListStudent() {
        return ApiResponse.<List<StudentUpdateResponse>>builder()
                .result(studentService.getListStudent())
                .build();
    }
    // http://localhost:8080/students/${studentId}
    // tra cuu sinh vien
    @GetMapping("/{studentId}")
    ApiResponse<StudentUpdateResponse> getStudent(@PathVariable("studentId") String id) {
        return ApiResponse.<StudentUpdateResponse>builder()
                .result(studentService.getStudent(id))
                .build();
    }

    @PutMapping("/update/{studentId}")
    ApiResponse<StudentUpdateResponse> updateStudent(
            @PathVariable("studentId") String id, @RequestBody StudentUpdateRequest studentUpdateRequest) {
        return ApiResponse.<StudentUpdateResponse>builder()
                .result(studentService.updateStudent(id, studentUpdateRequest))
                .build();
    }

    @DeleteMapping
    ApiResponse<String> deleteStudent(@RequestBody StudentDeleteRequest deleteRequest) {
        return ApiResponse.<String>builder()
                .result(studentService.deleteStudent(deleteRequest))
                .build();
    }
}
