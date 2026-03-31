package cv.dge.dge_api_rvcc.repositories;

import cv.dge.dge_api_rvcc.models.ProcessoRvcc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessoRvccRepository extends JpaRepository<ProcessoRvcc, Integer> {

    boolean existsByNumProcesso(String numProcesso);
}
