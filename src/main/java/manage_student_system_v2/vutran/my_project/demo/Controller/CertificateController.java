package manage_student_system_v2.vutran.my_project.demo.Controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.CertificateCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.ApiResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.CertificateResponse;
import manage_student_system_v2.vutran.my_project.demo.Service.CertificateService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CertificateController {

    CertificateService certificateService;

    @PostMapping
    ApiResponse<CertificateResponse> createCertificate(@RequestBody CertificateCreationRequest request){
        return ApiResponse.<CertificateResponse>builder()
                .result(certificateService.createCertificate(request))
                .build();
    }

    @GetMapping("/{certificateName}")
    ApiResponse<CertificateResponse> getCertificateByName(@PathVariable("certificateName") String nameCertificate){
        return ApiResponse.<CertificateResponse>builder()
                .result(certificateService.getCertificateByName(nameCertificate))
                .build();
    }

    @GetMapping
    ApiResponse<Set<CertificateResponse>> getListCertificate(){
        return ApiResponse.<Set<CertificateResponse>>builder()
                .result(certificateService.getListCertificate())
                .build();
    }

    @DeleteMapping("/{certificateId}")
    ApiResponse<String> deleteCertificateById(@PathVariable("certificateId") String idCertificate){
        return ApiResponse.<String>builder()
                .result(certificateService.deleteCertificate(idCertificate))
                .build();
    }

    @PutMapping
    ApiResponse<CertificateResponse> updateCertificate(@RequestBody CertificateCreationRequest certificateCreationRequest){
        return ApiResponse.<CertificateResponse>builder()
                .result(certificateService.updateCertificate(certificateCreationRequest))
                .build();
    }
}
