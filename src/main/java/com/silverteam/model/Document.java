package com.silverteam.model;

import lombok.Data;

import javax.persistence.*;
import java.nio.file.Path;

@Data
@Entity
public class Document {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String docName;

    @Column
    private String docNameOnDisk;

}
