package manage_student_system_v2.vutran.my_project.demo.Validator;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// lớp này mới chỉ là khai báo, cần 1 lớp để xử lý sự kiện

// target là cho apply ở đâu,
@Target({ElementType.FIELD}) // chỉ kiểm tra trong thuộc tính của đối tượng
@Retention(RetentionPolicy.RUNTIME) // annotation được xử lý lúc nào
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {
    // 3 cái cơ bản của validation
    String message() default "Invalid date of birth !";

    // config age
    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
