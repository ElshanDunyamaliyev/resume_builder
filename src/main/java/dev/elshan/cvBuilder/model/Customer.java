package dev.elshan.cvBuilder.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "summary")
    private String summary;

    @OneToMany(fetch = FetchType.LAZY)
    private List<EmploymentHistory> employmentHistory;

    @OneToMany(fetch = FetchType.LAZY)
    private List<EducationHistory> educationHistory;

    @OneToMany(fetch = FetchType.LAZY)
    private List<WebsiteLinks> websiteLinks;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Skill> skills;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Language> languages;
}
