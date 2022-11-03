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
@Table(name = "cocktails")
public class Cocktails {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "mix")
    private String mix;

    @ManyToMany
    @JoinTable(
            name = "cocktail_fragment",
            joinColumns = @JoinColumn(name = "fragment_id"),
            inverseJoinColumns = @JoinColumn(name = "cocktail_id"))
    List<Fragments> fragmentsSetCocktail;

    @Lob
    @Column(name = "d1")
    byte[] d1;
    @Lob
    @Column(name = "d2")
    byte[] d2;
    @Lob
    @Column(name = "d5")
    byte[] d5;
    @Lob
    @Column(name = "d20")
    byte[] d20;
    @Lob
    @Column(name = "d21")
    byte[] d21;
    @Lob
    @Column(name = "d22")
    byte[] d22;
    @Lob
    @Column(name = "d30")
    byte[] d30;
    @Lob
    @Column(name = "d31")
    byte[] d31;
    @Lob
    @Column(name = "d32")
    byte[] d32;

}
