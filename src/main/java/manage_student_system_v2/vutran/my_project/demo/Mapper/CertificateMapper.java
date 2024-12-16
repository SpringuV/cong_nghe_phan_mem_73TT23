package manage_student_system_v2.vutran.my_project.demo.Mapper;

import manage_student_system_v2.vutran.my_project.demo.Dto.Request.CertificateCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.CertificateResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Certificate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CertificateMapper {

    Certificate toCertificate(CertificateCreationRequest request);

    CertificateResponse toCertificateResponse(Certificate certificate);


    void updateCertificate(@MappingTarget Certificate certificate, CertificateCreationRequest certificateCreationRequest);
}
