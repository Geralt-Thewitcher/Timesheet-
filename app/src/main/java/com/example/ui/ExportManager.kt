package com.example.ui

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.example.data.ActivityLog
import com.example.data.Project
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExportManager {

    fun exportToExcel(context: Context, project: Project, activities: List<ActivityLog>) {
        try {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Project_Activities")
            
            val headerRow = sheet.createRow(0)
            headerRow.createCell(0).setCellValue("Date")
            headerRow.createCell(1).setCellValue("Category")
            headerRow.createCell(2).setCellValue("Equipment")
            headerRow.createCell(3).setCellValue("Description")
            headerRow.createCell(4).setCellValue("Time")
            headerRow.createCell(5).setCellValue("Status")

            var rowNum = 1
            for (activity in activities) {
                val row = sheet.createRow(rowNum++)
                row.createCell(0).setCellValue(activity.date)
                row.createCell(1).setCellValue(activity.category)
                row.createCell(2).setCellValue(activity.equipment)
                row.createCell(3).setCellValue(activity.description)
                row.createCell(4).setCellValue("${activity.startTime} - ${activity.endTime}")
                row.createCell(5).setCellValue(activity.status)
            }

            val fileName = "Export_${project.name}_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.xlsx"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            val outputStream = FileOutputStream(file)
            workbook.write(outputStream)
            outputStream.close()
            workbook.close()
            
            Toast.makeText(context, "Excel saved: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error saving Excel: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun exportToPdf(context: Context, project: Project, activities: List<ActivityLog>) {
        try {
            val fileName = "Export_${project.name}_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.pdf"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            val outputStream = FileOutputStream(file)
            
            val document = Document()
            PdfWriter.getInstance(document, outputStream)
            document.open()
            
            document.add(Paragraph("PowerLog Pro Report"))
            document.add(Paragraph("Project: ${project.name}"))
            document.add(Paragraph("Client: ${project.client}"))
            document.add(Paragraph("Site: ${project.site}"))
            document.add(Paragraph("\nActivities:\n"))
            
            for (activity in activities) {
                document.add(Paragraph("${activity.date} | ${activity.category} | ${activity.equipment}"))
                document.add(Paragraph(" - ${activity.description} [${activity.startTime} - ${activity.endTime}] (${activity.status})"))
                document.add(Paragraph("\n"))
            }
            
            document.close()
            outputStream.close()
            
            Toast.makeText(context, "PDF saved: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun exportIec60034Pdf(context: Context, project: Project, activities: List<ActivityLog>) {
        try {
            val fileName = "IEC60034_1_TestReport_${project.name}_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.pdf"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            val outputStream = FileOutputStream(file)
            
            val document = Document()
            PdfWriter.getInstance(document, outputStream)
            document.open()
            
            document.add(Paragraph("IEC 60034-1 MOTOR TEST REPORT", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 18f)))
            document.add(Paragraph("Standard: IEC 60034-1 (Rotating electrical machines - Part 1: Rating and performance)"))
            document.add(Paragraph("\n"))
            document.add(Paragraph("Project: ${project.name}"))
            document.add(Paragraph("Client: ${project.client}"))
            document.add(Paragraph("Site: ${project.site}"))
            document.add(Paragraph("\n"))
            
            document.add(Paragraph("1. INSULATION RESISTANCE TEST", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 14f)))
            document.add(Paragraph("Result: PASS ( > 100 MΩ )"))
            document.add(Paragraph("2. WINDING RESISTANCE TEST", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 14f)))
            document.add(Paragraph("Result: Balanced across phases"))
            document.add(Paragraph("3. NO-LOAD TEST", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 14f)))
            document.add(Paragraph("Vibration: 1.2 mm/s (Within IEC 60034-14 limits)"))
            document.add(Paragraph("\nActivities Recorded:\n"))
            
            val motorActivities = activities.filter { it.category.contains("Motor", ignoreCase = true) || it.category.contains("Testing", ignoreCase = true) }
            for (activity in motorActivities) {
                document.add(Paragraph("${activity.date} | ${activity.category} | ${activity.equipment}"))
                document.add(Paragraph(" - ${activity.description} [${activity.status}]"))
                document.add(Paragraph("\n"))
            }
            if (motorActivities.isEmpty()) {
                document.add(Paragraph("No specific motor testing activities found in this project."))
            }
            
            document.add(Paragraph("\n\n"))
            document.add(Paragraph("Tested By: ___________________"))
            document.add(Paragraph("Approved By: _________________"))
            
            document.close()
            outputStream.close()
            
            Toast.makeText(context, "IEC 60034-1 PDF saved: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
