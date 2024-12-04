package manage_student_system_v2.vutran.my_project.demo.Dto.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreationRequest {

    String name;
    String description;

    Set<String> permissions;
}
