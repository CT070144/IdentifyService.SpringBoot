package com.ngvanphuc.identify_service.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(100,"User existed",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003,"Username must be at least {min} charaters",HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1002,"Password must be at least {min} characters",HttpStatus.BAD_REQUEST),
    KEY_INVALID(1001,"INVALID KEY", HttpStatus.BAD_REQUEST),
    USER_NOT_EXIT(1007, "USER NOT EXIT",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1005,"UNAUTHENTICATED",HttpStatus.UNAUTHORIZED),
    UNAUTHORRIZED(1006,"YOU DO NOT HAVE PERMISSION",HttpStatus.FORBIDDEN),
    INVALID_DOB(1010,"Your age must be at least {min}",HttpStatus.BAD_REQUEST)
;
    private int code;
    private String message;
    private HttpStatusCode statusCode;
    ErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
