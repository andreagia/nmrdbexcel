package org.cirmmp.nmrdbexcel.repository;


import org.cirmmp.nmrdbexcel.model.hibernate.Cocktails;
import org.cirmmp.nmrdbexcel.model.hibernate.Screenings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreeningsRepository extends JpaRepository<Screenings, Long> {

    List<Screenings> findByName(String name);
}
