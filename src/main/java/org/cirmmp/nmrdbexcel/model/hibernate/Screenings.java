package org.cirmmp.nmrdbexcel.model.hibernate;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "screenings")
public class Screenings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //@Column(name = "name", unique=true)
    @Column(name = "name",unique = true)
    private String name;

    @Column(name = "temperature")
    private String temperature;

    @OneToMany(targetEntity = Results.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "screening_result_fk", referencedColumnName = "id")
    private List<Results> results;

    @OneToMany(targetEntity = Cocktails.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "screening_cocktail_fk", referencedColumnName = "id")
    private List<Cocktails> cocktails;

    @ManyToOne
    @JoinColumn(name = "proteins_id")
    private Proteins proteins;
}
