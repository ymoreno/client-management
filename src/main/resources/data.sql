-- Datos iniciales para la aplicación
-- Este archivo se ejecuta después de schema.sql

-- Verificar que las secuencias existen y reiniciarlas si es necesario
-- (Solo se ejecuta si las secuencias ya existen)
SELECT setval('seq_account_33', 1, false);
SELECT setval('seq_account_53', 1, false);