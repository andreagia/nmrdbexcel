package org.cirmmp.nmrdbexcel.repository;

import org.apache.poi.ss.formula.functions.T;
import org.cirmmp.nmrdbexcel.model.hibernate.Fragments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface FragmentsRepository extends JpaRepository<Fragments, Long> {
    List<Fragments> findBySpectrum(String spectrum);
    //https://www.baeldung.com/spring-data-derived-queries
    List<Fragments> findBySpectrumIn(Collection<String> spectrum);
    List<Fragments> findByZid(String zid);
    List<Fragments> findAll();

}
