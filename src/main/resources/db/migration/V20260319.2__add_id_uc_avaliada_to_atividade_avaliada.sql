ALTER TABLE public.rvcc_t_atividade_avaliada
ADD COLUMN IF NOT EXISTS id_uc_avaliada integer;

ALTER TABLE public.rvcc_t_atividade_avaliada
DROP CONSTRAINT IF EXISTS fk_rvcc_atividade_avaliada_uc_avaliada;

ALTER TABLE public.rvcc_t_atividade_avaliada
ADD CONSTRAINT fk_rvcc_atividade_avaliada_uc_avaliada
FOREIGN KEY (id_uc_avaliada)
REFERENCES public.rvcc_t_unidade_competencia_avaliada(id_uc_avaliada);
