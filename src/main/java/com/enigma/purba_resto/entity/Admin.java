package com.enigma.purba_resto.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "m_admin")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Admin {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;
    private String name;
//    private String phone; // jika seperti ini, akan kebobolan..
//    karena ada 2 nomer maka ada 2 akun, atau jika kita paksa ada nomer,
//    maka harus ada tabel tersendiri untuk nomer telepone yang disimpan berdasarkan tabel kredensial
    @OneToOne
    @JoinColumn(name = "user_credential_id", unique = true)
    private UserCredential userCredential;
}