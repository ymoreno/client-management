-- Script para configurar la base de datos PostgreSQL
-- Ejecuta este script en tu base de datos antes de usar la aplicación

-- Conectarse a la base de datos 'test'
-- \c test;

-- Crear secuencias para generar números de cuenta
DROP SEQUENCE IF EXISTS seq_account_33;
DROP SEQUENCE IF EXISTS seq_account_53;

CREATE SEQUENCE seq_account_33 
    START WITH 1 
    INCREMENT BY 1 
    MINVALUE 1 
    MAXVALUE 99999999 
    CACHE 1;

CREATE SEQUENCE seq_account_53 
    START WITH 1 
    INCREMENT BY 1 
    MINVALUE 1 
    MAXVALUE 99999999 
    CACHE 1;

-- Verificar que las secuencias se crearon correctamente
SELECT sequence_name, last_value, increment_by 
FROM information_schema.sequences 
WHERE sequence_name IN ('seq_account_33', 'seq_account_53');

-- Probar las secuencias
SELECT 'Cuenta Corriente: 33' || lpad(nextval('seq_account_33')::text, 8, '0') as cuenta_corriente;
SELECT 'Cuenta Ahorros: 53' || lpad(nextval('seq_account_53')::text, 8, '0') as cuenta_ahorros;

-- Reiniciar las secuencias para empezar desde 1
SELECT setval('seq_account_33', 1, false);
SELECT setval('seq_account_53', 1, false);

COMMIT;