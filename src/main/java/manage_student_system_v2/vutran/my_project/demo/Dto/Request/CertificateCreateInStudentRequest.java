package manage_student_system_v2.vutran.my_project.demo.Dto.Request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CertificateCreateInStudentRequest {

    String nameCertificate;
    LocalDate issueDate; // ngay cap
    String description;
    String certificateType;
}
