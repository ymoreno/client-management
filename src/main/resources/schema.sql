-- Crear secuencias para generar números de cuenta
-- Secuencia para cuentas corrientes (prefijo 33)
CREATE SEQUENCE IF NOT EXISTS seq_account_33 START WITH 1 INCREMENT BY 1;

-- Secuencia para cuentas de ahorros (prefijo 53)  
CREATE SEQUENCE IF NOT EXISTS seq_account_53 START WITH 1 INCREMENT BY 1;