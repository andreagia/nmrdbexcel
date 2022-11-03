package org.cirmmp.nmrdbexcel.repository;

import org.cirmmp.nmrdbexcel.model.hibernate.Cocktails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CocktailsRepository extends JpaRepository<Cocktails, Long>{

    List<Cocktails> findCocktailsById(Long id);
}
