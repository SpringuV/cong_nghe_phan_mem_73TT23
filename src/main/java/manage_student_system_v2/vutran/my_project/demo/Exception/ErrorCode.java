package manage_student_system_v2.vutran.my_project.demo.Exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(101, "Uncategorized exception !", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(102, "User existed !", HttpStatus.BAD_REQUEST),
    STUDENT_EXISTED(102, "Student existed !", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(103, "User not found !", HttpStatus.NOT_FOUND),
    UN_AUTHENTICATED(104, "Un Authenticated", HttpStatus.UNAUTHORIZED),
    USER_NOT_EXIST(105, "User not exist", HttpStatus.BAD_REQUEST),
    UN_AUTHORIZED(106, "UnAuthorized", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(107, "Token Invalid or Expired", HttpStatus.UNAUTHORIZED),
    PASSWORD_INVALID(108, "Password at least {min} characters", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(109, "Username at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_KEY(110, "Uncategorized error", HttpStatus.BAD_REQUEST),
    INVALID_DOB(111, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_OLD_PASSWORD(112, "Your old password incorrect !", HttpStatus.BAD_REQUEST),
    STUDENT_NOT_FOUND(113, "Student not found !", HttpStatus.NOT_FOUND),
    DIPLOMA_NOT_FOUND(103, "Diploma not found !", HttpStatus.NOT_FOUND),
    DIPLOMA_EXISTED(102, "Diploma existed !", HttpStatus.BAD_REQUEST),
    DEPARTMENT_NOT_FOUND(103, "Department not found !", HttpStatus.NOT_FOUND),
    CERTIFICATE_EXISTED(102, "Certificate existed !", HttpStatus.BAD_REQUEST),
    CERTIFICATE_NOT_FOUND(103, "Certificate not found !", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(103, "Role not found !", HttpStatus.NOT_FOUND),
    PERMISSION_NOT_FOUND(103, "Permission not found !", HttpStatus.NOT_FOUND);

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    private int code;
    private String message;
    private HttpStatus httpStatus;
}
