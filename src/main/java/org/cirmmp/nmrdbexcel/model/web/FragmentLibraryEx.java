package org.cirmmp.nmrdbexcel.model.web;


import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "fragmentex")
public class FragmentLibraryEx {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "zid")
    private String zid;

    @Column(name="spectrum")
    private String spectrum;

    @Column(name="smileforumala")
    private String smileformula;

}
