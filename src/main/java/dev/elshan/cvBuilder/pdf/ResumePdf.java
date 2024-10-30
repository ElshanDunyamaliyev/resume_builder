package dev.elshan.cvBuilder.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dev.elshan.cvBuilder.model.Language;
import dev.elshan.cvBuilder.model.Resume;
import dev.elshan.cvBuilder.model.Skill;
import dev.elshan.cvBuilder.model.WebsiteLinks;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.itextpdf.text.Element.*;

@Service
public class ResumePdf {

    @SneakyThrows
    public static byte[] getPdf(Resume resume){

        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Font bold13 = generateTimesNewRomanFont(13, Font.BOLD, BaseColor.BLACK);
        Font bold14 = generateTimesNewRomanFont(14, Font.BOLD, BaseColor.BLACK);

        Font normalEleven = generateTimesNewRomanFont(11, Font.NORMAL, BaseColor.BLACK);
        Font greenEleven = generateTimesNewRomanFont(11, Font.NORMAL, BaseColor.GREEN);

        PdfWriter.getInstance(document, outputStream);

        document.open();
        var headLineTxt = resume.getFirstName() + " " + resume.getLastName() + ", " + resume.getJobTitle();
        Paragraph paragraph = createParagraph(headLineTxt, bold14, ALIGN_CENTER, 1);
        document.add(paragraph);

        var locationTxt = resume.getCity() + ", " + resume.getCountry() + ", " + resume.getPhone() + ", " + resume.getEmail();
        paragraph = createParagraph(locationTxt, bold13, ALIGN_CENTER, 1);
        document.add(paragraph);

        var linksTxt = addCell(resume,"LINKS",resume.getWebsiteLinks().stream().map(WebsiteLinks::getName).toList());
        document.add(linksTxt);

        var profileTxt = addCell(resume,"PROFILE",List.of(resume.getSummary()));
        document.add(profileTxt);

        var employmentHistoryTxt = addCellForEmp(resume);
        document.add(employmentHistoryTxt);

        var educationHistoryTxt = addCellForEducationHistory(resume);
        document.add(educationHistoryTxt);

        var skillsTxt = createSkillsTable("SKILLS",resume.getSkills().stream().map(Skill::getName).toList());
        document.add(skillsTxt);

        var languagesTxt = createSkillsTable("LANGUAGES",resume.getLanguages().stream().map(Language::getName).toList());
        document.add(languagesTxt);

        document.close();
        outputStream.close();
        return outputStream.toByteArray();
    }

    static PdfPTable createSkillsTable(String header, List<String> skills) throws Exception {
        Font boldTwelve = generateTimesNewRomanFont(14, Font.BOLD, BaseColor.BLACK);
        Font regularTwelve = generateTimesNewRomanFont(12, Font.NORMAL, BaseColor.BLACK);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        float[] columnWidths = {0.3f, 0.7f};
        table.setWidths(columnWidths);

        PdfPCell leftCell = new PdfPCell(new Paragraph(header, boldTwelve));
        leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftCell.setBorder(PdfPCell.NO_BORDER);

        PdfPTable nestedTable = new PdfPTable(2);
        nestedTable.setWidthPercentage(100);

        for (int i = 0; i < skills.size(); i++) {
            PdfPCell skillCell = new PdfPCell(new Paragraph(skills.get(i), regularTwelve));
            skillCell.setBorder(PdfPCell.NO_BORDER);
            skillCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            nestedTable.addCell(skillCell);

            if (i == skills.size() - 1 && skills.size() % 2 != 0) {
                PdfPCell emptyCell = new PdfPCell(new Paragraph(""));
                emptyCell.setBorder(PdfPCell.NO_BORDER);
                nestedTable.addCell(emptyCell);
            }
        }

        PdfPCell rightCell = new PdfPCell(nestedTable);
        rightCell.setPaddingBottom(10f);
        rightCell.setBorder(PdfPCell.NO_BORDER);

        table.addCell(leftCell);
        table.addCell(rightCell);

        return table;
    }

