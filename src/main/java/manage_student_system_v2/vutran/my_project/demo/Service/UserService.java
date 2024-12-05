package manage_student_system_v2.vutran.my_project.demo.Service;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import manage_student_system_v2.vutran.my_project.demo.Constant.PredefinedRole;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.UserCreationRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Request.UserUpdateRequest;
import manage_student_system_v2.vutran.my_project.demo.Dto.Response.UserResponse;
import manage_student_system_v2.vutran.my_project.demo.Entity.Role;
import manage_student_system_v2.vutran.my_project.demo.Entity.User;
import manage_student_system_v2.vutran.my_project.demo.Exception.AppException;
import manage_student_system_v2.vutran.my_project.demo.Exception.ErrorCode;
import manage_student_system_v2.vutran.my_project.demo.Mapper.UserMapper;
import manage_student_system_v2.vutran.my_project.demo.Repository.RoleRepository;
import manage_student_system_v2.vutran.my_project.demo.Repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest userCreationRequest){
        // check User exist
        if(userRepository.existsByUsername(userCreationRequest.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(userCreationRequest);
        Set<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);

        // set password
        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getListUser(){
        log.info("In method get List User");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    // Người dùng được phép cập nhật nếu họ là chủ sở hữu của User hoặc admin
    @PostAuthorize("returnObject.id == #id || hasRole('ADMIN')")
    public UserResponse updateUser(String id,UserUpdateRequest request){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        userMapper.updateUser(user, request);

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PostAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse getMyInfo(){
        // sau khi user login success sẽ được lưu thông tin đăng nhập tại security context holder
        SecurityContext context = SecurityContextHolder.getContext();
        String name =context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXIST));
        return userMapper.toUserResponse(user);
    }

}
