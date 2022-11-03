package org.cirmmp.nmrdbexcel.model.web;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "summaryex")
public class SummaryEx {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "zid")
    private String zid;

    @Column(name="spectrum")
    private String spectrum;

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

}
