package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.entity.MenuImage;
import com.enigma.purba_resto.repository.MenuImageRepository;
import com.enigma.purba_resto.service.MenuImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class MenuImageServiceImpl implements MenuImageService {
    private final MenuImageRepository menuImageRepository;
    private final Path directoryPath = Paths.get("/home/user/Downloads/purba_resto/img");

    @Override
    public MenuImage createFile(MultipartFile file) {
        if(file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"file is required");
        try{
            //1. LOGIC UNTUK MENYIMPAN KE DIRECTORY
            // lokasi penyimpanayn file dalam folder
            // Path directoryPath = Paths.get("/home/user/Downloads/purba_resto/img"); // ini global truh luar saja, kenapa?,
            // karena method yang lain juga butuh, biar bisa diakses semuanya, jadi taruh diluar
            //les.createDirectory(directoryPath);
            // Periksa apakah direktori sudah ada
            /*if (!Files.exists(directoryPath)) {
                Files.createDirectory(directoryPath);
            }*/
            Files.createDirectories(directoryPath);
            String fileName = String.format("%d_%s",System.currentTimeMillis(),file.getOriginalFilename());
            // path dan fileNAme harus disatukan ajdi satu string
            // contoh : /home/user/Downloads/purba_resto/img + 02052024_namaFile.jpg
            // cara menambahkan adalah sebagai berikut
            Path filePath = directoryPath.resolve(fileName); // hasilnya ==> /home/user/Downloads/purba_resto/img/02052024_namaFile.jpg
            // lalu simpan gambar/file fisiknya di storage
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            //2. LOGIC UNTUK MENYIMPAN METADATA KE DB
            MenuImage metaDataMenuImage = MenuImage.builder()
                    .name(fileName)
                    .size(file.getSize())
                    .contentType(file.getContentType())
                    .path(filePath.toString())
                    .build();
            return  menuImageRepository.saveAndFlush(metaDataMenuImage);
        } catch (IOException e) {
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            //row new RuntimeException(e); // ini untuk melihat error yang lengkap
        }
    }

    //Download
    @Override
    public Resource findFileByPath(String path) {
        try {
            Path filePath = Paths.get(path);
            return new UrlResource(filePath.toUri());
        }catch (MalformedURLException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void deleteFile(String path) {

    }
}
