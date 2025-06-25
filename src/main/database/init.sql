
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






