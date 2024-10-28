package dev.elshan.cvBuilder.repository;

import dev.elshan.cvBuilder.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill,Long> {
}
