package manage_student_system_v2.vutran.my_project.demo.Dto.Request;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
