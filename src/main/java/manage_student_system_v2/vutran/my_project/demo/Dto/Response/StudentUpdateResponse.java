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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentUpdateResponse {

    @Size(min = 8, message = "USERNAME_INVALID")
    String username;
    String studentId;
    String className;
    String lastName;
    String firstName;
    @DobConstraint(min = 18, message = "INVALID_DOB")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
    LocalDate updateAt;

    List<CertificateResponse> certificates;
    List<DiplomaResponse> diplomas;
    DepartmentResponse department;

}