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
@Table(name = "results")
public class Results {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "zid")
    private String zid;

    @Column(name="fragment")
    private String fragment;

    @Column(name="bindingstate")
    private String bindingstate;

    @Column(name="waterlogsy")
    private String waterlogsy;

    @Column(name="t2")
    private String t2;

    @Column(name="csp")
    private String csp;

    @Column(name="std")
    private String std;

    @Column(name = "mapping")
    private String mapping;

    @Column(name = "residues")
    private String residues;

    @Column(name = "affinity")
    private String affinity;

    @Column(name = "kd")
    private String kd;

    @ManyToMany
    @JoinTable(
            name = "result_fragment",
            joinColumns = @JoinColumn(name = "result_id"),
            inverseJoinColumns = @JoinColumn(name = "fragment_id"))
    List<Fragments> fragmentsSetResult;

    @ManyToOne
    @JoinColumn(name = "screenings_id")
    private Screenings screenings;
}