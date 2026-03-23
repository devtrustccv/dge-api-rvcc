ALTER TABLE IF EXISTS public.rvcc_t_tipo_entidade
    ADD COLUMN IF NOT EXISTS id_entidade INT;

DO $$
BEGIN
    IF to_regclass('public.rvcc_t_entidade_tipo') IS NULL THEN
        RETURN;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM public.rvcc_t_entidade_tipo
        GROUP BY id_tipo_entidade
        HAVING COUNT(DISTINCT id_entidade) > 1
    ) THEN
        RAISE EXCEPTION
            'Nao foi possivel migrar rvcc_t_entidade_tipo: existem tipos de entidade associados a mais de uma entidade.';
    END IF;

    UPDATE public.rvcc_t_tipo_entidade tipo
    SET id_entidade = rel.id_entidade
    FROM public.rvcc_t_entidade_tipo rel
    WHERE tipo.id_tipo_entidade = rel.id_tipo_entidade
      AND tipo.id_entidade IS NULL;
END $$;

ALTER TABLE IF EXISTS public.rvcc_t_tipo_entidade
    DROP CONSTRAINT IF EXISTS fk_rvcc_tipo_entidade_entidade;

ALTER TABLE IF EXISTS public.rvcc_t_tipo_entidade
    ADD CONSTRAINT fk_rvcc_tipo_entidade_entidade
        FOREIGN KEY (id_entidade)
        REFERENCES public.rvcc_t_entidade(id_entidade)
        ON UPDATE CASCADE
        ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_rvcc_t_tipo_entidade_id_entidade
    ON public.rvcc_t_tipo_entidade (id_entidade);

DROP TABLE IF EXISTS public.rvcc_t_entidade_tipo;
