package stoyanov.stanimir.taulia.assignment.service;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import stoyanov.stanimir.taulia.assignment.model.*;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvParseServiceImpl implements CsvParseService {

    private static final String CSV = ".csv";
    private static final String XML = ".xml";

    @Override
    public ArrayList<File> processCsv(MultipartFile file, ReturnType returnType) throws JAXBException {
        var buyers = parseCsvByBuyer(file);

        return switch (returnType) {
            case CSV -> saveToCsvFiles(buyers);
            case XML -> saveToXmlFiles(buyers);
        };
    }

    private Invoices parseCsvByBuyer(MultipartFile file) {
        try (
                Reader fileReader = new InputStreamReader(
                        new BOMInputStream(file.getInputStream(), false),
                        StandardCharsets.UTF_8)
        ) {
            var reader = new CSVReader(fileReader);
            var invoices = new CsvToBeanBuilder<Invoice>(reader)
                    .withType(Invoice.class)
                    .withSeparator(',')
                    .withOrderedResults(true)
                    .build()
                    .stream()
                    .collect(Collectors.groupingBy(Invoice::getBuyer));
            return new Invoices(invoices);
        } catch (IOException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private ArrayList<File> saveToCsvFiles(Invoices buyers) {
        var files = new ArrayList<File>();
        buyers.getBuyers()
                .forEach((buyer, invoices) -> {
                    var file = new File(buyer + CSV);
                    try (Writer writer = new FileWriter(file)) {
                        var beanToCsv = new StatefulBeanToCsvBuilder<>(writer)
                                .withOrderedResults(true)
                                .build();
                        beanToCsv.write(new ArrayList<>(invoices));
                        files.add(file);
                    } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
                        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                    }
                });
        return files;
    }

    private ArrayList<File> saveToXmlFiles(Invoices invoices) throws JAXBException {
        var files = new ArrayList<File>();
        var context = JAXBContext.newInstance(
                Invoices.class,
                Invoice.class,
                BuyersList.class,
                BuyerEntry.class
        );
        var marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        var file = new File("invoices" + XML);
        marshaller.marshal(invoices, file);
        files.add(file);
        files.addAll(exportFilesFromCsv(invoices));
        return files;
    }


    private List<File> exportFilesFromCsv(Invoices invoices) {
        return invoices.getBuyers()
                .values()
                .stream()
                .flatMap(List::stream)
                .filter(invoice -> Strings.isNotEmpty(invoice.getInvoiceImage()))
                .map(invoice -> {
                    File file = null;
                    try {
                        file = new File(invoice.getImageName());
                        var image = ImageIO.createImageOutputStream(new FileOutputStream(file));
                        image.write(
                                invoice.getInvoiceImage()
                                        .getBytes()
                        );
                        image.close();
                    } catch (IOException e) {
                        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                    }
                    return file;
                }).toList();
    }
}
