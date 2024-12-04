package manage_student_system_v2.vutran.my_project.demo.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
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

    @Column(name = "position")
    String position;

    @Column(name = "dob")
    LocalDate dob;

    @ManyToMany
    Set<Role> roles;
}