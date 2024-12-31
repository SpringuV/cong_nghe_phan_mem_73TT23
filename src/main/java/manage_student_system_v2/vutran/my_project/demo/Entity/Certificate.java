package manage_student_system_v2.vutran.my_project.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // muốn giữ lại cấu trúc tuần tự hóa nhưng tránh vòng lặp bằng cách sử dụng định danh
@Table(name = "certificate")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name_certificate", unique = true, nullable = false)
    String nameCertificate;

    @Column(name = "issue_date")
    LocalDate issueDate; // ngay cap

    @Column(name = "description")
    String description;

    @Column(name = "certificate_type", nullable = false)
    String certificateType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "student_id")
    @JsonBackReference
    Student student;
}
