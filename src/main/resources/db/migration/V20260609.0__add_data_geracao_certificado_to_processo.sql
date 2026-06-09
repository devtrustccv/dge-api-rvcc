ALTER TABLE IF EXISTS public.rvcc_t_processo_rvcc
    ADD COLUMN IF NOT EXISTS data_geracao_certificado DATE;
