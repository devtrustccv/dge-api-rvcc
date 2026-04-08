ALTER TABLE IF EXISTS public.rvcc_t_candidato
    ADD COLUMN IF NOT EXISTS empregado VARCHAR(50),
    ADD COLUMN IF NOT EXISTS endereco_atual TEXT;
