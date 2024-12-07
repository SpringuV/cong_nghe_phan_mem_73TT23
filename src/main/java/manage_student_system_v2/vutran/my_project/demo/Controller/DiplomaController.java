package manage_student_system_v2.vutran.my_project.demo.Controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.DiplomaCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.ApiResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.DiplomaResponse;
import manage_student_system_v2.vutran.my_project.demo.Service.DiplomaService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/diplomas")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiplomaController {

    DiplomaService diplomaService;

    @PostMapping
    ApiResponse<DiplomaResponse> createDiploma(@RequestBody DiplomaCreationRequest diplomaCreationRequest){
        return ApiResponse.<DiplomaResponse>builder()
                .result(diplomaService.createDiploma(diplomaCreationRequest))
                .build();
    }

    @GetMapping("/{idDiploma}")
    ApiResponse<DiplomaResponse> getDiploma(@PathVariable("idDiploma") String idDiploma){
        return ApiResponse.<DiplomaResponse>builder()
                .result(diplomaService.getDiplomaByID(idDiploma))
                .build();
    }

    @GetMapping("/find/{nameMajor}")
    ApiResponse<Set<DiplomaResponse>> getListDiplomaByMajor(@PathVariable("nameMajor") String nameMajor){
        System.out.println("Name Major: " +nameMajor);
        return ApiResponse.<Set<DiplomaResponse>>builder()
                .result(diplomaService.getListDiplomaByMajor(nameMajor))
                .build();
    }

    @DeleteMapping("/{idDiploma}")
    ApiResponse<String> deleteDiploma(@PathVariable("idDiploma") String idDiploma){
        return ApiResponse.<String>builder()
                .result(diplomaService.deleteDiploma(idDiploma))
                .build();
    }

}
