package org.cirmmp.nmrdbexcel.model.hibernate;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@ToString
@Entity
@Table(name = "fragments")
public class Fragments {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "zid")
    private String zid;

    @Column(name="spectrum")
    private String spectrum;

    @Column(name="smileforumala")
    private String smileformula;

    @ManyToMany(mappedBy = "fragmentsSetResult")
    List<Results> results;

    @ManyToMany(mappedBy = "fragmentsSetCocktail")
    List<Cocktails> cocktails;

}