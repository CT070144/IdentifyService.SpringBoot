package com.ngvanphuc.identify_service.controller;

import com.ngvanphuc.identify_service.dto.request.*;
import com.ngvanphuc.identify_service.dto.response.AuthenticationResponse;
import com.ngvanphuc.identify_service.dto.response.IntrospectResponse;
import com.ngvanphuc.identify_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/token")
    APIResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
       var result =  authenticationService.authenticate(request);
       return APIResponse.<AuthenticationResponse>builder()
                       .result(result)
                       .build();

    }
    @PostMapping("/introspect")
    APIResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return APIResponse.<IntrospectResponse>builder()
                .code(1009)
                .result(result)
                .build();
    }
    @PostMapping("/logout")
    APIResponse<Void> logout(@RequestBody LogoutRequest request)
    throws ParseException,JOSEException{
        authenticationService.logout(request);
        return APIResponse.<Void>builder()
                .build();
    }
    @PostMapping("/refresh")
    APIResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
                return APIResponse.<AuthenticationResponse>builder()
                        .result(authenticationService.refreshToken(request))
                        .build();
    }

}
