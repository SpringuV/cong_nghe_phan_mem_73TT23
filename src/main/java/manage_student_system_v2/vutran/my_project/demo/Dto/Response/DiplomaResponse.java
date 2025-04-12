package manage_student_system_v2.vutran.my_project.demo.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiplomaResponse {
    String diplomaId;
    String major;
    String degreeType;
    String issueDate;
    String studentId;
}
