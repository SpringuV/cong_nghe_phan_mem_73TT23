package manage_student_system_v2.vutran.my_project.demo.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.CertificateCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.CertificateResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Certificate;
import manage_student_system_v2.vutran.my_project.demo.Entity.Student;
import manage_student_system_v2.vutran.my_project.demo.Exception.AppException;
import manage_student_system_v2.vutran.my_project.demo.Exception.ErrorCode;
import manage_student_system_v2.vutran.my_project.demo.Mapper.CertificateMapper;
import manage_student_system_v2.vutran.my_project.demo.Repository.CertificateRepository;
import manage_student_system_v2.vutran.my_project.demo.Repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class CertificateService {

    CertificateRepository certificateRepository;
    StudentRepository studentRepository;
    CertificateMapper certificateMapper;

    public CertificateResponse createCertificate(CertificateCreationRequest request){
        // check student
        Student student = studentRepository.findByStudentId(request.getStudentId()).orElseThrow(()-> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        Certificate certificate = certificateMapper.toCertificate(request);
        certificate.setStudent(student);

        // save
        certificateRepository.save(certificate);

        return certificateMapper.toCertificateResponse(certificate);
    }

    public CertificateResponse getCertificateById(String id){
        return certificateRepository.findById(id).map(certificateMapper::toCertificateResponse).orElseThrow(() -> new AppException(ErrorCode.CERTIFICATE_NOT_FOUND));
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

    public CertificateResponse updateCertificate(String id, CertificateCreationRequest certificateCreationRequest){
        // check id
        Certificate certificate = certificateRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CERTIFICATE_NOT_FOUND));
        certificateMapper.updateCertificate(certificate, certificateCreationRequest);

        certificate.setStudent(studentRepository.findByStudentId(certificateCreationRequest.getStudentId()).orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND)));
        // save
        certificateRepository.save(certificate);
        return certificateMapper.toCertificateResponse(certificate);
    }

}
