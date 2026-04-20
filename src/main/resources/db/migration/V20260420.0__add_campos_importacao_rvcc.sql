ALTER TABLE IF EXISTS public.rvcc_t_unidade_competencia
    ADD COLUMN IF NOT EXISTS codigo_modulo_formativo VARCHAR(50);

ALTER TABLE IF EXISTS public.rvcc_t_unidade_competencia
    ADD COLUMN IF NOT EXISTS denominacao_mf VARCHAR(255);

ALTER TABLE IF EXISTS public.rvcc_t_atividade_unidade_competencia
    ADD COLUMN IF NOT EXISTS requisitos TEXT;

ALTER TABLE IF EXISTS public.rvcc_t_atividade_unidade_competencia
    ADD COLUMN IF NOT EXISTS conhecimentos TEXT;
