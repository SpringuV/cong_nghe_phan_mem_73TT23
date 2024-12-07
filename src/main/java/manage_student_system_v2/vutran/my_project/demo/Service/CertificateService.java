package manage_student_system_v2.vutran.my_project.demo.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.CertificateCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.CertificateResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Certificate;
import manage_student_system_v2.vutran.my_project.demo.Exception.AppException;
import manage_student_system_v2.vutran.my_project.demo.Exception.ErrorCode;
import manage_student_system_v2.vutran.my_project.demo.Mapper.CertificateMapper;
import manage_student_system_v2.vutran.my_project.demo.Repository.CertificateRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CertificateService {

    CertificateRepository certificateRepository;
    CertificateMapper certificateMapper;

    public CertificateResponse createCertificate(CertificateCreationRequest request){
        // check exist
        if(certificateRepository.exitsByNameCertificate(request.getNameCertificate())){
            throw new AppException(ErrorCode.CERTIFICATE_EXISTED);
        }
        Certificate certificate = certificateMapper.toCertificate(request);

        // save
        certificateRepository.save(certificate);

        return certificateMapper.toCertificateResponse(certificate);
    }

    public CertificateResponse getCertificateByName(String nameCertificate){
        Certificate certificate = certificateRepository.findByNameCertificate(nameCertificate).orElseThrow(()-> new AppException(ErrorCode.CERTIFICATE_NOT_FOUND));
        return certificateMapper.toCertificateResponse(certificate);
    }

    public Set<CertificateResponse> getListCertificate(){
        return certificateRepository.findAll().stream().map(certificateMapper::toCertificateResponse).collect(Collectors.toSet());
    }

    public String deleteCertificate(String idCertificate){
        Certificate certificate = certificateRepository.findById(idCertificate).orElseThrow(()-> new AppException(ErrorCode.CERTIFICATE_NOT_FOUND));
        // delete
        certificateRepository.deleteById(idCertificate);
        return "Deleted Certificate: " + certificate.getNameCertificate();
    }

}
