package com.dizertatie.backend.user.service;


import com.dizertatie.backend.user.model.GameCover;
import com.dizertatie.backend.user.repository.ImageUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {

    @Autowired
    private ImageUploadRepository imageUploadRepository;

    @Override
    public List<GameCover> getImages() {
        return imageUploadRepository.findAll();
    }

    @Override
    public void removeImageById(Long id) {
        imageUploadRepository.deleteById(id);
    }

    @Override
    public void removeImage(GameCover image) {
        imageUploadRepository.delete(image);
    }

    @Override
    public GameCover uploadImage(MultipartFile file) throws IOException {
        GameCover gameCover = new GameCover(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        imageUploadRepository.save(gameCover);
        return gameCover;
    }
}
