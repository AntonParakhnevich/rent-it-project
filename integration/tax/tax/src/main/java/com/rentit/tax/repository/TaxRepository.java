package com.rentit.tax.repository;

import com.rentit.tax.model.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxRepository extends JpaRepository<Tax, Long> {


}
