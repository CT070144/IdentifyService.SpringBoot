package com.ngvanphuc.identify_service.dto.request;

import com.ngvanphuc.identify_service.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min=3, message = "USERNAME_INVALID")
    //throw MethodArgumentNotValidException. That is handled in GlobalExceptionHandle.java
     String username;
    @Size(min = 3, message = "PASSWORD_INVALID")
     String password;
     String firstName;
     String lastName;
     @DobConstraint(min = 18, message = "INVALID_DOB")
     LocalDate dob;


}
