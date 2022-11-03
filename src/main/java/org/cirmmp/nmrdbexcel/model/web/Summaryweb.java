package org.cirmmp.nmrdbexcel.model.web;

import lombok.*;

import javax.persistence.Column;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Summaryweb {

    private String zid;

    private String spectrum;

    private String bindingstate;

    private String waterlogsy;

    private String t2;

    private String csp;

    private String std;

    private String mapping;

    private String residues;

    private String affinity;

    private String kd;
}
