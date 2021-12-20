package com.sftp.api;

import com.sftp.service.FileTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileTransferService fileTransferService;

     @PostMapping(value = "/upload")
     ResponseEntity<Object> uploadFile(@RequestBody MultipartFile file) {
         if (fileTransferService.uploadFile(file)) {
             return ResponseEntity.status(HttpStatus.OK).body("Successfully Uploaded");
         } else {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while uploading file");
         }
     }

     @GetMapping("/download/{fileName:.+}")
     ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws Exception {
         InputStreamResource resource = null;
         File file = fileTransferService.downloadFile(fileName);
         resource = new InputStreamResource(new FileInputStream(file));
             return ResponseEntity.ok()
                     .contentLength(file.length())
                     .contentType(MediaType.MULTIPART_FORM_DATA)
                     .header("Content-Disposition", file.getName())
                     .body(resource);
     }


}
