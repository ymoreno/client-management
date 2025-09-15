# Comandos cURL para Client Management API

## Configuración Base
```bash
BASE_URL="http://localhost:8080"
ADMIN_AUTH="admin:admin"
USER_AUTH="user:password"
```

## 1. Cliente Endpoints (Requiere ADMIN)

### 1.1 Crear Cliente
```bash
curl -u admin:admin \
  -X POST http://localhost:8080/client \
  -H "Content-Type: application/json" \
  -d '{
    "documentType": "CC",
    "numberId": 12345678,
    "names": "Juan Carlos",
    "surname": "Pérez García",
    "email": "juan.perez@email.com",
    "birthDate": "1990-05-15"
  }'
```

### 1.2 Obtener Cliente (reemplaza {CLIENT_ID})
```bash
curl -u admin:admin \
  -X GET http://localhost:8080/client/{CLIENT_ID}
```

### 1.3 Actualizar Cliente (reemplaza {CLIENT_ID})
```bash
curl -u admin:admin \
  -X PUT http://localhost:8080/client/{CLIENT_ID} \
  -H "Content-Type: application/json" \
  -d '{
    "documentType": "CC",
    "numberId": 12345678,
    "names": "Juan Carlos",
    "surname": "Pérez García Actualizado",
    "email": "juan.perez.updated@email.com",
    "birthDate": "1990-05-15"
  }'
```

### 1.4 Eliminar Cliente (reemplaza {CLIENT_ID})
```bash
curl -u admin:admin \
  -X DELETE http://localhost:8080/client/{CLIENT_ID}
```

## 2. Producto Endpoints (Requiere ADMIN)

### 2.1 Crear Cuenta Corriente (reemplaza {CLIENT_ID})
```bash
curl -u admin:admin \
  -X POST http://localhost:8080/product/checking \
  -H "Content-Type: application/json" \
  -d '{
    "accountType": "CC",
    "accountNumber": "3312345678",
    "balance": 1000.0,
    "gmfExempt": false,
    "owner": {
      "id": "{CLIENT_ID}"
    }
  }'
```

### 2.2 Crear Cuenta de Ahorros (reemplaza {CLIENT_ID})
```bash
curl -u admin:admin \
  -X POST http://localhost:8080/product/savings \
  -H "Content-Type: application/json" \
  -d '{
    "accountType": "CA",
    "accountNumber": "5387654321",
    "balance": 500.0,
    "gmfExempt": true,
    "owner": {
      "id": "{CLIENT_ID}"
    }
  }'
```

### 2.3 Activar Cuenta (reemplaza {PRODUCT_ID})
```bash
curl -u admin:admin \
  -X PUT http://localhost:8080/product/activate/{PRODUCT_ID}
```

### 2.4 Desactivar Cuenta (reemplaza {PRODUCT_ID})
```bash
curl -u admin:admin \
  -X PUT http://localhost:8080/product/deactivate/{PRODUCT_ID}
```

### 2.5 Cancelar Cuenta (reemplaza {PRODUCT_ID})
```bash
curl -u admin:admin \
  -X PUT http://localhost:8080/product/cancel/{PRODUCT_ID}
```

### 2.6 Obtener Producto por Número de Cuenta (como USER)
```bash
curl -u user:password \
  -X GET http://localhost:8080/product/3312345678
```

### 2.7 Obtener Producto por Número de Cuenta (como ADMIN)
```bash
curl -u admin:admin \
  -X GET http://localhost:8080/product/3312345678
```

## 3. Transacción Endpoints (Requiere autenticación)

### 3.1 Realizar Depósito (reemplaza {PRODUCT_ID})
```bash
curl -u user:password \
  -X POST http://localhost:8080/transaction \
  -d "originId={PRODUCT_ID}&type=DEPOSIT&amount=100.0"
```

### 3.2 Realizar Retiro (reemplaza {PRODUCT_ID})
```bash
curl -u admin:admin \
  -X POST http://localhost:8080/transaction \
  -d "originId={PRODUCT_ID}&type=WITHDRAWAL&amount=50.0"
```

### 3.3 Realizar Transferencia (reemplaza {ORIGIN_ID} y {TARGET_ID})
```bash
curl -u user:password \
  -X POST http://localhost:8080/transaction \
  -d "originId={ORIGIN_ID}&targetId={TARGET_ID}&type=TRANSFER&amount=25.0"
```

## 4. Tests de Autorización

### 4.1 Acceso sin autenticación (debe retornar 401)
```bash
curl -X GET http://localhost:8080/client/00000000-0000-0000-0000-000000000000
```

### 4.2 USER intentando crear cliente (debe retornar 403)
```bash
curl -u user:password \
  -X POST http://localhost:8080/client \
  -H "Content-Type: application/json" \
  -d '{
    "names": "Test",
    "surname": "User",
    "email": "test@test.com"
  }'
```

### 4.3 USER intentando crear producto (debe retornar 403)
```bash
curl -u user:password \
  -X POST http://localhost:8080/product/checking \
  -H "Content-Type: application/json" \
  -d '{
    "accountType": "CC",
    "accountNumber": "3399999999",
    "balance": 1000.0
  }'
```

## 5. Comandos con Verbose Output (para debugging)

### Ver headers y detalles de respuesta
```bash
curl -v -u admin:admin \
  -X GET http://localhost:8080/client/{CLIENT_ID}
```

### Solo mostrar código de respuesta HTTP
```bash
curl -s -o /dev/null -w "%{http_code}" -u admin:admin \
  -X GET http://localhost:8080/client/{CLIENT_ID}
```

### Mostrar tiempo de respuesta
```bash
curl -w "Time: %{time_total}s\n" -u admin:admin \
  -X GET http://localhost:8080/client/{CLIENT_ID}
```

## Notas Importantes

1. **Credenciales por defecto:**
   - Admin: `admin:admin` (rol ADMIN)
   - User: `user:password` (rol USER)

2. **Tipos de Cuenta:**
   - `CC` (Cuenta Corriente): prefijo `33`
   - `CA` (Cuenta de Ahorros): prefijo `53`

3. **Tipos de Transacción:**
   - `DEPOSIT`: Solo requiere originId
   - `WITHDRAWAL`: Solo requiere originId
   - `TRANSFER`: Requiere originId y targetId

4. **Validaciones:**
   - Números de cuenta deben tener exactamente 10 dígitos
   - Deben empezar con el prefijo correcto según el tipo
   - Los montos deben ser positivos
   - Los emails deben tener formato válido

5. **Códigos de Respuesta Esperados:**
   - `200`: OK (GET, PUT exitosos)
   - `201`: Created (POST exitoso)
   - `204`: No Content (DELETE exitoso)
   - `400`: Bad Request (datos inválidos)
   - `401`: Unauthorized (sin autenticación)
   - `403`: Forbidden (sin permisos)
   - `404`: Not Found (recurso no encontrado)