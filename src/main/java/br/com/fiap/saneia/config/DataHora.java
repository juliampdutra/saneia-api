package br.com.fiap.saneia.config;

import java.time.LocalDateTime;
import java.time.ZoneId;

public final class DataHora {

    private static final ZoneId FUSO_SAO_PAULO = ZoneId.of("America/Sao_Paulo");

    private DataHora() {
    }

    public static LocalDateTime agora() {
        return LocalDateTime.now(FUSO_SAO_PAULO).withNano(0);
    }
}
