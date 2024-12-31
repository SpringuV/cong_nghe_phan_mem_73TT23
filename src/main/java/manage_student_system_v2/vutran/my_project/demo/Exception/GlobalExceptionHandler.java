package manage_student_system_v2.vutran.my_project.demo.Exception;

import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException appException){
        log.error("Exception: ", appException);
        return ResponseEntity.status(appException.getErrorCode().getHttpStatus())
                .body(ApiResponse
                        .builder()
                        .code(appException.getErrorCode().getCode())
                        .result(appException.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValid(MethodArgumentNotValidException notValidException){
        log.error("Exception: ", notValidException);
        // get enum key
        String enumKey = notValidException.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY; // giá trị mặc định, vd khi ghi sai

        Map<String, Object> attribute = null;

        // get message of key
        try{
            errorCode = ErrorCode.valueOf(enumKey);

            //get object chứa attribute cần thiết
            var constraintViolation = notValidException.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            attribute = constraintViolation.getConstraintDescriptor().getAttributes();
            log.info("Attribute: {}", attribute.toString());
        } catch (IllegalArgumentException e){
            log.error(e.getMessage());
        }

        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .message(Objects.nonNull(attribute) ? mapAttribute(errorCode.getMessage(), attribute) : errorCode.getMessage())
                        .code(errorCode.getCode())
                        .build()
        );
    }

    private String mapAttribute(String message, Map<String, Object> attribute){
        String min_Value = String.valueOf(attribute.get(MIN_ATTRIBUTE));
        // dùng ngoặc nhọn vì đánh dấu đây ko phải là string thông thường
        return message.replace("{"+MIN_ATTRIBUTE+"}", min_Value);
    }


    @ExceptionHandler(value = IllegalArgumentException.class)
    ResponseEntity<ApiResponse> handlingIllegalArgument(IllegalArgumentException argumentException){
        return ResponseEntity.badRequest()
                .body(ApiResponse.<Void>builder()
                        .message(argumentException.getMessage())
                        .build()
                    );
    }
}
