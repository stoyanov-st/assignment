package stoyanov.stanimir.taulia.assignment.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipServiceImpl implements ZipService {

    @Override
    public void zipIt(List<File> fileList, ZipOutputStream zipOut) throws IOException {
        fileList.forEach(file -> {
            try (var fis = new FileInputStream(file)) {
                var entry = new ZipEntry(file.getName());
                zipOut.putNextEntry(entry);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
            } catch (IOException e) {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        });
        zipOut.close();
    }
}
