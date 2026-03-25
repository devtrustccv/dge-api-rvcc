ALTER TABLE IF EXISTS public.rvcc_t_entidade
    ADD COLUMN IF NOT EXISTS id_organica VARCHAR(100);
