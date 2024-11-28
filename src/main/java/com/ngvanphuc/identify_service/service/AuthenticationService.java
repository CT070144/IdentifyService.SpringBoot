package com.ngvanphuc.identify_service.service;

import com.ngvanphuc.identify_service.dto.request.AuthenticationRequest;
import com.ngvanphuc.identify_service.dto.request.IntrospectRequest;
import com.ngvanphuc.identify_service.dto.request.LogoutRequest;
import com.ngvanphuc.identify_service.dto.response.AuthenticationResponse;
import com.ngvanphuc.identify_service.dto.response.IntrospectResponse;
import com.ngvanphuc.identify_service.exception.AppException;
import com.ngvanphuc.identify_service.exception.ErrorCode;
import com.ngvanphuc.identify_service.models.InvalidatedToken;
import com.ngvanphuc.identify_service.models.User;
import com.ngvanphuc.identify_service.repository.InvalidatedTokenRepository;
import com.ngvanphuc.identify_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor //Tự động tạo constructor cho lớp dựa trên các trường final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean inValid=true;
       try {
           verifyToken(token); // builded sai thì thows exception đúng thì return valid(true)
       }
       catch (AppException e){
          inValid = false;
       }
        return     IntrospectResponse.builder()
                .valid(inValid)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXIT));
       boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
       if(!authenticated)
           throw new AppException(ErrorCode.UNAUTHENTICATED);
       String token = generateToken(user);
       return AuthenticationResponse.builder()
               .token(token)
               .authenticated(true)
               .build();
    }

    private String generateToken(User user)  {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("phuc.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(24, ChronoUnit.HOURS).toEpochMilli()))
//                .expirationTime(new Date(System.currentTimeMillis() - 1000)) hết hạn ngay lập tức
                .claim("scope", buildRole(user))
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(jwtClaimSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);

        try {
            jwsObject.sign(new MACSigner(SIGER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            System.out.println("Error token");
            throw new RuntimeException(e);
        }
    }
    private String buildRole(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_"+role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions()))
                role.getPermissions()
                        .forEach(permission -> stringJoiner.add(permission.getName()));
            });
        return stringJoiner.toString();
    }
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();
        invalidatedTokenRepository.save(invalidatedToken);
    }
    private  SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
        Date exprityTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);

        if(!(verified && exprityTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        // Kiểm tra xem token có nằm trong bảng invalidatedToken
        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

}
