package com.example.stcProject.Model.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "\"binary\"")
    @Basic(fetch = FetchType.EAGER)
    private byte[] binary;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;


}
