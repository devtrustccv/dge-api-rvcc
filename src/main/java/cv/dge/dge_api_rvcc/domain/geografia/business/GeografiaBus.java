package cv.dge.dge_api_rvcc.domain.geografia.business;

import cv.dge.dge_api_rvcc.application.geografia.enums.TypeGeografia;
import cv.dge.dge_api_rvcc.infrastructure.geografia.GlobalGeografiaEntity;
import cv.dge.dge_api_rvcc.infrastructure.geografia.VGeograficaEntity;
import java.util.List;

public interface GeografiaBus {

    List<GlobalGeografiaEntity> findIlhaByPais(String pais);

    List<GlobalGeografiaEntity> findConcelhoByIlha(String ilha);

    List<GlobalGeografiaEntity> findNacionalidade();

    List<VGeograficaEntity> getLocalidadeByPai(TypeGeografia tipo, String idPai);

    String findNameById(String id);
}
