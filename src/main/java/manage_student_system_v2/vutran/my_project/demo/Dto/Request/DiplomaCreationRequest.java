package manage_student_system_v2.vutran.my_project.demo.Dto.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiplomaCreationRequest {
    
    String major;
    String degreeType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate issueDate;
    String studentId;
}