    static PdfPTable addCellForEducationHistory(Resume resume) throws DocumentException, IOException {
        Font dateFont = generateTimesNewRomanFont(14, Font.BOLD, BaseColor.BLACK);
        Font empFont = generateTimesNewRomanFont(12, Font.NORMAL, BaseColor.BLACK);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        PdfPCell headerCell = new PdfPCell(new Paragraph("Education History", dateFont));
        headerCell.setPaddingBottom(10f);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setColspan(2);
        headerCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(headerCell);

        float[] columnWidths = {0.3f, 0.7f};
        table.setWidths(columnWidths);

        resume.getEducationHistory().forEach((educationHistory) -> {
            var startDate = educationHistory.getStartDate().getMonth().toString().substring(0,3) + " " + educationHistory.getStartDate().getYear();
            var endDate = educationHistory.getEndDate().getMonth().toString().substring(0,3) + " " + educationHistory.getEndDate().getYear();
            PdfPCell leftCell = new PdfPCell(new Paragraph(startDate + " - " + endDate, empFont));
            leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            leftCell.setBorder(PdfPCell.NO_BORDER);

            var educationDescription =  educationHistory.getSpeciality() + ", " + educationHistory.getSchool();
            PdfPCell rightCell = new PdfPCell(new Paragraph(String.join(", ", educationDescription), empFont));
            rightCell.setHorizontalAlignment(ALIGN_LEFT);
            rightCell.setPaddingBottom(10f);
            rightCell.setBorder(PdfPCell.NO_BORDER);

            table.addCell(leftCell);
            table.addCell(rightCell);
        });

        table.setSpacingBefore(15);

        return table;
    }

    static PdfPTable addCellForEmp(Resume resume) throws DocumentException, IOException {
        // Font definitions
        Font dateFont = generateTimesNewRomanFont(14, Font.BOLD, BaseColor.BLACK);
        Font empDateFont = generateTimesNewRomanFont(12, Font.NORMAL, BaseColor.BLACK);
        Font empDescriptionFont = generateTimesNewRomanFont(13, Font.NORMAL, BaseColor.BLACK);

// Create a table with two columns
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

// Header for the employment history
        PdfPCell headerCell = new PdfPCell(new Paragraph("Employment History", dateFont));
        headerCell.setPaddingBottom(10f);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setColspan(2);
        headerCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(headerCell);

// Set column widths
        float[] columnWidths = {0.3f, 0.7f}; // Adjust widths for two columns
        table.setWidths(columnWidths);

// Iterate through each employment history entry
        resume.getEmploymentHistory().forEach((employmentHistory) -> {
            // Format start and end dates
            var startDate = employmentHistory.getStartDate().getMonth().toString().substring(0, 3) + " " + employmentHistory.getStartDate().getYear();
            var endDate = employmentHistory.getEndDate().getMonth().toString().substring(0, 3) + " " + employmentHistory.getEndDate().getYear();

            // Create left cell with formatted dates
            PdfPCell leftCell = new PdfPCell(new Paragraph(startDate + " - " + endDate, empDateFont));
            leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            leftCell.setBorder(PdfPCell.NO_BORDER);

            // Create right cell for job title and employer
            var jobTitleAndEmployer = employmentHistory.getJobTitle() + " - " + employmentHistory.getEmployer();
            var description = employmentHistory.getDescription();

            // Create a paragraph with job title and employer on the first line
            Paragraph jobParagraph = new Paragraph(jobTitleAndEmployer, empDescriptionFont);
            // Create a paragraph for the description on the second line
            Paragraph descriptionParagraph = new Paragraph(description, empDescriptionFont);

            // Create a new PdfPCell for the right cell and add the paragraphs
            PdfPCell rightCell = new PdfPCell();
            rightCell.addElement(jobParagraph);
            rightCell.addElement(descriptionParagraph);
            rightCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            rightCell.setBorder(PdfPCell.NO_BORDER);

//            leftCell.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
//            rightCell.setVerticalAlignment(ALIGN_TOP);

            table.addCell(leftCell);
            table.addCell(rightCell);
        });

        table.setSpacingBefore(10);
        return table;
    }

