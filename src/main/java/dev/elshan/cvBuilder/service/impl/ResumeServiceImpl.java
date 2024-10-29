package dev.elshan.cvBuilder.service.impl;

import dev.elshan.cvBuilder.mapper.ResumeMapper;
import dev.elshan.cvBuilder.model.Resume;
import dev.elshan.cvBuilder.repository.ResumeRepository;
import dev.elshan.cvBuilder.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static dev.elshan.cvBuilder.pdf.ResumePdf.getPdf;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository repository;
    private final ResumeMapper mapper;

    @Override
    public Resume findResumeById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND,"There is no resume found with given id"));
    }

    @Override
    public byte[] getResumePdf(Long resumeId) {
        var resume = findResumeById(resumeId);
        return getPdf(resume);
//        return Base64.getEncoder().encodeToString(pdf);
    }


    @Override
    public void addResume(Resume resume) {
        repository.save(resume);
    }

    @Override
    public void updateResume(Long id, Resume resume) {
        Resume foundedResume = findResumeById(id);
        mapper.updateEntity(foundedResume, resume);
        repository.save(foundedResume);
    }
}
