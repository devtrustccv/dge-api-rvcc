ALTER TABLE IF EXISTS public.rvcc_t_processo_rvcc
    ADD COLUMN IF NOT EXISTS cod_acolhimento VARCHAR(100);
