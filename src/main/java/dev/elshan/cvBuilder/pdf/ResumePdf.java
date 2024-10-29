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
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.itextpdf.text.Element.ALIGN_CENTER;
import static com.itextpdf.text.Element.ALIGN_LEFT;

@Service
public class ResumePdf {

    @SneakyThrows
    public static byte[] getPdf(Resume resume){

        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Font boldEleven = generateTimesNewRomanFont(11, Font.BOLD, BaseColor.BLACK);
        Font boldTwelve = generateTimesNewRomanFont(12, Font.BOLD, BaseColor.BLACK);

        Font normalEleven = generateTimesNewRomanFont(11, Font.NORMAL, BaseColor.BLACK);
        Font greenEleven = generateTimesNewRomanFont(11, Font.NORMAL, BaseColor.GREEN);

        PdfWriter.getInstance(document, outputStream);

        document.open();
        var headLineTxt = resume.getFirstName() + " " + resume.getLastName() + ", " + resume.getJobTitle();
        Paragraph paragraph = createParagraph(headLineTxt, boldTwelve, ALIGN_CENTER, 2);
        document.add(paragraph);

        var linksTxt = addCell(resume,"LINKS",resume.getWebsiteLinks().stream().map(WebsiteLinks::getName).toList());
        document.add(linksTxt);

        var profileTxt = addCell(resume,"PROFILE",List.of(resume.getSummary()));
        document.add(profileTxt);

        var employmentHistoryTxt = addCellForEmploymentHistory(resume);
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
        Font boldTwelve = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
        Font regularTwelve = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);

        // Create the main table with 2 columns and set width to 100%
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        // Set column widths to 20% for "Skills" label and 80% for the nested table
        float[] columnWidths = {0.3f, 0.7f};
        table.setWidths(columnWidths);

        // Left cell with "Skills" label
        PdfPCell leftCell = new PdfPCell(new Paragraph(header, boldTwelve));
        leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftCell.setBorder(PdfPCell.NO_BORDER);

        // Create a nested table with 2 columns for the skills list
        PdfPTable nestedTable = new PdfPTable(2);
        nestedTable.setWidthPercentage(100);

        // Add skills to the nested table in two columns
        for (int i = 0; i < skills.size(); i++) {
            PdfPCell skillCell = new PdfPCell(new Paragraph(skills.get(i), regularTwelve));
            skillCell.setBorder(PdfPCell.NO_BORDER);
            skillCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            nestedTable.addCell(skillCell);

            // If it's the last item and there's no pair, add an empty cell
            if (i == skills.size() - 1 && skills.size() % 2 != 0) {
                PdfPCell emptyCell = new PdfPCell(new Paragraph(""));
                emptyCell.setBorder(PdfPCell.NO_BORDER);
                nestedTable.addCell(emptyCell);
            }
        }

        // Right cell containing the nested table
        PdfPCell rightCell = new PdfPCell(nestedTable);
        rightCell.setBorder(PdfPCell.NO_BORDER);

        // Add cells to the main table
        table.addCell(leftCell);
        table.addCell(rightCell);

        return table;
    }

    static PdfPTable addCellForEducationHistory(Resume resume) throws DocumentException, IOException {
        Font dateFont = generateTimesNewRomanFont(10, Font.NORMAL, BaseColor.BLACK);
        Font empFont = generateTimesNewRomanFont(13, Font.NORMAL, BaseColor.BLACK);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        PdfPCell headerCell = new PdfPCell(new Paragraph("Education History", dateFont));
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setColspan(2);
        headerCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(headerCell);

        float[] columnWidths = {0.3f, 0.7f};
        table.setWidths(columnWidths);

        resume.getEducationHistory().forEach((educationHistory) -> {
            var startDate = educationHistory.getStartDate().getMonth().toString().substring(0,3) + " " + educationHistory.getStartDate().getYear();
            var endDate = educationHistory.getEndDate().getMonth().toString().substring(0,3) + " " + educationHistory.getEndDate().getYear();
            PdfPCell leftCell = new PdfPCell(new Paragraph(startDate + " - " + endDate, dateFont));
            leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            leftCell.setBorder(PdfPCell.NO_BORDER);

            var educationDescription =  educationHistory.getSpeciality() + ", " + educationHistory.getSchool();
            PdfPCell rightCell = new PdfPCell(new Paragraph(String.join(", ", educationDescription), empFont));
            rightCell.setHorizontalAlignment(ALIGN_LEFT);
            rightCell.setBorder(PdfPCell.NO_BORDER);

            table.addCell(leftCell);
            table.addCell(rightCell);
        });

        return table;
    }

    static PdfPTable addCellForEmploymentHistory(Resume resume) throws DocumentException, IOException {
        Font dateFont = generateTimesNewRomanFont(10, Font.NORMAL, BaseColor.BLACK);
        Font empFont = generateTimesNewRomanFont(13, Font.NORMAL, BaseColor.BLACK);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        PdfPCell headerCell = new PdfPCell(new Paragraph("Employment History", dateFont));
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setColspan(2);
        headerCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(headerCell);

        float[] columnWidths = {0.3f, 0.7f};
        table.setWidths(columnWidths);

        resume.getEmploymentHistory().forEach((employmentHistory) -> {
            var startDate = employmentHistory.getStartDate().getMonth().toString().substring(0,3) + " " + employmentHistory.getStartDate().getYear();
            var endDate = employmentHistory.getEndDate().getMonth().toString().substring(0,3) + " " + employmentHistory.getEndDate().getYear();
            PdfPCell leftCell = new PdfPCell(new Paragraph(startDate + " - " + endDate, dateFont));
            leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            leftCell.setBorder(PdfPCell.NO_BORDER);

            var employmentDescription =  employmentHistory.getJobTitle() + ", " + employmentHistory.getEmployer() + "\n" +
                    employmentHistory.getDescription();
            PdfPCell rightCell = new PdfPCell(new Paragraph(String.join(", ", employmentDescription), empFont));
            rightCell.setHorizontalAlignment(ALIGN_LEFT);
            rightCell.setBorder(PdfPCell.NO_BORDER);

            table.addCell(leftCell);
            table.addCell(rightCell);
        });

        return table;
    }

    static PdfPTable addCell(Resume resume, String header, List<String> data) throws DocumentException, IOException {
        Font boldTwelve = generateTimesNewRomanFont(12, Font.BOLD, BaseColor.BLACK);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

// Set column widths to 30% and 70%
        float[] columnWidths = {0.3f, 0.7f};
        table.setWidths(columnWidths);

// Left Cell
        PdfPCell leftCell = new PdfPCell(new Paragraph(header, boldTwelve));
        leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftCell.setBorder(PdfPCell.NO_BORDER);

// Right Cell
        String linksText = String.join(", ", data);
        PdfPCell rightCell = new PdfPCell(new Paragraph(linksText, boldTwelve));
        rightCell.setHorizontalAlignment(ALIGN_LEFT);
        rightCell.setBorder(PdfPCell.NO_BORDER);

// Add cells to table
        table.addCell(leftCell);
        table.addCell(rightCell);

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
        BaseFont myBaseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.EMBEDDED);
        return new Font(myBaseFont, size, font, baseColor);
    }
}
