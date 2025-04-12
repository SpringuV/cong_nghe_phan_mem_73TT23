package manage_student_system_v2.vutran.my_project.demo.Dto.Request;

import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {

    @Size(min = 8, message = "PASSWORD_INVALID")
    String oldPassword;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String newPassword;
}
