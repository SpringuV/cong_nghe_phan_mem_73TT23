package manage_student_system_v2.vutran.my_project.demo.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "department")
public class Department {

    @Id
    @Column(name = "name_department", nullable = false, unique = true)
    String nameDepartment;

    @Column(name = "description")
    String description;

    @Column(name = "contact_email", nullable = false)
    String contactEmail;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "departments_students", joinColumns = @JoinColumn(name = "department_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    @JsonManagedReference
    Set<Student> studentSet;
}
