ALTER TABLE IF EXISTS public.rvcc_t_unidade_competencia_avaliada
    ADD COLUMN IF NOT EXISTS id_processo INT,
    ADD COLUMN IF NOT EXISTS utilizador_registo VARCHAR,
    ADD COLUMN IF NOT EXISTS  data_avaliacao  DATE;
    

ALTER TABLE IF EXISTS public.rvcc_t_unidade_competencia_avaliada
    DROP CONSTRAINT IF EXISTS fk_rvcc_uc_avaliada_processo;

ALTER TABLE IF EXISTS public.rvcc_t_unidade_competencia_avaliada
    ADD CONSTRAINT fk_rvcc_uc_avaliada_processo
        FOREIGN KEY (id_processo)
        REFERENCES public.rvcc_t_processo_rvcc(id_processo)
        ON UPDATE CASCADE
        ON DELETE CASCADE;
