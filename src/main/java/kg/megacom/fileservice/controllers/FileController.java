package kg.megacom.fileservice.controllers;

import kg.megacom.fileservice.responses.Response;
import kg.megacom.fileservice.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping(value = "api/v1/file")
public class FileController {

    @Autowired
    private FileService fileService;



    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public Response upload(@RequestPart MultipartFile file){
        String fileName = fileService.storeFile(file);

        String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/file/download")
                .path("//")
                .path(fileName)
                .toUriString();

        return new Response(
                fileName,
                downloadUri,
                file.getContentType(),
                file.getSize()
        );
    }

    @GetMapping("/download/{folder}/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String folder, @PathVariable String fileName, HttpServletRequest request){
        Resource resource = fileService.getFileByName(folder.concat("/").concat(fileName));

        String contentType =  null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (contentType == null){
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }


}
