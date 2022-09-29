package stoyanov.stanimir.taulia.assignment.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import stoyanov.stanimir.taulia.assignment.model.ReturnType;
import stoyanov.stanimir.taulia.assignment.service.CsvParseService;
import stoyanov.stanimir.taulia.assignment.service.ZipService;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;
import java.util.zip.ZipOutputStream;

@RestController
public class MainController {

    @Autowired
    CsvParseService csvParseService;
    @Autowired
    ZipService zipService;

    @PostMapping(value = "/parse-csv")
    public ResponseEntity<StreamingResponseBody> parseCsv(@RequestParam MultipartFile file,
                                      @RequestParam(value = "return-type") ReturnType returnType) {
        var returnFiles = new ArrayList<File>();
        try {
            returnFiles = csvParseService.processCsv(file, returnType);
            ArrayList<File> finalReturnFiles = returnFiles;
            StreamingResponseBody responseBody = outputStream -> {
                try(var zipOutput = new ZipOutputStream(outputStream)) {
                    zipService.zipIt(finalReturnFiles, zipOutput);
                }
            };
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment;filename=\"invoices.zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(responseBody);
        } catch (JAXBException e) {
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }
}
