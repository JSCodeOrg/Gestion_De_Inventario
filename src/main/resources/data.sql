/*Esto permite insertar datos de prueba, cuando los tengan, pueden comentar el codigo*/
/* Los truncate son para reiniciar los IDS de las tablas, por si crean datos en la bd y los borran, vuelvan a empezar desde 0 las primary key, 
mientras tengan datos los dejan comentados*/

/*
TRUNCATE TABLE productos RESTART IDENTITY CASCADE;
TRUNCATE TABLE categoria RESTART IDENTITY CASCADE;
TRUNCATE TABLE imagenes RESTART IDENTITY CASCADE;
*/
/*

CREATE EXTENSION IF NOT EXISTS unaccent;

INSERT INTO categoria (nombre_categoria) VALUES ('Hogar');
INSERT INTO categoria (nombre_categoria) VALUES ('Ropa');
INSERT INTO categoria (nombre_categoria) VALUES ('Tecnología');
INSERT INTO categoria (nombre_categoria) VALUES ('Deportes');

INSERT INTO productos (nombre, descripcion, cantidad_disponible, precio_compra, stock_minimo, deleted_at, categoria_id, palabras_clave)
VALUES 
('Aspiradora', 'Aspiradora eléctrica para el hogar', 10, 350000.00, 2, NULL, 1, 'aspiradora, limpieza, hogar, electrodoméstico'),
('Camiseta', 'Camiseta de algodón', 25, 25000.00, 5, NULL, 2, 'camiseta, ropa, algodón, camiseta básica'),
('Smartphone', 'Teléfono inteligente gama media', 15, 900000.00, 3, NULL, 3, 'smartphone, celular, android, tecnología'),
('Silla de comedor', 'Silla de madera para comedor', 8, 120000.00, 2, NULL, 1, 'silla, comedor, madera, mueble'),
('Jeans', 'Pantalón de mezclilla azul', 30, 60000.00, 5, NULL, 2, 'jeans, mezclilla, pantalón, azul'),
('Balón de fútbol', 'Balón de fútbol profesional talla 5', 20, 45000.00, 4, NULL, 4, 'balón, fútbol, deporte, pelota'),
('Raqueta de tenis', 'Raqueta de tenis ligera para principiantes', 12, 80000.00, 3, NULL, 4, 'raqueta, tenis, deporte, juego');

INSERT INTO imagenes (producto_id, image_url) VALUES (1, 'https://ejemplo.com/aspiradora1.jpg');
INSERT INTO imagenes (producto_id, image_url) VALUES (1, 'https://ejemplo.com/aspiradora2.jpg');

INSERT INTO imagenes (producto_id, image_url) VALUES (2, 'https://ejemplo.com/camiseta1.jpg');
INSERT INTO imagenes (producto_id, image_url) VALUES (2, 'https://ejemplo.com/camiseta2.jpg');

INSERT INTO imagenes (producto_id, image_url) VALUES (3, 'https://ejemplo.com/smartphone1.jpg');
INSERT INTO imagenes (producto_id, image_url) VALUES (3, 'https://ejemplo.com/smartphone2.jpg');

INSERT INTO imagenes (producto_id, image_url) VALUES (4, 'https://ejemplo.com/silla1.jpg');
INSERT INTO imagenes (producto_id, image_url) VALUES (4, 'https://ejemplo.com/silla2.jpg');

INSERT INTO imagenes (producto_id, image_url) VALUES (5, 'https://ejemplo.com/jeans1.jpg');
INSERT INTO imagenes (producto_id, image_url) VALUES (5, 'https://ejemplo.com/jeans2.jpg');

INSERT INTO imagenes (producto_id, image_url) VALUES (6, 'https://ejemplo.com/balon1.jpg');
INSERT INTO imagenes (producto_id, image_url) VALUES (6, 'https://ejemplo.com/balon2.jpg');

INSERT INTO imagenes (producto_id, image_url) VALUES (7, 'https://ejemplo.com/raqueta1.jpg');
INSERT INTO imagenes (producto_id, image_url) VALUES (7, 'https://ejemplo.com/raqueta2.jpg');
*/