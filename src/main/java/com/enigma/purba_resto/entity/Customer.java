package com.enigma.purba_resto.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "m_customer")
public class Customer {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "phone",nullable = false,unique = true)
    private String phone;
    @Column(name = "is_member")
    private boolean isMember;

    public Customer(String id, String name, String email, String phone, boolean isMember) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.isMember = isMember;
    }
    public Customer() {}

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getIsMember() {
        return isMember;
    }

    public void setIsMember(boolean member) {
        isMember = member;
    }
}
