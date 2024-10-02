package com.enigma.purba_resto.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "m_menu_image")
@Builder
public class MenuImage {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;
    private String name; // nama file
    private String path; // menyimpan URL atau path ke lokasi gambar, yang bisa berupa lokasi di server atau URL di cloud storage
    private String contentType; // ni menyimpan jenis konten (content type) dari gambar, misalnya image/jpeg atau image/png. Ini penting untuk mengetahui format gambar yang sedang digunakan.
    private Long size; // ini menyimpan ukuran file gambar dalam bytes. Ini bisa berguna untuk mengetahui apakah file terlalu besar atau untuk keperluan lain yang berkaitan dengan penyimpanan atau pengiriman data.
}
