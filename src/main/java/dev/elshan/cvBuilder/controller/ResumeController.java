package dev.elshan.cvBuilder.controller;

import dev.elshan.cvBuilder.model.Resume;
import dev.elshan.cvBuilder.service.ResumeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService service;

    @GetMapping("/{id}")
    public Resume getResumeById(@PathVariable Long id){
        return service.findResumeById(id);
    }

    @GetMapping("/{id}/resume")
    @SneakyThrows
    public void getResume(@PathVariable Long id, HttpServletResponse response){
        var pdf = service.getResumePdf(id);
        response.setHeader(CONTENT_TYPE, "application/pdf");
        response.setHeader(CONTENT_DISPOSITION, "attachment;filename=" + "contract.pdf");
        IOUtils.copy(new ByteArrayInputStream(pdf), response.getOutputStream());
    }

    @PostMapping
    public String addResume(@RequestBody Resume resume){
        service.addResume(resume);
        return "ok";
    }

    @PutMapping("/{id}")
    public String updateResume(@PathVariable Long id, @RequestBody Resume resume){
        service.updateResume(id,resume);
        return "ok";
    }
}
