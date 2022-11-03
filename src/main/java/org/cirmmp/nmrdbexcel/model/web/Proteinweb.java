package org.cirmmp.nmrdbexcel.model.web;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Proteinweb {
    private String seq;
    private String name;
    private List<String> expname;
}
