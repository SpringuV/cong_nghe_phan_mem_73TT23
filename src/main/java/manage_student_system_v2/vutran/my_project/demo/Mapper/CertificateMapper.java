package manage_student_system_v2.vutran.my_project.demo.Mapper;

import manage_student_system_v2.vutran.my_project.demo.Dto.Request.CertificateCreateInStudentRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.CertificateCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.CertificateResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Certificate;
import manage_student_system_v2.vutran.my_project.demo.Entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CertificateMapper {

    @Mapping(target = "student", ignore = true)
    Certificate toCertificate(CertificateCreationRequest request);

    Certificate toCertificateFromStudentRequest(CertificateCreateInStudentRequest request);

    @Mapping(target = "studentId", source = "student.studentId")
    CertificateResponse toCertificateResponse(Certificate certificate);

    void updateCertificate(@MappingTarget Certificate certificate, CertificateCreationRequest certificateCreationRequest);
}
