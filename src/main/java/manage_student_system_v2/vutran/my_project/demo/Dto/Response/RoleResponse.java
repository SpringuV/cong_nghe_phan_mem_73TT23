package manage_student_system_v2.vutran.my_project.demo.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoleResponse {

    String name;
    String description;

    Set<PermissionResponse> permissionResponseSet;
}
