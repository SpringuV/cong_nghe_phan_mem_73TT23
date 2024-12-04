package manage_student_system_v2.vutran.my_project.demo.Dto.Response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // nhung field null se khong duoc hien thi
public class ApiResponse <T> {
    @Builder.Default
    private int code = 100;
    private String message;
    private T result;
}
