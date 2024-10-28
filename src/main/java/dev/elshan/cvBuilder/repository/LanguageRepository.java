package dev.elshan.cvBuilder.repository;

import dev.elshan.cvBuilder.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language,Long> {
}
