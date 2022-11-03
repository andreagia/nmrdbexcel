package org.cirmmp.nmrdbexcel.repository;

import org.cirmmp.nmrdbexcel.model.hibernate.Fragments;
import org.cirmmp.nmrdbexcel.model.hibernate.Results;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultsRepository extends JpaRepository<Results, Long> {
    List<Results> findByZid(String zid);
}
