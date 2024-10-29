package dev.elshan.cvBuilder.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "resume")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "summary", length = 1000)
    private String summary;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<EmploymentHistory> employmentHistory;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<EducationHistory> educationHistory;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<WebsiteLinks> websiteLinks;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Skill> skills;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Language> languages;
}
