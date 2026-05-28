package com.example.studentmanagement.util;

import com.example.studentmanagement.entity.Student;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class StudentPdfExporter {

    public static byte[] export(List<Student> students) {

        Document document = new Document();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);

            document.open();

            // Title Font
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            titleFont.setSize(18);

            // Title
            Paragraph title = new Paragraph("Student Management System", titleFont);

            title.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(title);

            document.add(Chunk.NEWLINE);

            // Table
            PdfPTable table = new PdfPTable(5);

            table.setWidthPercentage(100);

            table.setSpacingBefore(10);

            // Optional Column Widths
            float[] columnWidths = {1.5f, 3f, 5f, 3f, 1.5f};
            table.setWidths(columnWidths);

            // Header Font
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            // ID Header
            PdfPCell idHeader = new PdfPCell(new Phrase("ID", headerFont));
            idHeader.setBackgroundColor(Color.LIGHT_GRAY);
            idHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            idHeader.setPadding(5);

            // Name Header
            PdfPCell nameHeader = new PdfPCell(new Phrase("Name", headerFont));
            nameHeader.setBackgroundColor(Color.LIGHT_GRAY);
            nameHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            nameHeader.setPadding(5);

            // Email Header
            PdfPCell emailHeader = new PdfPCell(new Phrase("Email", headerFont));
            emailHeader.setBackgroundColor(Color.LIGHT_GRAY);
            emailHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            emailHeader.setPadding(5);

            // Course Header
            PdfPCell courseHeader = new PdfPCell(new Phrase("Course", headerFont));
            courseHeader.setBackgroundColor(Color.LIGHT_GRAY);
            courseHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            courseHeader.setPadding(5);

            // Age Header
            PdfPCell ageHeader = new PdfPCell(new Phrase("Age", headerFont));
            ageHeader.setBackgroundColor(Color.LIGHT_GRAY);
            ageHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            ageHeader.setPadding(5);

            // Add Headers
            table.addCell(idHeader);
            table.addCell(nameHeader);
            table.addCell(emailHeader);
            table.addCell(courseHeader);
            table.addCell(ageHeader);

            // Table Data
            for (Student student : students) {

                // ID Cell
                PdfPCell idCell = new PdfPCell(
                        new Phrase(String.valueOf(student.getId()))
                );
                idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                idCell.setPadding(5);

                // Name Cell
                PdfPCell nameCell = new PdfPCell(
                        new Phrase(student.getName())
                );
                nameCell.setPadding(5);

                // Email Cell
                PdfPCell emailCell = new PdfPCell(
                        new Phrase(student.getEmail())
                );
                emailCell.setPadding(5);

                // Course Cell
                PdfPCell courseCell = new PdfPCell(
                        new Phrase(student.getCourse())
                );
                courseCell.setPadding(5);

                // Age Cell
                PdfPCell ageCell = new PdfPCell(
                        new Phrase(String.valueOf(student.getAge()))
                );
                ageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                ageCell.setPadding(5);

                // Add Data Cells
                table.addCell(idCell);
                table.addCell(nameCell);
                table.addCell(emailCell);
                table.addCell(courseCell);
                table.addCell(ageCell);
            }

            document.add(table);

            document.close();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException("Error while generating PDF", e);
        }

        return out.toByteArray();
    }
}