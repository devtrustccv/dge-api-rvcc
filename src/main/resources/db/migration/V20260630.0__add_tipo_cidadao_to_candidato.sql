ALTER TABLE IF EXISTS public.rvcc_t_candidato
    ADD COLUMN IF NOT EXISTS tipo_cidadao VARCHAR(50);
