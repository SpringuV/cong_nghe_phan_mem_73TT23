package manage_student_system_v2.vutran.my_project.demo.Configuration;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import manage_student_system_v2.vutran.my_project.demo.Constant.PredefinedRole;
import manage_student_system_v2.vutran.my_project.demo.Entity.Role;
import manage_student_system_v2.vutran.my_project.demo.Entity.User;
import manage_student_system_v2.vutran.my_project.demo.Repository.RoleRepository;
import manage_student_system_v2.vutran.my_project.demo.Repository.UserRepository;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfiguration {

    PasswordEncoder passwordEncoder;

    static final String ADMIN_USER_NAME = "admin";
    static final String ADMIN_PASSWORD = "1234";

    @Bean // khi có class name của mysql thì mới init bean lên, không thì sẽ dùng driverClass của H2 để test
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        log.info("Init application.......");
        return args -> {
            // Tạo role USER nếu chưa có
            log.info("Start check role user");
            if (!roleRepository.existsById(PredefinedRole.USER_ROLE)) {
                roleRepository.save(Role.builder()
                        .name(PredefinedRole.USER_ROLE)
                        .description("user role")
                        .build());
                log.info("USER role has been created.");
            }

            // Tạo role ADMIN nếu chưa có
            log.info("Start check role admin");
            Role adminRole = roleRepository.findById(PredefinedRole.ADMIN_ROLE).orElseGet(() -> {
                Role newAdminRole = Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Role admin")
                        .build();
                log.info("ADMIN role has been created.");
                return roleRepository.save(newAdminRole);
            });

            // Nếu chưa có user admin thì tạo mới
            log.info("Start check username");
            if (!userRepository.existsByUsername(ADMIN_USER_NAME)) {
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);

                User user = User.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();
                log.info("create user admin");
                userRepository.save(user);
                log.warn("Admin user has been created with default password: 1234, please change it.");
            }
        };
    }
}
