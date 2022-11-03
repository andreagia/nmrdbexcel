package org.cirmmp.nmrdbexcel.model.web;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResQueryByTarget {
    private String proteinname;
    private String screeningname;
    private String spectrum;
    private String smileformula;
    private String mapping;
    private String affinity;

}
