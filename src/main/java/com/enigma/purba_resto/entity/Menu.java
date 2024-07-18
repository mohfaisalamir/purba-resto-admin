package com.enigma.purba_resto.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Service;

@Entity
@Table(name = "m_menu")
public class Menu {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "price", columnDefinition = "bigint check (price >= 0)")
    private Long price;

    public Menu(String id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public Menu() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
