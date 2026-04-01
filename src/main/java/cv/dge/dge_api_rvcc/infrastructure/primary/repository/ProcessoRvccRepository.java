package cv.dge.dge_api_rvcc.infrastructure.primary.repository;

import cv.dge.dge_api_rvcc.infrastructure.primary.entity.ProcessoRvcc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessoRvccRepository extends JpaRepository<ProcessoRvcc, Integer> {

    boolean existsByNumProcesso(String numProcesso);
}

