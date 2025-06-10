-- Criação do banco de dados (schema)
CREATE DATABASE IF NOT EXISTS sistema_manutencao_equipamentos;

-- Usa o banco de dados
USE sistema_manutencao_equipamentos;

-- Tabela `Funcionarios`
CREATE TABLE IF NOT EXISTS Funcionarios (
    id_funcionario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    matricula VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    funcao VARCHAR(100) NOT NULL,
    telefone VARCHAR(20),
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL
) ENGINE = InnoDB;

-- Tabela `CategoriasEquipamento`
CREATE TABLE IF NOT EXISTS CategoriasEquipamento (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nome_categoria VARCHAR(100) UNIQUE NOT NULL
) ENGINE = InnoDB;

-- Tabela `StatusEquipamento`
CREATE TABLE IF NOT EXISTS StatusEquipamento (
    id_status INT AUTO_INCREMENT PRIMARY KEY,
    nome_status VARCHAR(50) UNIQUE NOT NULL
) ENGINE = InnoDB;

-- Tabela `Equipamentos`
CREATE TABLE IF NOT EXISTS Equipamentos (
    id_equipamento INT AUTO_INCREMENT PRIMARY KEY,
    nome_equipamento VARCHAR(255) NOT NULL,
    numero_serie VARCHAR(100) UNIQUE NOT NULL,
    data_aquisicao DATE NOT NULL,
    descricao_servico TEXT,
    id_categoria INT,
    id_status INT,
    localizacao VARCHAR(255),
    fabricante VARCHAR(100),
    modelo VARCHAR(100),
    id_responsavel INT,
    FOREIGN KEY (id_categoria) REFERENCES CategoriasEquipamento(id_categoria)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_status) REFERENCES StatusEquipamento(id_status)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_responsavel) REFERENCES Funcionarios(id_funcionario)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB;

-- Tabela `Manutencoes`
CREATE TABLE IF NOT EXISTS Manutencoes (
    id_manutencao INT AUTO_INCREMENT PRIMARY KEY,
    id_equipamento INT,
    id_responsavel INT,
    data_inicio_manutencao DATE NOT NULL,
    data_fim_manutencao DATE,
    descricao_servico TEXT,
    FOREIGN KEY (id_equipamento) REFERENCES Equipamentos(id_equipamento)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_responsavel) REFERENCES Funcionarios(id_funcionario)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB;

INSERT INTO CategoriasEquipamento (nome_categoria) VALUES 
('Impressora'),
('Perifericos'),
('Rede'),
('Ar-condicionado'),
('Ferramentas'),
('No-break'),
('Estabilizador'),
('Scanner'),
('Projetor'),
('Suporte Tecnico'),
('Servidores'),
('Monitores'),
('Mobilario Tecnico'),
('Cameras de Seguranca'),
('Telefonia');

INSERT INTO StatusEquipamento (nome_status) VALUES ('Em Manutencao');
INSERT INTO StatusEquipamento (nome_status) VALUES ('Ativo');
