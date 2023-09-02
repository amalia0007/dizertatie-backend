package com.dizertatie.backend.game.controller;

import com.dizertatie.backend.game.model.GameCover;
import com.dizertatie.backend.game.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class ImageUploadController {

    @Autowired
    ImageUploadService imageUploadService;

    @PostMapping("/uploadImage")
    public ResponseEntity<GameCover> uploadImage(@RequestParam("myFile") MultipartFile file) throws IOException {
        return new ResponseEntity<>(imageUploadService.uploadImage(file), HttpStatus.OK);
    }

    @GetMapping("/getImages")
    public List<GameCover> listAllImages() {
        return imageUploadService.getImages();
    }

    @DeleteMapping("/deleteImage")
    public void deleteImage(@RequestBody GameCover image) {
        imageUploadService.removeImage(image);
    }

    @DeleteMapping("/removeImage/{id}")
    public void removeGame(@PathVariable(value = "id") Long id) {
        imageUploadService.removeImageById(id);
    }

}
