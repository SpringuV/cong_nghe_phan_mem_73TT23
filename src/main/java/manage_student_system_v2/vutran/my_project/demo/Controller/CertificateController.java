package manage_student_system_v2.vutran.my_project.demo.Controller;

import java.util.Set;

import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.CertificateCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.ApiResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.CertificateResponse;
import manage_student_system_v2.vutran.my_project.demo.Service.CertificateService;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CertificateController {

    CertificateService certificateService;

    @PostMapping
    ApiResponse<CertificateResponse> createCertificate(@RequestBody CertificateCreationRequest request) {
        return ApiResponse.<CertificateResponse>builder()
                .result(certificateService.createCertificate(request))
                .build();
    }

    @GetMapping("/{certificateId}")
    ApiResponse<CertificateResponse> getCertificateById(@PathVariable("certificateId") String idCertificate) {
        return ApiResponse.<CertificateResponse>builder()
                .result(certificateService.getCertificateById(idCertificate))
                .build();
    }

    @GetMapping("/{certificateName}")
    ApiResponse<CertificateResponse> getCertificateByName(@PathVariable("certificateName") String nameCertificate) {
        return ApiResponse.<CertificateResponse>builder()
                .result(certificateService.getCertificateByName(nameCertificate))
                .build();
    }

    @GetMapping
    ApiResponse<Set<CertificateResponse>> getListCertificate() {
        return ApiResponse.<Set<CertificateResponse>>builder()
                .result(certificateService.getListCertificate())
                .build();
    }

    @DeleteMapping("/{certificateId}")
    ApiResponse<String> deleteCertificateById(@PathVariable("certificateId") String idCertificate) {
        return ApiResponse.<String>builder()
                .result(certificateService.deleteCertificate(idCertificate))
                .build();
    }

    @PutMapping("/{certificateId}")
    ApiResponse<CertificateResponse> updateCertificate(
            @PathVariable("certificateId") String id,
            @RequestBody CertificateCreationRequest certificateCreationRequest) {
        return ApiResponse.<CertificateResponse>builder()
                .result(certificateService.updateCertificate(id, certificateCreationRequest))
                .build();
    }
}
