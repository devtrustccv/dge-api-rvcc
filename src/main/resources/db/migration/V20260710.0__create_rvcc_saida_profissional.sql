CREATE TABLE IF NOT EXISTS public.rvcc_t_saida_profissional (
    id_saida_profissional SERIAL PRIMARY KEY,
    codigo                VARCHAR(50)  NOT NULL,
    denominacao           VARCHAR(255) NOT NULL,
    id_qualificacao       INT,
    id_referencial        INT,
    estado                VARCHAR(50),
    CONSTRAINT fk_saida_profissional_qualificacao
        FOREIGN KEY (id_qualificacao)
            REFERENCES public.rvcc_t_qualificacao_profissional(id_qualificacao)
            ON UPDATE CASCADE
            ON DELETE SET NULL
);
