ALTER TABLE IF EXISTS public.rvcc_t_encaminhamento
    ADD COLUMN IF NOT EXISTS encaminhado VARCHAR(4);

    