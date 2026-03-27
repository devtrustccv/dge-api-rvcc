ALTER TABLE IF EXISTS public.rvcc_t_processo_rvcc
    ADD COLUMN IF NOT EXISTS id_entidade INT;

ALTER TABLE IF EXISTS public.rvcc_t_processo_rvcc
    DROP CONSTRAINT IF EXISTS fk_rvcc_processo_entidade;

ALTER TABLE IF EXISTS public.rvcc_t_processo_rvcc
    ADD CONSTRAINT fk_rvcc_processo_entidade
        FOREIGN KEY (id_entidade)
        REFERENCES public.rvcc_t_entidade(id_entidade)
        ON UPDATE CASCADE
        ON DELETE SET NULL;

