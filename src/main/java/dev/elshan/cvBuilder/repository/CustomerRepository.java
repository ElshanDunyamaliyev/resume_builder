package dev.elshan.cvBuilder.repository;

import dev.elshan.cvBuilder.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
