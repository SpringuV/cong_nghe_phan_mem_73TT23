package manage_student_system_v2.vutran.my_project.demo.Controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.DiplomaCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.ApiResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.DiplomaResponse;
import manage_student_system_v2.vutran.my_project.demo.Service.DiplomaService;

@RestController
@RequestMapping("/diplomas")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiplomaController {

    private static final Logger log = LoggerFactory.getLogger(DiplomaController.class);
    DiplomaService diplomaService;

    @PostMapping
    ApiResponse<DiplomaResponse> createDiploma(@RequestBody DiplomaCreationRequest diplomaCreationRequest) {
        return ApiResponse.<DiplomaResponse>builder()
                .result(diplomaService.createDiploma(diplomaCreationRequest))
                .build();
    }

    @GetMapping
    ApiResponse<Set<DiplomaResponse>> getListDiploma() {
        return ApiResponse.<Set<DiplomaResponse>>builder()
                .result(diplomaService.getListDiploma())
                .build();
    }

    @GetMapping("/{idStudentId}")
    ApiResponse<Set<DiplomaResponse>> getlistDiplomaByStudentId(@PathVariable("idStudentId") String idStudentId) {
        return ApiResponse.<Set<DiplomaResponse>>builder()
                .result(diplomaService.getListDiplomaByStudentId(idStudentId))
                .build();
    }

    @GetMapping("/find/{nameMajor}")
    ApiResponse<Set<DiplomaResponse>> getListDiplomaByMajor(@PathVariable("nameMajor") String nameMajor) {
        log.info("Name Major: {}", nameMajor);
        return ApiResponse.<Set<DiplomaResponse>>builder()
                .result(diplomaService.getListDiplomaByMajor(nameMajor))
                .build();
    }

    @DeleteMapping("/{idDiploma}")
    ApiResponse<String> deleteDiploma(@PathVariable("idDiploma") String idDiploma) {
        return ApiResponse.<String>builder()
                .result(diplomaService.deleteDiploma(idDiploma))
                .build();
    }

    @PutMapping("/{diplomaId}")
    ApiResponse<DiplomaResponse> updateDiploma(
            @PathVariable("diplomaId") String id, @RequestBody DiplomaCreationRequest diplomaCreationRequest) {
        return ApiResponse.<DiplomaResponse>builder()
                .result(diplomaService.updateDiploma(id, diplomaCreationRequest))
                .build();
    }
}
