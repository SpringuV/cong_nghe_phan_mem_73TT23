package manage_student_system_v2.vutran.my_project.demo.Dto.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

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
    String position;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;

    Set<RoleResponse> roles;
}
