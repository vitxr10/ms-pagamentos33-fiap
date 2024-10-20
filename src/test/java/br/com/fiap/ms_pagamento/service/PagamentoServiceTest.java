package br.com.fiap.ms_pagamento.service;

import br.com.fiap.ms_pagamento.Factory;
import br.com.fiap.ms_pagamento.dto.PagamentoDTO;
import br.com.fiap.ms_pagamento.model.Pagamento;
import br.com.fiap.ms_pagamento.model.Status;
import br.com.fiap.ms_pagamento.repository.PagamentoRepository;
import br.com.fiap.ms_pagamento.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PagamentoServiceTest {

    @InjectMocks
    private PagamentoService service;

    @Mock
    private PagamentoRepository repository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAllShouldReturnPageOfPagamentoDTO() {
        Page<Pagamento> page = new PageImpl<>(List.of(Factory.createPagamento()));
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(page);

        var pagamentos = service.findAll(pageable);

        Assertions.assertNotNull(pagamentos);
        Assertions.assertEquals(1, pagamentos.getTotalElements());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void findByIdShouldReturnPagamentoDTO() {
        Long existingId = 1L;
        Pagamento pagamento = Factory.createPagamento();

        when(repository.findById(existingId)).thenReturn(Optional.of(pagamento));

        var result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        verify(repository, times(1)).findById(existingId);
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDontExists(){
        Long nonExistingId = 100L;

        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
        verify(repository, times(1)).findById(nonExistingId);
    }

    @Test
    void insertShouldReturnPagamentoDTO() {
        PagamentoDTO dto = new PagamentoDTO(1L, new BigDecimal(100), "pix", "378292222223", "08/09", "233", Status.CRIADO, 1L, 1L);
        Pagamento pagamento = new Pagamento();
        copyDtoToEntity(dto, pagamento);

        when(repository.save(any(Pagamento.class))).thenReturn(pagamento);

        var result = service.insert(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        verify(repository, times(1)).save(any(Pagamento.class));
    }

    @Test
    void updateShouldReturnPagamentoDTOWhenIdExists() {
        Long existingId = 1L;
        PagamentoDTO dto = new PagamentoDTO(1L, new BigDecimal(100), "pix", "378292222223", "08/09", "233", Status.CRIADO, 1L, 1L);
        Pagamento pagamento = Factory.createPagamento();

        when(repository.getReferenceById(existingId)).thenReturn(pagamento);
        copyDtoToEntity(dto, pagamento);
        when(repository.save(any(Pagamento.class))).thenReturn(pagamento);

        var result = service.update(existingId, dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertEquals(dto.getValor(), result.getValor());
        verify(repository, times(1)).getReferenceById(existingId);
        verify(repository, times(1)).save(any(Pagamento.class));
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDontExists() {
        Long nonExistingId = 100L;
        PagamentoDTO dto = new PagamentoDTO(100L, new BigDecimal(100), "pix", "378292222223", "08/09", "233", Status.CRIADO, 1L, 1L);

        when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, dto);
        });
        verify(repository, times(1)).getReferenceById(any(Long.class));
    }

    @Test
    void deleteShouldDoNothingWhenIdExists() {
        Long existingId = 1L;
        Pagamento pagamento = Factory.createPagamento();

        when(repository.existsById(existingId)).thenReturn(true);

        service.delete(existingId);

        verify(repository, times(1)).existsById(existingId);
        verify(repository, times(1)).deleteById(existingId);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDontExists() {
        Long nonExistingId = 100L;

        when(repository.existsById(nonExistingId)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
        verify(repository, times(1)).existsById(nonExistingId);
    }

    private void copyDtoToEntity(PagamentoDTO dto, Pagamento entity) {
        entity.setId(dto.getId());
        entity.setValor(dto.getValor());
        entity.setNome(dto.getNome());
        entity.setNumeroDoCartao(dto.getNumeroDoCartao());
        entity.setValidade(dto.getValidade());
        entity.setCodigoDeSeguranca(dto.getCodigoDeSeguranca());
        entity.setStatus(dto.getStatus());
        entity.setPedidoId(dto.getPedidoId());
        entity.setFormaDePagamentoId(dto.getFormaDePagamentoId());
    }

}