package cv.dge.dge_api_rvcc.infrastructure.secondary.repository;

import cv.dge.dge_api_rvcc.infrastructure.secondary.TNotificacaoConfigEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificacaoConfigEmailRepository extends JpaRepository<TNotificacaoConfigEmail, Long> {

    Optional<TNotificacaoConfigEmail> findFirstByCodigoAndAppCodeAndProcessoCodeAndEtapaCode(
            String codigo, String appCode, String processo, String etapa
    );

    Optional<TNotificacaoConfigEmail> findFirstByCodigoAndAppCode(
            String codigo, String appCode
    );
}
