package manage_student_system_v2.vutran.my_project.demo.Service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.AuthenticationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.IntrospectRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.LogoutRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.RefreshTokenRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.AuthenticationResponse;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.IntrospectResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.InvalidatedToken;
import manage_student_system_v2.vutran.my_project.demo.Entity.User;
import manage_student_system_v2.vutran.my_project.demo.Exception.AppException;
import manage_student_system_v2.vutran.my_project.demo.Exception.ErrorCode;
import manage_student_system_v2.vutran.my_project.demo.Repository.InvalidatedTokenRepository;
import manage_student_system_v2.vutran.my_project.demo.Repository.UserRepository;
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
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;

    @NonFinal // nonfinal vì @RequiredArgsConstructor tạo constructor tự động chỉ cho các trường final.
    // Các trường không final, như SIGNER_KEY, được inject bởi @Value và yêu cầu Spring xử lý thông qua setter
    // hoặc reflection. Nếu chưa được xử lý đúng, có thể gây lỗi.
    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;


    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        User user = userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // verify password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if(!authenticated){
            throw  new AppException(ErrorCode.UN_AUTHENTICATED);
        }

        // generate token
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }


    private String generateToken(User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("vu_tran.project")
                .issueTime(new Date())
                .claim("scope", buildScope(user))
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload =new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject =new JWSObject(jwsHeader, payload);

        // ki so
        try{
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize(); //serialize(): Chuyển đổi JWT đã ký thành chuỗi định dạng Base64Url (header.payload.signature).
        } catch (JOSEException e){
            log.error("Cannot generate token", e);
            throw new RuntimeException(e);
        }
    }

    // introspect
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException{
        String token = request.getToken();
        boolean isValid = true;

        try{
            verifyToken(token, false);
        } catch (AppException e){
            isValid = false;
        }

        return IntrospectResponse.builder().isValid(isValid).build();
    }

    // logout
    public void logout(LogoutRequest logoutRequest) throws JOSEException, ParseException{
        try {
            // phải refresh vì ngăn chặn việc sử dụng lại token khi logout
            var signToken = verifyToken(logoutRequest.getToken(), true);

            String jit =signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .expiryTime(expiryTime)
                    .id(jit)
                    .build();

            // save invalidatedToken
            invalidatedTokenRepository.save(invalidatedToken);

        } catch (AppException e){
            log.info("Token already expired");
            throw new AppException(ErrorCode.UN_AUTHENTICATED);
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh) ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli()) : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);

        if(!(verified && expiryTime.after(new Date()))){
            throw new AppException(ErrorCode.UN_AUTHENTICATED);
        }

        // nếu tồn tại các token đã hết hạn hoặc đã đăng xuất
        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.UN_AUTHENTICATED);
        }
        return signedJWT;
    }

    private String buildScope(User user){
        StringJoiner joiner = new StringJoiner(" ");

        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role -> {
                joiner.add("ROLE_" + role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissionSet())){
                    role.getPermissionSet().forEach(permission -> joiner.add(permission.getName()));
                }
            });
        }

        return joiner.toString();
    }

    public AuthenticationResponse refresh(RefreshTokenRequest tokenRequest) throws ParseException, JOSEException{
        SignedJWT signedToken = verifyToken(tokenRequest.getToken(), true);
        String jit =signedToken.getJWTClaimsSet().getJWTID();
        Date expiryTime =signedToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        String username =signedToken.getJWTClaimsSet().getSubject();
        var user =userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }
}