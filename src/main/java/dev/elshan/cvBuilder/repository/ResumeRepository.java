package dev.elshan.cvBuilder.repository;

import dev.elshan.cvBuilder.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume,Long> {
}
