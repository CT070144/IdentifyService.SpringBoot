package com.ngvanphuc.identify_service.controller;

import com.ngvanphuc.identify_service.dto.request.APIResponse;
import com.ngvanphuc.identify_service.dto.request.UserCreationRequest;
import com.ngvanphuc.identify_service.dto.request.UserUpdateRequest;
import com.ngvanphuc.identify_service.dto.response.UserResponse;
import com.ngvanphuc.identify_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    APIResponse<UserResponse> createUser(@RequestBody @Valid  UserCreationRequest request){
       return APIResponse.<UserResponse>builder()
               .result(userService.createUser(request))
               .build();
    }
    @GetMapping
    APIResponse<List<UserResponse>> getUsers(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getName()+"");
        authentication.getAuthorities().forEach(role->log.info(role.getAuthority()));
         return APIResponse.<List<UserResponse>>builder()
                 .result(userService.getUsers())
                 .build();
    }


    @GetMapping("/{userId}")
    APIResponse<UserResponse> getUser(@PathVariable("userId") String userId){
        return APIResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();

    }
    @GetMapping("/myinfo")
    APIResponse<UserResponse> getInfo(){
         return APIResponse.<UserResponse>builder()
                 .result(userService.getInfo())
                 .build();
    }
    @PutMapping("/{userId}")
    APIResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request){
        return APIResponse.<UserResponse>builder()
                .result(userService.updateUser(userId,request))
                .build();
    }
    @DeleteMapping("/{userId}")
    APIResponse<String> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return APIResponse.<String>builder()
                .result("user has been deleted")
                .build();
    }

}
