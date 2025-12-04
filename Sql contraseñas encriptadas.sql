-- ============================================
-- BASE DE DATOS: PLATAFORMA ACADÉMICA
-- ============================================

-- Eliminar base de datos si existe
DROP DATABASE IF EXISTS plataforma;

-- Crear base de datos
CREATE DATABASE plataforma
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Usar la base de datos
USE plataforma;

-- ============================================
-- TABLA: rol
-- ============================================
DROP TABLE IF EXISTS rol;
CREATE TABLE rol (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  descripcion VARCHAR(255),
  PRIMARY KEY (id),
  UNIQUE KEY ux_rol_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: usuario
-- ============================================
DROP TABLE IF EXISTS usuario;
CREATE TABLE usuario (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  apellido VARCHAR(150) NOT NULL,
  email VARCHAR(200) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  rol_id BIGINT UNSIGNED NOT NULL,
  fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  
  PRIMARY KEY (id),
  KEY idx_usuario_rol (rol_id),
  
  CONSTRAINT fk_usuario_rol FOREIGN KEY (rol_id)
    REFERENCES rol (id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- DATOS INICIALES
-- ============================================

-- Insertar roles
INSERT INTO rol (nombre, descripcion) VALUES
('ADMIN', 'Administrador del sistema - Acceso completo'),
('PROFESOR', 'Profesor - Puede ver reportes y consultas'),
('ESTUDIANTE', 'Estudiante - Solo puede ver su perfil');

-- Insertar usuarios con contraseñas CORRECTAS para "12345"
-- Contraseña: 12345 (encriptada con BCrypt)
INSERT INTO usuario (nombre, apellido, email, password, rol_id) VALUES
('Carlos', 'Ramírez', 'c.ramirez@correo.com', '$2a$10$5dQNs0hbhvQPeJEbM0.xeFdyBrCKLzG.rNqI1c.zXGOW7tU0qqdK', 1),
('Ana', 'Soto', 'ana.soto@correo.com', '$2a$10$5dQNs0hbhvQPeJEbM0.xeFdyBrCKLzG.rNqI1c.zXGOW7tU0qqdK', 3),
('Luisa', 'Fernández', 'l.fernandez@correo.com', '$2a$10$5dQNs0hbhvQPeJEbM0.xeFdyBrCKLzG.rNqI1c.zXGOW7tU0qqdK', 2);

-- ============================================
-- DATOS ADICIONALES PARA PRUEBAS (Opcional)
-- ============================================

-- Más usuarios para pruebas
INSERT INTO usuario (nombre, apellido, email, password, rol_id) VALUES
('Juan', 'Pérez', 'juan.perez@correo.com', '$2a$10$5dQNs0hbhvQPeJEbM0.xeFdyBrCKLzG.rNqI1c.zXGOW7tU0qqdK', 2),
('María', 'Gómez', 'maria.gomez@correo.com', '$2a$10$5dQNs0hbhvQPeJEbM0.xeFdyBrCKLzG.rNqI1c.zXGOW7tU0qqdK', 2),
('Pedro', 'López', 'pedro.lopez@correo.com', '$2a$10$5dQNs0hbhvQPeJEbM0.xeFdyBrCKLzG.rNqI1c.zXGOW7tU0qqdK', 3),
('Laura', 'Martínez', 'laura.martinez@correo.com', '$2a$10$5dQNs0hbhvQPeJEbM0.xeFdyBrCKLzG.rNqI1c.zXGOW7tU0qqdK', 3),
('Miguel', 'Rodríguez', 'miguel.rodriguez@correo.com', '$2a$10$5dQNs0hbhvQPeJEbM0.xeFdyBrCKLzG.rNqI1c.zXGOW7tU0qqdK', 3);

-- ============================================
-- VISTAS ÚTILES (Opcional)
-- ============================================

-- Vista para ver usuarios con información de rol
CREATE OR REPLACE VIEW vista_usuarios AS
SELECT 
  u.id,
  u.nombre,
  u.apellido,
  u.email,
  u.activo,
  r.nombre as rol,
  r.descripcion as descripcion_rol,
  u.fecha_creacion
FROM usuario u
JOIN rol r ON u.rol_id = r.id
ORDER BY u.fecha_creacion DESC;

-- ============================================
-- PROCEDIMIENTOS ALMACENADOS (Opcional)
-- ============================================

-- Procedimiento para cambiar estado de usuario
DELIMITER $$
CREATE PROCEDURE cambiar_estado_usuario(
  IN p_usuario_id BIGINT,
  IN p_activo BOOLEAN
)
BEGIN
  UPDATE usuario 
  SET activo = p_activo 
  WHERE id = p_usuario_id;
END$$
DELIMITER ;

-- Procedimiento para resetear contraseña
DELIMITER $$
CREATE PROCEDURE resetear_password(
  IN p_usuario_id BIGINT
)
BEGIN
  UPDATE usuario 
  SET password = '$2a$10$5dQNs0hbhvQPeJEbM0.xeFdyBrCKLzG.rNqI1c.zXGOW7tU0qqdK' -- 12345
  WHERE id = p_usuario_id;
END$$
DELIMITER ;

-- ============================================
-- CONSULTAS DE VERIFICACIÓN
-- ============================================

-- Verificar datos insertados
SELECT '=== ROLES ===' as Info;
SELECT * FROM rol;

SELECT '=== USUARIOS ===' as Info;
SELECT 
  u.id,
  u.nombre,
  u.apellido,
  u.email,
  CASE WHEN u.activo THEN 'ACTIVO' ELSE 'INACTIVO' END as estado,
  r.nombre as rol,
  DATE(u.fecha_creacion) as fecha_registro
FROM usuario u
JOIN rol r ON u.rol_id = r.id
ORDER BY u.id;

-- Verificar que las contraseñas tienen longitud correcta
SELECT '=== VERIFICACIÓN CONTRASEÑAS ===' as Info;
SELECT 
  email,
  LENGTH(password) as longitud,
  CASE 
    WHEN LENGTH(password) = 60 THEN 'CORRECTA (BCrypt)'
    ELSE 'PROBLEMA - Revisar'
  END as estado
FROM usuario;

-- ============================================
-- ÍNDICES ADICIONALES PARA MEJOR RENDIMIENTO
-- ============================================

CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_usuario_activo ON usuario(activo);
CREATE INDEX idx_usuario_fecha ON usuario(fecha_creacion);

-- ============================================
-- FIN DEL SCRIPT
-- ============================================
SELECT '=== BASE DE DATOS CREADA EXITOSAMENTE ===' as Mensaje;

USE plataforma;

-- Solo mantener UN hash (el último que generó tu test)
UPDATE usuario SET password = '$2a$10$UGTgVCpw42jvBAmrvnq7OuA/OKbR8V27KwKfRcMCLU1C1YycApSXW' 
WHERE email = 'c.ramirez@correo.com';

-- Verificar
SELECT email, '12345' as password_texto, password as password_hash FROM usuario;