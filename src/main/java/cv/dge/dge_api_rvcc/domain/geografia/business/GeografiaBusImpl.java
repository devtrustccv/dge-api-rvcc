package cv.dge.dge_api_rvcc.domain.geografia.business;

import cv.dge.dge_api_rvcc.application.geografia.enums.TypeGeografia;
import cv.dge.dge_api_rvcc.infrastructure.geografia.GlobalGeografiaEntity;
import cv.dge.dge_api_rvcc.infrastructure.geografia.VGeograficaEntity;
import cv.dge.dge_api_rvcc.infrastructure.geografia.repository.JpaGlobalGeografiaRepository;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class GeografiaBusImpl implements GeografiaBus {

    private final JpaGlobalGeografiaRepository jpaGeografiaRepository;

    @Override
    public List<GlobalGeografiaEntity> findIlhaByPais(String pais) {
        return jpaGeografiaRepository.findIlhaByPais(pais);
    }

    @Override
    public List<GlobalGeografiaEntity> findConcelhoByIlha(String ilha) {
        return jpaGeografiaRepository.findConcelhoByIlha(ilha);
    }

    @Override
    public List<GlobalGeografiaEntity> findNacionalidade() {
        return jpaGeografiaRepository.findNacionalidade()
                .stream()
                .sorted(Comparator.comparing(GlobalGeografiaEntity::getNome))
                .toList();
    }

    @Override
    public List<VGeograficaEntity> getLocalidadeByPai(TypeGeografia tipo, String idPai) {
        return jpaGeografiaRepository.getLocalidades(tipo.name(), idPai);
    }

    @Override
    public String findNameById(String id) {
        if (!StringUtils.hasText(id)) {
            return null;
        }

        String codigo = id.trim();
        return jpaGeografiaRepository.findFirstByIdOrCodigo(codigo, codigo)
                .map(GlobalGeografiaEntity::getNome)
                .filter(StringUtils::hasText)
                .orElse(codigo);
    }
}
