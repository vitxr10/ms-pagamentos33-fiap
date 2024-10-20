package br.com.fiap.ms_pagamento.repository;

import br.com.fiap.ms_pagamento.Factory;
import br.com.fiap.ms_pagamento.model.Pagamento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class PagamentoRepositoryTests {

    @Autowired
    private PagamentoRepository repository;

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        // Arrange
        Long existingId = 1L;

        // Act
        repository.deleteById(existingId);

        // Assert
        Optional<Pagamento> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnNonEmptyOptionalWhenIdExists(){
        // Arrange
        Long existingId = 1L;

        // Act
        Optional<Pagamento> pagamento = repository.findById(existingId);

        // Assert
        Assertions.assertFalse(pagamento.isEmpty());
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdDontExists(){
        // Arrange
        Long nonExistingId = 100L;

        // Act
        Optional<Pagamento> pagamento = repository.findById(nonExistingId);

        // Assert
        Assertions.assertTrue(pagamento.isEmpty());
    }

    @Test
    public void saveShouldPersistPagamento(){
        // Arrange
        Pagamento pagamento = Factory.createPagamento();
        pagamento.setId(null);

        // Act
        pagamento = repository.save(pagamento);

        // Assert
        Assertions.assertNotNull(pagamento.getId());
    }
}
