package manage_student_system_v2.vutran.my_project.demo.Dto.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class StudentUpdateRequest {
    String graduationStatus;
    String lastName;
    String firstName;
    String departmentName;
    String yearAdmission;
    String yearGraduation;
    String email;
    @DobConstraint(min = 18, message = "INVALID_DOB")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
    List<CertificateCreateInStudentRequest> certificateList;
    List<DiplomaCreateInStudentRequest> diplomaList;
}