    static PdfPTable addCellForEmploymentHistory(Resume resume) throws DocumentException, IOException {
        Font dateFont = generateTimesNewRomanFont(14, Font.BOLD, BaseColor.BLACK);
        Font empDateFont = generateTimesNewRomanFont(12, Font.NORMAL, BaseColor.BLACK);
        Font empDescriptionFont = generateTimesNewRomanFont(13, Font.NORMAL, BaseColor.BLACK);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        PdfPCell headerCell = new PdfPCell(new Paragraph("Employment History", dateFont));
        headerCell.setPaddingBottom(10f);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setColspan(2);
        headerCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(headerCell);

        float[] columnWidths = {0.3f, 0.7f};
        table.setWidths(columnWidths);

        resume.getEmploymentHistory().forEach((employmentHistory) -> {
            var startDate = employmentHistory.getStartDate().getMonth().toString().substring(0,3) + " " + employmentHistory.getStartDate().getYear();
            var endDate = employmentHistory.getEndDate().getMonth().toString().substring(0,3) + " " + employmentHistory.getEndDate().getYear();
            PdfPCell leftCell = new PdfPCell(new Paragraph(startDate + " - " + endDate, empDateFont));
            leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            leftCell.setBorder(PdfPCell.NO_BORDER);

            var employmentDescription =  employmentHistory.getJobTitle() + " - " + employmentHistory.getEmployer() + "\n" +
                    employmentHistory.getDescription();
            PdfPCell rightCell = new PdfPCell(new Paragraph(String.join(", ", employmentDescription), empDescriptionFont));
            rightCell.setHorizontalAlignment(ALIGN_LEFT);
            rightCell.setBorder(PdfPCell.NO_BORDER);

            table.addCell(leftCell);
            table.addCell(rightCell);
            table.setSpacingBefore(10);
        });

        table.setSpacingBefore(15);
        return table;
    }

    static PdfPTable addCell(Resume resume, String header, List<String> data) throws DocumentException, IOException {
        Font boldTwelve = generateTimesNewRomanFont(12, Font.BOLD, BaseColor.BLACK);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        float[] columnWidths = {0.3f, 0.7f};
        table.setWidths(columnWidths);

        PdfPCell leftCell = new PdfPCell(new Paragraph(header, boldTwelve));
        leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftCell.setBorder(PdfPCell.NO_BORDER);

        String linksText = String.join(", ", data);
        PdfPCell rightCell = new PdfPCell(new Paragraph(linksText, boldTwelve));
        rightCell.setLeading(0.5f,1.5f);
        rightCell.setHorizontalAlignment(ALIGN_LEFT);
        rightCell.setBorder(PdfPCell.NO_BORDER);

        table.addCell(leftCell);
        table.addCell(rightCell);

        table.setSpacingBefore(15);
        return table;
    }

    static Paragraph createParagraph(String content, Font font, int alignment, int newLine) {
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setIndentationLeft(15);
        paragraph.setIndentationRight(15);
        paragraph.setAlignment(alignment);
        for (int i = 0; i < newLine; i++)
            paragraph.add(Chunk.NEWLINE);
        return paragraph;
    }

    static Font generateTimesNewRomanFont(float size, Integer font, BaseColor baseColor) throws
            DocumentException, IOException {
        BaseFont myBaseFont = BaseFont.createFont("fonts/times.ttf", BaseFont.CP1252, BaseFont.EMBEDDED);
        return new Font(myBaseFont, size, font, baseColor);
    }
}
