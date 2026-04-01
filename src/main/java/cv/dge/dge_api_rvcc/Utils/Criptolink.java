package cv.dge.dge_api_rvcc.Utils;

public class Criptolink {

    public String gerarTokenCriptografadoDeclaracao(Integer id) {
        String rotaInterna = "equiv/declaracao_equivalencia" + id;
        try {
            return AESUtil.encrypt(rotaInterna);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar rota: " + e.getMessage());
        }
    }

    public String gerarTokenCriptografadoComprovativo(Integer id) {
        String rotaInterna = "equiv/comprovativo_do_recibo_do_pedido" + id;
        try {
            return AESUtil.encrypt(rotaInterna);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar rota: " + e.getMessage());
        }
    }

}
