package manage_student_system_v2.vutran.my_project.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // muốn giữ lại cấu trúc tuần tự hóa nhưng tránh vòng lặp bằng cách sử dụng định danh
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "lastName")
    String lastName;

    @Column(name = "firstName")
    String firstName;

    @Column(name = "dob")
    LocalDate dob;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"), // Khóa chính của User
            inverseJoinColumns = @JoinColumn(name = "roles_name") // Khóa chính của Role
    )
    @JsonBackReference
    Set<Role> roles;
}
