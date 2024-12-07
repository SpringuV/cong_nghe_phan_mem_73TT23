package manage_student_system_v2.vutran.my_project.demo.Dto.Request;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentCreationRequest {

    String nameDepartment;
    String description;
    String contactEmail;

}
