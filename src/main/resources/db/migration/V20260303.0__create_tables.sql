-- =====================================================
-- MODELO DE DADOS RVCC – POSTGRESQL
-- CRIAÇÃO COMPLETA DO ZERO
-- =====================================================

-- =====================================================
-- LIMPEZA INICIAL
-- =====================================================

DROP TABLE IF EXISTS public.rvcc_t_auditoria CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_notificacao CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_documento CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_encaminhamento CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_unidade_competencia_avaliada CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_atividade_avaliada CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_avaliacao CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_formacao_profissional CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_experiencia_profissional CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_ficha_percurso_profissional CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_agendamento_io CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_processo_rvcc CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_candidato CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_entidade_qualificacao CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_atividade_unidade_competencia CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_unidade_competencia CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_qualificacao_profissional CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_entidade_tipo CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_tipo_entidade CASCADE;
DROP TABLE IF EXISTS public.rvcc_t_entidade CASCADE;

-- =====================================================
-- 1. ESTRUTURA INSTITUCIONAL
-- =====================================================

CREATE TABLE public.rvcc_t_entidade (
                                        id_entidade SERIAL PRIMARY KEY,
                                        designacao_comercial VARCHAR(255),
                                        ilha VARCHAR(100),
                                        concelho VARCHAR(100),
                                        id_concelho INT,
                                        endereco VARCHAR(255),
                                        num_alvara VARCHAR(100),
                                        estado_alvara VARCHAR(50),
                                        ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE public.rvcc_t_tipo_entidade (
                                             id_tipo_entidade SERIAL PRIMARY KEY,
                                             codigo VARCHAR(20) UNIQUE NOT NULL,
                                             descricao VARCHAR(150)
);

CREATE TABLE public.rvcc_t_entidade_tipo (
                                             id_entidade INT NOT NULL,
                                             id_tipo_entidade INT NOT NULL,
                                             CONSTRAINT rvcc_t_entidade_tipo_pkey PRIMARY KEY (id_entidade, id_tipo_entidade),
                                             CONSTRAINT fk_rvcc_entidade_tipo_entidade
                                                 FOREIGN KEY (id_entidade)
                                                     REFERENCES public.rvcc_t_entidade(id_entidade)
                                                     ON UPDATE CASCADE
                                                     ON DELETE CASCADE,
                                             CONSTRAINT fk_rvcc_entidade_tipo_tipo
                                                 FOREIGN KEY (id_tipo_entidade)
                                                     REFERENCES public.rvcc_t_tipo_entidade(id_tipo_entidade)
                                                     ON UPDATE CASCADE
                                                     ON DELETE CASCADE
);

-- =====================================================
-- 2. CNQ / PAEF
-- =====================================================

CREATE TABLE public.rvcc_t_qualificacao_profissional (
                                                         id_qualificacao SERIAL PRIMARY KEY,
                                                         codigo_cnq VARCHAR(50) UNIQUE,
                                                         selfid_qp VARCHAR(50),
                                                         denominacao VARCHAR(255),
                                                         familia_profissional VARCHAR(150),
                                                         nivel_qnq INT,
                                                         ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE public.rvcc_t_unidade_competencia (
                                                   id_uc SERIAL PRIMARY KEY,
                                                   id_qualificacao INT,
                                                   codigo_uc VARCHAR(50),
                                                   denominacao VARCHAR(255),
                                                   descricao TEXT,
                                                   carga_horaria INT,
                                                   ativo BOOLEAN DEFAULT TRUE,
                                                   CONSTRAINT fk_rvcc_uc_qualificacao
                                                       FOREIGN KEY (id_qualificacao)
                                                           REFERENCES public.rvcc_t_qualificacao_profissional(id_qualificacao)
                                                           ON UPDATE CASCADE
                                                           ON DELETE CASCADE
);

CREATE TABLE public.rvcc_t_atividade_unidade_competencia (
                                                             id_atividade SERIAL PRIMARY KEY,
                                                             id_uc INT,
                                                             codigo_atividade VARCHAR(50),
                                                             descricao TEXT,
                                                             ponderacao INT,
                                                             ativo BOOLEAN DEFAULT TRUE,
                                                             CONSTRAINT fk_rvcc_atividade_uc
                                                                 FOREIGN KEY (id_uc)
                                                                     REFERENCES public.rvcc_t_unidade_competencia(id_uc)
                                                                     ON UPDATE CASCADE
                                                                     ON DELETE CASCADE
);

CREATE TABLE public.rvcc_t_entidade_qualificacao (
                                                     id_entidade INT NOT NULL,
                                                     id_qualificacao INT NOT NULL,
                                                     data_acreditacao DATE,
                                                     estado_acreditacao VARCHAR(50),
                                                     CONSTRAINT rvcc_t_entidade_qualificacao_pkey PRIMARY KEY (id_entidade, id_qualificacao),
                                                     CONSTRAINT fk_rvcc_entidade_qualificacao_entidade
                                                         FOREIGN KEY (id_entidade)
                                                             REFERENCES public.rvcc_t_entidade(id_entidade)
                                                             ON UPDATE CASCADE
                                                             ON DELETE CASCADE,
                                                     CONSTRAINT fk_rvcc_entidade_qualificacao_qualificacao
                                                         FOREIGN KEY (id_qualificacao)
                                                             REFERENCES public.rvcc_t_qualificacao_profissional(id_qualificacao)
                                                             ON UPDATE CASCADE
                                                             ON DELETE CASCADE
);

-- =====================================================
-- 3. CANDIDATO
-- =====================================================

CREATE TABLE public.rvcc_t_candidato (
                                         id_candidato SERIAL PRIMARY KEY,
                                         nome_completo VARCHAR(255),
                                         data_nascimento DATE,
                                         sexo VARCHAR(20),
                                         tipo_documento VARCHAR(50),
                                         numero_documento VARCHAR(50) UNIQUE,
                                         validade_documento DATE,
                                         data_emissao_documento DATE,
                                         nif VARCHAR(20),
                                         nacionalidade VARCHAR(100),
                                         naturalidade VARCHAR(100),
                                         morada TEXT,
                                         telefone VARCHAR(30),
                                         email VARCHAR(150),
                                         situacao_emprego VARCHAR(50),
                                         entidade_empregadora VARCHAR(255),
                                         profissao VARCHAR(150),
                                         habilitacoes_literarias VARCHAR(50),
                                         disponibilidade VARCHAR(50),
                                         idade INT,
                                         ilha VARCHAR(100),
                                         concelho VARCHAR(100),
                                         estado CHAR(1) DEFAULT 'A',
                                         utilizador_registo VARCHAR(150),
                                         data_registo TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- 4. PROCESSO RVCC
-- =====================================================

CREATE TABLE public.rvcc_t_processo_rvcc (
                                             id_processo SERIAL PRIMARY KEY,
                                             id_candidato INT,
                                             num_processo VARCHAR(50),
                                             estado VARCHAR(50),
                                             data_submissao TIMESTAMP,
                                             data_inicio TIMESTAMP,
                                             data_conclusao TIMESTAMP,
                                             observacoes TEXT,
                                             utilizador_registo VARCHAR,
                                             datareg TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             CONSTRAINT fk_rvcc_processo_candidato
                                                 FOREIGN KEY (id_candidato)
                                                     REFERENCES public.rvcc_t_candidato(id_candidato)
                                                     ON UPDATE CASCADE
                                                     ON DELETE CASCADE
);

-- =====================================================
-- 5. INFORMAÇÃO E ORIENTAÇÃO (IO)
-- =====================================================

CREATE TABLE public.rvcc_t_agendamento_io (
                                              id_agendamento SERIAL PRIMARY KEY,
                                              id_processo INT,
                                              id_entidade_cefp INT,
                                              data_agendada TIMESTAMP,
                                              data_realizacao TIMESTAMP,
                                              estado VARCHAR(30),
                                              modalidade VARCHAR(30),
                                              resultado_io VARCHAR(50),
                                              recomendacao TEXT,
                                              tecnico_entrevistador VARCHAR(255),
                                              criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                              utilizador_registo VARCHAR,
                                              datareg TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                              tipo_agendamento VARCHAR,
                                              CONSTRAINT fk_rvcc_agendamento_processo
                                                  FOREIGN KEY (id_processo)
                                                      REFERENCES public.rvcc_t_processo_rvcc(id_processo)
                                                      ON UPDATE CASCADE
                                                      ON DELETE CASCADE,
                                              CONSTRAINT fk_rvcc_agendamento_entidade
                                                  FOREIGN KEY (id_entidade_cefp)
                                                      REFERENCES public.rvcc_t_entidade(id_entidade)
                                                      ON UPDATE CASCADE
                                                      ON DELETE SET NULL
);

-- =====================================================
-- 6. FICHA DE PERCURSO
-- =====================================================

CREATE TABLE public.rvcc_t_ficha_percurso_profissional (
                                                           id_ficha SERIAL PRIMARY KEY,
                                                           id_processo INT,
                                                           id_qualificacao INT,
                                                           familia_profissional VARCHAR(150),
                                                           parecer_io VARCHAR(50),
                                                           data_parecer TIMESTAMP,
                                                           observacoes_finais TEXT,
                                                           utilizador_registo VARCHAR,
                                                           datareg TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                           CONSTRAINT fk_rvcc_ficha_processo
                                                               FOREIGN KEY (id_processo)
                                                                   REFERENCES public.rvcc_t_processo_rvcc(id_processo)
                                                                   ON UPDATE CASCADE
                                                                   ON DELETE CASCADE,
                                                           CONSTRAINT fk_rvcc_ficha_qualificacao
                                                               FOREIGN KEY (id_qualificacao)
                                                                   REFERENCES public.rvcc_t_qualificacao_profissional(id_qualificacao)
                                                                   ON UPDATE CASCADE
                                                                   ON DELETE SET NULL
);

CREATE TABLE public.rvcc_t_experiencia_profissional (
                                                        id_experiencia SERIAL PRIMARY KEY,
                                                        id_ficha INT,
                                                        nome_empresa VARCHAR(255),
                                                        morada_empresa TEXT,
                                                        ramo_atividade VARCHAR(150),
                                                        departamento VARCHAR(150),
                                                        categoria_profissional VARCHAR(150),
                                                        funcoes_desempenhadas TEXT,
                                                        pais VARCHAR(100),
                                                        CONSTRAINT fk_rvcc_experiencia_ficha
                                                            FOREIGN KEY (id_ficha)
                                                                REFERENCES public.rvcc_t_ficha_percurso_profissional(id_ficha)
                                                                ON UPDATE CASCADE
                                                                ON DELETE CASCADE
);

CREATE TABLE public.rvcc_t_formacao_profissional (
                                                     id_formacao SERIAL PRIMARY KEY,
                                                     id_ficha INT,
                                                     designacao_acao VARCHAR(255),
                                                     entidade_promotora VARCHAR(255),
                                                     duracao_horas INT,
                                                     data_realizacao DATE,
                                                     observacoes TEXT,
                                                     CONSTRAINT fk_rvcc_formacao_ficha
                                                         FOREIGN KEY (id_ficha)
                                                             REFERENCES public.rvcc_t_ficha_percurso_profissional(id_ficha)
                                                             ON UPDATE CASCADE
                                                             ON DELETE CASCADE
);

-- =====================================================
-- 7. AVALIAÇÃO RVCC
-- =====================================================

CREATE TABLE public.rvcc_t_avaliacao (
                                         id_avaliacao SERIAL PRIMARY KEY,
                                         id_processo INT,
                                         id_entidade_juri INT,
                                         resultado VARCHAR(50),
                                         media_final NUMERIC(10,2),
                                         observacoes TEXT,
                                         data_avaliacao TIMESTAMP,
                                         CONSTRAINT fk_rvcc_avaliacao_processo
                                             FOREIGN KEY (id_processo)
                                                 REFERENCES public.rvcc_t_processo_rvcc(id_processo)
                                                 ON UPDATE CASCADE
                                                 ON DELETE CASCADE,
                                         CONSTRAINT fk_rvcc_avaliacao_entidade
                                             FOREIGN KEY (id_entidade_juri)
                                                 REFERENCES public.rvcc_t_entidade(id_entidade)
                                                 ON UPDATE CASCADE
                                                 ON DELETE SET NULL
);

CREATE TABLE public.rvcc_t_atividade_avaliada (
                                                  id_atividade_avaliada SERIAL PRIMARY KEY,
                                                  id_avaliacao INT,
                                                  id_atividade INT,
                                                  instrumento_avaliacao VARCHAR(100),
                                                  pontuacao NUMERIC(10,2),
                                                  media_ponderada NUMERIC(10,2),
                                                  validada BOOLEAN,
                                                  observacoes TEXT,
                                                  CONSTRAINT fk_rvcc_atividade_avaliada_avaliacao
                                                      FOREIGN KEY (id_avaliacao)
                                                          REFERENCES public.rvcc_t_avaliacao(id_avaliacao)
                                                          ON UPDATE CASCADE
                                                          ON DELETE CASCADE,
                                                  CONSTRAINT fk_rvcc_atividade_avaliada_atividade
                                                      FOREIGN KEY (id_atividade)
                                                          REFERENCES public.rvcc_t_atividade_unidade_competencia(id_atividade)
                                                          ON UPDATE CASCADE
                                                          ON DELETE CASCADE
);

CREATE TABLE public.rvcc_t_unidade_competencia_avaliada (
                                                            id_uc_avaliada SERIAL PRIMARY KEY,
                                                            id_avaliacao INT,
                                                            id_uc INT,
                                                            media_final NUMERIC(10,2),
                                                            validada BOOLEAN,
                                                            CONSTRAINT fk_rvcc_uc_avaliada_avaliacao
                                                                FOREIGN KEY (id_avaliacao)
                                                                    REFERENCES public.rvcc_t_avaliacao(id_avaliacao)
                                                                    ON UPDATE CASCADE
                                                                    ON DELETE CASCADE,
                                                            CONSTRAINT fk_rvcc_uc_avaliada_uc
                                                                FOREIGN KEY (id_uc)
                                                                    REFERENCES public.rvcc_t_unidade_competencia(id_uc)
                                                                    ON UPDATE CASCADE
                                                                    ON DELETE CASCADE
);

-- =====================================================
-- 8. ENCAMINHAMENTO (PIE)
-- =====================================================

CREATE TABLE public.rvcc_t_encaminhamento (
                                              id_encaminhamento SERIAL PRIMARY KEY,
                                              id_processo INT,
                                              id_entidade_certificadora INT,
                                              id_qualificacao INT,
                                              designacao_oferta VARCHAR(255),
                                              modalidade VARCHAR(100),
                                              saida_profissional VARCHAR(150),
                                              tipo_certificacao VARCHAR(50),
                                              horario VARCHAR(50),
                                              lingua_estrangeira VARCHAR(100),
                                              outros_encaminhamentos TEXT,
                                              data_encaminhamento TIMESTAMP,
                                              CONSTRAINT fk_rvcc_encaminhamento_processo
                                                  FOREIGN KEY (id_processo)
                                                      REFERENCES public.rvcc_t_processo_rvcc(id_processo)
                                                      ON UPDATE CASCADE
                                                      ON DELETE CASCADE,
                                              CONSTRAINT fk_rvcc_encaminhamento_entidade
                                                  FOREIGN KEY (id_entidade_certificadora)
                                                      REFERENCES public.rvcc_t_entidade(id_entidade)
                                                      ON UPDATE CASCADE
                                                      ON DELETE SET NULL,
                                              CONSTRAINT fk_rvcc_encaminhamento_qualificacao
                                                  FOREIGN KEY (id_qualificacao)
                                                      REFERENCES public.rvcc_t_qualificacao_profissional(id_qualificacao)
                                                      ON UPDATE CASCADE
                                                      ON DELETE SET NULL
);

-- =====================================================
-- 9. DOCUMENTOS
-- =====================================================

CREATE TABLE public.rvcc_t_documento (
                                         id_documento SERIAL PRIMARY KEY,
                                         id_ficha INT,
                                         tipo_comprovativo VARCHAR(100),
                                         descricao VARCHAR(255),
                                         caminho_ficheiro VARCHAR(500),
                                         submetido_por VARCHAR(50),
                                         validado BOOLEAN,
                                         validado_por_entidade INT,
                                         data_submissao TIMESTAMP,
                                         data_validacao TIMESTAMP,
                                         CONSTRAINT fk_rvcc_documento_ficha
                                             FOREIGN KEY (id_ficha)
                                                 REFERENCES public.rvcc_t_ficha_percurso_profissional(id_ficha)
                                                 ON UPDATE CASCADE
                                                 ON DELETE CASCADE,
                                         CONSTRAINT fk_rvcc_documento_entidade
                                             FOREIGN KEY (validado_por_entidade)
                                                 REFERENCES public.rvcc_t_entidade(id_entidade)
                                                 ON UPDATE CASCADE
                                                 ON DELETE SET NULL
);

-- =====================================================
-- 10. NOTIFICAÇÕES
-- =====================================================

CREATE TABLE public.rvcc_t_notificacao (
                                           id_notificacao SERIAL PRIMARY KEY,
                                           id_processo INT,
                                           tipo VARCHAR(30),
                                           assunto VARCHAR(255),
                                           mensagem TEXT,
                                           destinatario VARCHAR(255),
                                           enviado_em TIMESTAMP,
                                           CONSTRAINT fk_rvcc_notificacao_processo
                                               FOREIGN KEY (id_processo)
                                                   REFERENCES public.rvcc_t_processo_rvcc(id_processo)
                                                   ON UPDATE CASCADE
                                                   ON DELETE CASCADE
);

-- =====================================================
-- 11. AUDITORIA
-- =====================================================

CREATE TABLE public.rvcc_t_auditoria (
                                         id_auditoria SERIAL PRIMARY KEY,
                                         entidade_afetada VARCHAR(100),
                                         id_registo INT,
                                         operacao VARCHAR(50),
                                         executado_por_entidade INT,
                                         user_execute VARCHAR(255),
                                         data_evento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         detalhe TEXT,
                                         CONSTRAINT fk_rvcc_auditoria_entidade
                                             FOREIGN KEY (executado_por_entidade)
                                                 REFERENCES public.rvcc_t_entidade(id_entidade)
                                                 ON UPDATE CASCADE
                                                 ON DELETE SET NULL
);
