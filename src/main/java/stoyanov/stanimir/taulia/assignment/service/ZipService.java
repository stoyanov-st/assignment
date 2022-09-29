package stoyanov.stanimir.taulia.assignment.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipOutputStream;

public interface ZipService {

    public void zipIt(List<File> fileList, ZipOutputStream zipOutput) throws IOException;
}
