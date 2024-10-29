package dev.elshan.cvBuilder.service;

import dev.elshan.cvBuilder.model.Resume;

public interface ResumeService {

    Resume findResumeById(Long id);
    byte[] getResumePdf(Long resumeId);
    void addResume(Resume resume);
    void updateResume(Long id, Resume resume);
}
