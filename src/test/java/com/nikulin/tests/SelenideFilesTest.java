package com.nikulin.tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class SelenideFilesTest {

    @Test
    public void txtTest() throws IOException {
        String txtFilePath = "./src/test/resources/1.txt";
        String expectedData = "hello qa.guru students!";

        String actualData = FileUtils.readFileToString(new File(txtFilePath), StandardCharsets.UTF_8);
        assertThat(actualData, containsString(expectedData));
    }

    @Test
    public void pdfTest() throws IOException {
        String pdfFilePath = "./src/test/resources/1.pdf";
        String expectedData = "JUnit 5 User Guide";

        PDF pdf = new PDF(new File(pdfFilePath));
        assertThat(pdf, PDF.containsText(expectedData));
    }

    @Test
    public void xlsTest() {
        String xlsFilePath = "./src/test/resources/1.xls";
        String expectedData = "hello qa.guru students!";

        XLS xls = new XLS(new File(xlsFilePath));
        String actualData = xls.excel.getSheetAt(0).getRow(3).getCell(1).toString();
        assertThat(actualData, containsString(expectedData));
    }

    @Test
    public void zipWithoutPasswordTest() throws IOException, ZipException {
        String zipFilePath = "./src/test/resources/WithoutPassword.zip";
        String unzipFolderPath = "./src/test/resources/unzip";
        String unzipTxtFilePath = "./src/test/resources/unzip/WithoutPassword.txt";
        String expectedData = "hello qa.guru students!";

        ZipFile zipFile = new ZipFile(zipFilePath);
        zipFile.extractAll(unzipFolderPath);
        String actualData = FileUtils.readFileToString(new File(unzipTxtFilePath), StandardCharsets.UTF_8);
        assertThat(actualData, containsString(expectedData));
    }

    @Test
    public void zipWithPasswordTest() throws IOException, ZipException {
        String zipFilePath = "./src/test/resources/WithPassword.zip";
        String unzipFolderPath = "./src/test/resources/unzip";
        String unzipTxtFilePath = "./src/test/resources/unzip/WithPassword.txt";
        String expectedData = "hello qa.guru students!";
        String password = "12345678";

        ZipFile zipFile = new ZipFile(zipFilePath);
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(password);
        }
        zipFile.extractAll(unzipFolderPath);
        String actualData = FileUtils.readFileToString(new File(unzipTxtFilePath), StandardCharsets.UTF_8);
        assertThat(actualData, containsString(expectedData));
    }

        @Test
    public void docxTest() throws IOException, InvalidFormatException {
        String docFilePath = "1.docx";
        String expectedData = "hello qa.guru students!";

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(docFilePath);
        XWPFDocument docx = new XWPFDocument(OPCPackage.open(stream));
        XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
        String actualData = extractor.getText().trim();
        assertThat(actualData, containsString(expectedData));
    }
}
