package org.cirmmp.nmrdbexcel.model.hibernate;

import lombok.*;

import javax.persistence.*;
//https://www.baeldung.com/java-db-storing-files
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "filenmr")
public class FilesNMR {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Lob
    byte[] content;

    String name;
}
