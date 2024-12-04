package manage_student_system_v2.vutran.my_project.demo.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(101, "Uncategorized exception !", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(102, "User existed !", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(103, "User not found !", HttpStatus.NOT_FOUND),
    UN_AUTHENTICATED(104, "Un Authenticated", HttpStatus.UNAUTHORIZED),
    USER_NOT_EXIST(105, "User not exist", HttpStatus.BAD_REQUEST),
    UN_AUTHORIZED(106, "UnAuthorized", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(107, "Token Invalid or Expired", HttpStatus.UNAUTHORIZED),
    PASSWORD_INVALID(108, "Password at least {min} characters", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(109, "Username at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_KEY(110, "Uncategorized error", HttpStatus.BAD_REQUEST),
    INVALID_DOB(111, "Your name must be at least {min}", HttpStatus.BAD_REQUEST)
    ;

    ErrorCode(int code, String message, HttpStatus httpStatus){
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }


    private int code;
    private String message;
    private HttpStatus httpStatus;
}
