ALTER TABLE IF EXISTS public.rvcc_t_unidade_competencia
    ADD COLUMN IF NOT EXISTS id_referencial INT;
