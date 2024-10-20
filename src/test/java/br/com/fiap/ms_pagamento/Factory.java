package br.com.fiap.ms_pagamento;

import br.com.fiap.ms_pagamento.model.Pagamento;
import br.com.fiap.ms_pagamento.model.Status;

import java.math.BigDecimal;

public class Factory {

    public static Pagamento createPagamento(){
        return new Pagamento(1L, BigDecimal.valueOf(32.25), "Bach", "294737375583726632",
                "07/08", "585", Status.CRIADO, 1L, 2L);

    }
}
