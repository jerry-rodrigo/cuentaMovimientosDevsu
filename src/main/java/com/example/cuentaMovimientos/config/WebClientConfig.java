package com.example.cuentaMovimientos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuración para la creación de un {@link WebClient} en la aplicación.
 * <p>
 * Esta clase define un bean de {@link WebClient.Builder} que puede ser utilizado
 * en otros componentes de la aplicación para construir instancias de {@link WebClient}.
 * </p>
 */
@Configuration
public class WebClientConfig {

    /**
     * Crea un {@link WebClient.Builder} como un bean de Spring.
     * <p>
     * Este bean puede ser utilizado para construir instancias de {@link WebClient}
     * con configuraciones personalizadas si es necesario.
     * </p>
     *
     * @return Un {@link WebClient.Builder} configurado.
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
