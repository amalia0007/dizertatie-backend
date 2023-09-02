package com.dizertatie.backend.user.service;

import com.dizertatie.backend.user.model.GameCover;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ImageUploadService {

    List<GameCover> getImages();

    void removeImageById(Long id);

    void removeImage(GameCover image);

    GameCover uploadImage(MultipartFile file) throws IOException;
}
