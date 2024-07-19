package org.example.book.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.book.entity.Book;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@Service
public class ExcelImportService {

    public List<Book> importBooksFromExcel(InputStream inputStream) throws IOException {
        List<Book> books = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        // Skip header row
        int rowIndex = 1;

        for (Row row : sheet) {
            if (rowIndex == 1) {
                rowIndex++;
                continue; // Skip header row
            }

            Book book = new Book();
            book.setTitle(row.getCell(0).getStringCellValue());
            book.setAuthor(row.getCell(1).getStringCellValue());
            book.setPrice((int) row.getCell(2).getNumericCellValue());

            books.add(book);
        }

        workbook.close();
        return books;
    }
}
