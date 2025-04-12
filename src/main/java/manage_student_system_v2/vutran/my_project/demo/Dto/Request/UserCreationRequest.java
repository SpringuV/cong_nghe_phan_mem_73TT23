package manage_student_system_v2.vutran.my_project.demo.Dto.Request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Validator.DobConstraint;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {
    @Size(min = 8, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;

    String lastName;
    String firstName;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
}
