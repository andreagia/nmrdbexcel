package org.cirmmp.nmrdbexcel.repository;

import org.cirmmp.nmrdbexcel.model.hibernate.Proteins;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProteinRepository  extends JpaRepository<Proteins, Long> {
    List<Proteins> findProteinsByName(String name);
}
