package org.cirmmp.nmrdbexcel.model.web;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Cocktailsweb {
    private Long pid;
    private String mix;
    private List<String> compaunds;
    private Map<String,String> mapcompounds;

    private String dirname;

    private String sd1;
    private byte[] d1;

    private byte[] d2;

    private byte[] d5;

    private byte[] d20;

    private byte[] d21;

    private byte[] d22;

    private byte[] d30;

    private byte[] d31;

    private byte[] d32;

}
