package manage_student_system_v2.vutran.my_project.demo.Dto.Request;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;
import manage_student_system_v2.vutran.my_project.demo.Validator.DobConstraint;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    String username;
    String lastName;
    String firstName;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;

    List<String> roles;
}
