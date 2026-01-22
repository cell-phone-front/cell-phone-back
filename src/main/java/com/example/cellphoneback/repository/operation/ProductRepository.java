package com.example.cellphoneback.repository.operation;

import com.example.cellphoneback.entity.operation.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
}
