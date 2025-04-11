/* Esto permite insertar datos de prueba, cuando los tengan, pueden comentar el codigo*/

INSERT INTO categoria (nombre_categoria) VALUES ('Hogar');
INSERT INTO categoria (nombre_categoria) VALUES ('Ropa');
INSERT INTO categoria (nombre_categoria) VALUES ('Tecnología');

INSERT INTO productos (nombre, descripcion, cantidad_disponible, precio_compra, stock_minimo, deleted_at, categoria_id)
VALUES 
('Aspiradora', 'Aspiradora eléctrica para el hogar', 10, 350000.00, 2, NULL, 1),
('Camiseta', 'Camiseta de algodón', 25, 25000.00, 5, NULL, 2),
('Smartphone', 'Teléfono inteligente gama media', 15, 900000.00, 3, NULL, 3),
('Silla de comedor', 'Silla de madera para comedor', 8, 120000.00, 2, NULL, 1),
('Jeans', 'Pantalón de mezclilla azul', 30, 60000.00, 5, NULL, 2);

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
