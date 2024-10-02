package com.enigma.purba_resto.service;

import com.enigma.purba_resto.entity.MenuImage;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface MenuImageService {
    MenuImage createFile(MultipartFile file);
    Resource findFileByPath(String path);
    void deleteFile(String path);
}
