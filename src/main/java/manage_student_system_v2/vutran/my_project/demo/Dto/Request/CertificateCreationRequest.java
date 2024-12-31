package manage_student_system_v2.vutran.my_project.demo.Dto.Request;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CertificateCreationRequest {
    String nameCertificate;
    LocalDate issueDate; // ngay cap
    String description;
    String studentId;
    String certificateType;
}
