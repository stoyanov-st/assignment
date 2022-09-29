package stoyanov.stanimir.taulia.assignment.service;

import org.springframework.web.multipart.MultipartFile;
import stoyanov.stanimir.taulia.assignment.model.ReturnType;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;

public interface CsvParseService {
    ArrayList<File> processCsv(MultipartFile file, ReturnType returnType) throws JAXBException;
}
