package manage_student_system_v2.vutran.my_project.demo.Dto.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Validator.DobConstraint;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentUpdateResponse {
    String studentId;
    String lastName;
    String firstName;
    String departmentName;
    String yearAdmission;
    String yearGraduation;
    String graduationStatus;
    String email;
    @DobConstraint(min = 18, message = "INVALID_DOB")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate updateAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate createAt;
    Set<CertificateResponse> certificates;
    Set<DiplomaResponse> diplomas;
}
