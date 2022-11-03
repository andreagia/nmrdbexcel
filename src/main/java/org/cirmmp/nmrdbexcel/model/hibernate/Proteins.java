package org.cirmmp.nmrdbexcel.model.hibernate;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "proteins")
public class Proteins {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "seq",length=2000)
//    @Column(name = "seq",columnDefinition="LONGTEXT")
    private String seq;

    @Column(name = "name", unique=true)
    private String name;

    @OneToMany(targetEntity = Screenings.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "protein_fk", referencedColumnName = "id")
    private List<Screenings> screeningsset;


}