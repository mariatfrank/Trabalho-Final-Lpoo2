-- Criação das tabelas

CREATE TABLE Cliente (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    telefone TEXT
);

CREATE TABLE Pedido (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cliente_id INTEGER,
    preco_total REAL,
    estado TEXT,
    FOREIGN KEY(cliente_id) REFERENCES Cliente(id)
);

CREATE TABLE Pizza (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    pedido_id INTEGER,
    forma TEXT,
    dimensao REAL,
    preco REAL,
    FOREIGN KEY(pedido_id) REFERENCES Pedido(id)
);

CREATE TABLE Sabor (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL
);

CREATE TABLE Pizza_Sabor (
    pizza_id INTEGER,
    sabor_id INTEGER,
    PRIMARY KEY (pizza_id, sabor_id),
    FOREIGN KEY(pizza_id) REFERENCES Pizza(id),
    FOREIGN KEY(sabor_id) REFERENCES Sabor(id)
);

-- Inserir clientes de exemplo

INSERT INTO Cliente (nome, telefone) VALUES ('João Silva', '9999-9999');
INSERT INTO Cliente (nome, telefone) VALUES ('Maria Oliveira', '8888-8888');
INSERT INTO Cliente (nome, telefone) VALUES ('Carlos Santos', '7777-7777');

-- Inserir sabores de exemplo

INSERT INTO Sabor (nome) VALUES ('Calabresa');
INSERT INTO Sabor (nome) VALUES ('Mussarela');
INSERT INTO Sabor (nome) VALUES ('Portuguesa');
INSERT INTO Sabor (nome) VALUES ('Frango com Catupiry');
INSERT INTO Sabor (nome) VALUES ('Chocolate');
