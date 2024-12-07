package manage_student_system_v2.vutran.my_project.demo.Dto.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiplomaResponse {

    String diplomaId;
    String major;
    String degreeType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate issueDate;
    Set<StudentResponse> students;

}
