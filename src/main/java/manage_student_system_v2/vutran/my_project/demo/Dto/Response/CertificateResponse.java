package manage_student_system_v2.vutran.my_project.demo.Dto.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CertificateResponse {
    String id;
    String nameCertificate;
    @JsonFormat(pattern = "yy-MM-dd")
    LocalDate issueDate; // ngay cap
    String description;
    String certificateType;
    String studentId;
}
