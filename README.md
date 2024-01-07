-- CREAMOS LA BASE DE DATOS
CREATE DATABASE IF NOT EXISTS BDD_HundirLaFlota;

-- SELECCIONAMOS LA BASE DE DATOS
use BDD_HundirLaFlota;

-- Borrar la tabla Disparos
DROP TABLE IF EXISTS Disparos;

-- Borrar la tabla Barcos
DROP TABLE IF EXISTS Barcos;

-- Borrar la tabla Partidas
DROP TABLE IF EXISTS Partidas;

-- Borrar la tabla Jugadores
DROP TABLE IF EXISTS Jugadores;

-- Tabla Jugadores
CREATE TABLE Jugadores (
    id_jugador INT AUTO_INCREMENT,
    nombre VARCHAR(10) NOT NULL,
	contraseña INT NOT NULL,
    PRIMARY KEY (id_jugador)
);

-- Tabla Partidas
CREATE TABLE Partidas (
    id_partida INT AUTO_INCREMENT,
    jugador_1 INT,
    jugador_2 INT,
    estado VARCHAR(1) NOT NULL,-- X -> Terminada, O -> En curso
    ganador INT,-- Id del jugador Ganador 
    ultimo_turno INT,-- Id del jugador del ultimo turno
    PRIMARY KEY (id_partida),
    FOREIGN KEY (jugador_1) REFERENCES Jugadores(id_jugador),
    FOREIGN KEY (jugador_2) REFERENCES Jugadores(id_jugador),
    FOREIGN KEY (ganador) REFERENCES Jugadores(id_jugador),
    FOREIGN KEY (ultimo_turno) REFERENCES Jugadores(id_jugador)
);


-- Tabla Barcos
CREATE TABLE Barcos (-- Solo 3 varco por jugador en cada partida
    id_barco INT AUTO_INCREMENT,
    id_partida INT,
    jugador_id INT,
    tamaño INT NOT NULL,-- Barco sea mayor que 2 y menor que 7
    posicion_x INT NOT NULL,
    posicion_y INT NOT NULL,
    orientacion VARCHAR(1) NOT NULL,-- V -> Vertical, H -> Horizontal
    PRIMARY KEY (id_barco),
    FOREIGN KEY (id_partida) REFERENCES Partidas(id_partida),
    FOREIGN KEY (jugador_id) REFERENCES Jugadores(id_jugador)
);

-- Tabla Disparos
CREATE TABLE Disparos (
    id_disparo INT AUTO_INCREMENT,
    id_partida INT,
    jugador_id INT,
    posicion_x INT NOT NULL,
    posicion_y INT NOT NULL,
    resultado VARCHAR(1) NOT NULL,-- A -> Agua,  T -> Tocado, H -> Hundido
    PRIMARY KEY (id_disparo),
    FOREIGN KEY (id_partida) REFERENCES Partidas(id_partida),
    FOREIGN KEY (jugador_id) REFERENCES Jugadores(id_jugador)
);
-- Datos de ejemplo para la tabla Jugadores
INSERT INTO Jugadores (nombre, contraseña) VALUES
    ('Jugador1', 123456),
    ('Jugador2', 789012),
    ('Jugador3', 345678);

-- Datos de ejemplo para la tabla Partidas
INSERT INTO Partidas (jugador_1, jugador_2, estado, ganador, ultimo_turno) VALUES
    (1, 2, 'O', NULL, 1),
    (2, 3, 'X', 2, 3),
    (3, 1, 'O', NULL, 3);

-- Datos de ejemplo para la tabla Barcos
INSERT INTO Barcos (id_partida, jugador_id, tamaño, posicion_x, posicion_y, orientacion) VALUES
    (1, 1, 3, 1, 1, 'H'),
    (1, 1, 4, 2, 3, 'V'),
    (2, 2, 5, 5, 5, 'H'),
    (2, 2, 6, 8, 2, 'V'),
    (3, 3, 3, 4, 6, 'H');

-- Datos de ejemplo para la tabla Disparos
INSERT INTO Disparos (id_partida, jugador_id, posicion_x, posicion_y, resultado) VALUES
    (1, 1, 2, 2, 'A'),
    (1, 2, 3, 3, 'T'),
    (2, 2, 5, 5, 'H'),
    (2, 1, 8, 2, 'A'),
    (3, 3, 4, 6, 'T');
-- Datos de ejemplo adicionales para la tabla Jugadores
INSERT INTO Jugadores (nombre, contraseña) VALUES
    ('Alice', 111111),
    ('Bob', 222222),
    ('Charlie', 333333),
    ('David', 444444),
    ('Eva', 555555);

-- Datos de ejemplo adicionales para la tabla Partidas
INSERT INTO Partidas (jugador_1, jugador_2, estado, ganador, ultimo_turno) VALUES
    (1, 2, 'O', NULL, 1),
    (2, 3, 'X', 2, 3),
    (3, 1, 'O', NULL, 3),
    (4, 5, 'O', NULL, 4),
    (1, 3, 'X', 1, 1),
    (2, 4, 'O', NULL, 2),
    (3, 5, 'X', 3, 3),
    (4, 1, 'O', NULL, 4),
    (5, 2, 'O', NULL, 5);

-- Datos de ejemplo adicionales para la tabla Barcos
INSERT INTO Barcos (id_partida, jugador_id, tamaño, posicion_x, posicion_y, orientacion) VALUES
    (1, 1, 3, 1, 1, 'H'),
    (1, 1, 4, 2, 3, 'V'),
    (2, 2, 5, 5, 5, 'H'),
    (2, 2, 6, 8, 2, 'V'),
    (3, 3, 3, 4, 6, 'H'),
    (4, 4, 4, 7, 3, 'V'),
    (5, 5, 5, 2, 2, 'H'),
    (6, 1, 3, 6, 7, 'V'),
    (7, 2, 4, 4, 1, 'H'),
    (8, 3, 5, 8, 5, 'V'),
    (9, 4, 6, 3, 4, 'H'),
    (10, 5, 3, 1, 9, 'V');

-- Datos de ejemplo adicionales para la tabla Disparos
INSERT INTO Disparos (id_partida, jugador_id, posicion_x, posicion_y, resultado) VALUES
    (1, 1, 2, 2, 'A'),
    (1, 2, 3, 3, 'T'),
    (2, 2, 5, 5, 'H'),
    (2, 1, 8, 2, 'A'),
    (3, 3, 4, 6, 'T'),
    (4, 4, 7, 3, 'H'),
    (5, 1, 6, 7, 'T'),
    (6, 2, 4, 1, 'A'),
    (7, 3, 8, 5, 'T'),
    (8, 4, 4, 4, 'A'),
    (9, 5, 3, 9, 'T'),
    (10, 1, 1, 1, 'A');
