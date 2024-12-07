package manage_student_system_v2.vutran.my_project.demo.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "certificate")
public class Certificate {
    @Column(name = "certificate_id")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "name_certificate", unique = true)
    String nameCertificate;

    @Column(name = "issue_date", nullable = false)
    LocalDate issueDate; // ngay cap

    @Column(name = "description")
    String description;

    @Column(name = "certificate_type", nullable = false)
    String certificateType;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "certificate_student", joinColumns = @JoinColumn(name = "certificate_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    @JsonManagedReference
    Set<Student> studentSet;
}
