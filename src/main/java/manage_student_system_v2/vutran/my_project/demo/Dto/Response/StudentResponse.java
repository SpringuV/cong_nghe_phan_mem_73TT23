package manage_student_system_v2.vutran.my_project.demo.Dto.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    String id;
    String username;
    String lastName;
    String firstName;

    String studentId;
    String className;
    String nameDepartment;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;

    Set<RoleResponse> roles;
}
