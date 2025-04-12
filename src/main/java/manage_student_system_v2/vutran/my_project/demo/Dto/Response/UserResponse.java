package manage_student_system_v2.vutran.my_project.demo.Dto.Response;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    String id;
    String username;
    String lastName;
    String firstName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;

    Set<RoleResponse> roles;
}
