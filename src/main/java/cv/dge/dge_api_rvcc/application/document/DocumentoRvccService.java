package cv.dge.dge_api_rvcc.application.document;

import cv.dge.dge_api_rvcc.infrastructure.tertiary.DocRelacaoEntity;
import cv.dge.dge_api_rvcc.infrastructure.tertiary.repository.DocRelacaoRepository;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class DocumentoRvccService {

    private static final String APP_CODE = "certificacao_rvcc";

    private final RestTemplate restTemplate;
    private final DocRelacaoRepository docRelacaoRepository;
    private final String baseServiceUrl;

    public DocumentoRvccService(
            RestTemplate restTemplate,
            DocRelacaoRepository docRelacaoRepository,
            @Value("${api.base.service.url}") String baseServiceUrl
    ) {
        this.restTemplate = restTemplate;
        this.docRelacaoRepository = docRelacaoRepository;
        this.baseServiceUrl = baseServiceUrl;
    }

    public String guardarDocumento(
            MultipartFile file,
            String tipoRelacao,
            Integer idRelacao,
            String numProcesso
    ) {
        String ext = getExtension(file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        String path = buildPath(tipoRelacao, numProcesso, idRelacao, fileName, ext);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("tipoRelacao", tipoRelacao);
        body.add("idRelacao", idRelacao);
        body.add("estado", "A");
        body.add("appCode", APP_CODE);
        body.add("fileName", fileName);
        body.add("path", path);

        try {
            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            body.add("file", resource);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o ficheiro: " + e.getMessage(), e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ResponseEntity<String> response = restTemplate.exchange(
                baseServiceUrl + "/documentos",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao guardar documento na base_service_api: " + response.getBody());
        }

        log.info("Documento guardado: path={}, processo={}", path, numProcesso);
        return path;
    }

    public List<DocRelacaoEntity> listarDocumentos(Integer idRelacao, String tipoRelacao) {
        return docRelacaoRepository.findByIdRelacaoAndTipoRelacaoAndAppCode(
                Long.valueOf(idRelacao), tipoRelacao, APP_CODE);
    }

    private String buildPath(String tipoRelacao, String numProcesso, Integer idRelacao, String fileName, String ext) {
        return APP_CODE + "/" + java.time.LocalDate.now().getYear()
                + "/processos/" + tipoRelacao + "/" + numProcesso + "/" + idRelacao + "/" + fileName + "." + ext;
    }

    private String getExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }
}
