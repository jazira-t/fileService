package kg.megacom.fileservice.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String storeFile(MultipartFile file);

    Resource storeFileWithPath(MultipartFile file, String path);

    Resource getFileByName(String concat);
}
