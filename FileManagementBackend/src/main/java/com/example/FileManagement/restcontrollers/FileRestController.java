package com.example.FileManagement.restcontrollers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileRestController {

    List<String> fileNames;

    @CrossOrigin("http://127.0.0.1:5500/")
    @PostMapping("files/add/{fileName}")
    public ResponseEntity<String> createFile(@PathVariable("fileName") String fileName, @RequestBody String content) {
        File newFile = new File("src\\main\\resources\\userFiles\\" + fileName + ".txt");
        try {
            if (newFile.createNewFile()) {
                FileWriter fileWriter = new FileWriter(newFile);
                fileWriter.write(content);
                fileWriter.close();
            } else {
                return ResponseEntity.badRequest().body("File '" + fileName + "' already exists...");
            }
        } catch (IOException ex) {
            Logger.getLogger(FileRestController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ResponseEntity.ok().body(fileName + ".txt was created successfully!");
    }

    @CrossOrigin("http://127.0.0.1:5500/")
    @PostMapping("files/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable("fileName") String fileName) {
        File file = new File("src\\main\\resources\\userFiles\\" + fileName);
        file.delete();
        return ResponseEntity.ok().body(fileName + ".txt was deleted successfully!");
    }

    @CrossOrigin("http://127.0.0.1:5500/")
    @GetMapping("files/get")
    public List<String> getAllFiles() {
        File folder = new File("src\\main\\resources\\userFiles");
        fileNames = new ArrayList<>();
        for (File file : folder.listFiles()) {
            fileNames.add(file.getName());
        }
        return fileNames;
    }

    @CrossOrigin("http://127.0.0.1:5500/")
    @GetMapping("files/get/content/{fileName}")
    public String getFileContent(@PathVariable("fileName") String fileName) {
        String filePath = "src\\main\\resources\\userFiles\\" + fileName;
        try ( BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                return line;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileRestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileRestController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    @CrossOrigin("http://127.0.0.1:5500/")
    @PutMapping("files/edit/{fileName}")
    public ResponseEntity<String> editFile(@PathVariable("fileName") String fileName, @RequestBody String newContent) {
        //File file = new File("src\\main\\resources\\userFiles\\" + fileName);
        File folder = new File("src\\main\\resources\\userFiles");
        FileWriter fileWriter;
        for (File file : folder.listFiles()) {
            if (fileName.equals(file.getName())) {
                try {
                    System.out.println(newContent);
                    fileWriter = new FileWriter(file);
                    fileWriter.write(newContent);
                    fileWriter.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileRestController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        return ResponseEntity.ok().body(fileName + ".txt was edited successfully!");
    }

    @CrossOrigin("http://127.0.0.1:5500/")
    @GetMapping("files/download/{fileName}")
    public ResponseEntity<FileSystemResource> downloadFile(@PathVariable("fileName") String fileName) throws IOException {

        File file = new File("src\\main\\resources\\userFiles\\" + fileName);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        FileSystemResource resource = new FileSystemResource(file);
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .contentLength(resource.contentLength())
                .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
