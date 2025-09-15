#!/bin/bash

# Smoke Test para Client Management API
# Asegúrate de que la aplicación esté ejecutándose en http://localhost:8080

BASE_URL="http://localhost:8080"
ADMIN_AUTH="admin:admin"
USER_AUTH="user:password"

echo "=== SMOKE TEST - CLIENT MANAGEMENT API ==="
echo "Base URL: $BASE_URL"
echo ""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para mostrar resultados
show_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ PASS${NC}"
    else
        echo -e "${RED}✗ FAIL${NC}"
    fi
    echo ""
}

# Variables para IDs generados
CLIENT_ID=""
PRODUCT_ID=""
ACCOUNT_NUMBER=""

echo "=== 1. CLIENTE ENDPOINTS (Requiere ADMIN) ==="

echo -n "1.1 Crear Cliente: "
RESPONSE=$(curl -s -w "%{http_code}" -u "$ADMIN_AUTH" \
  -X POST "$BASE_URL/client" \
  -H "Content-Type: application/json" \
  -d '{
    "documentType": "CC",
    "numberId": 12345678,
    "names": "Juan Carlos",
    "surname": "Pérez García",
    "email": "juan.perez@email.com",
    "birthDate": "1990-05-15"
  }')

HTTP_CODE="${RESPONSE: -3}"
BODY="${RESPONSE%???}"

if [ "$HTTP_CODE" = "201" ]; then
    CLIENT_ID=$(echo "$BODY" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
    echo -e "${GREEN}✓ PASS${NC} (ID: $CLIENT_ID)"
else
    echo -e "${RED}✗ FAIL${NC} (HTTP: $HTTP_CODE)"
    echo "Response: $BODY"
fi
echo ""

if [ -n "$CLIENT_ID" ]; then
    echo -n "1.2 Obtener Cliente: "
    RESPONSE=$(curl -s -w "%{http_code}" -u "$ADMIN_AUTH" \
      -X GET "$BASE_URL/client/$CLIENT_ID")
    
    HTTP_CODE="${RESPONSE: -3}"
    if [ "$HTTP_CODE" = "200" ]; then
        show_result 0
    else
        show_result 1
        echo "HTTP Code: $HTTP_CODE"
    fi

    echo -n "1.3 Actualizar Cliente: "
    RESPONSE=$(curl -s -w "%{http_code}" -u "$ADMIN_AUTH" \
      -X PUT "$BASE_URL/client/$CLIENT_ID" \
      -H "Content-Type: application/json" \
      -d '{
        "documentType": "CC",
        "numberId": 12345678,
        "names": "Juan Carlos",
        "surname": "Pérez García Actualizado",
        "email": "juan.perez.updated@email.com",
        "birthDate": "1990-05-15"
      }')
    
    HTTP_CODE="${RESPONSE: -3}"
    if [ "$HTTP_CODE" = "200" ]; then
        show_result 0
    else
        show_result 1
        echo "HTTP Code: $HTTP_CODE"
    fi
fi

echo "=== 2. PRODUCTO ENDPOINTS (Requiere ADMIN) ==="

if [ -n "$CLIENT_ID" ]; then
    echo -n "2.1 Crear Cuenta Corriente: "
    RESPONSE=$(curl -s -w "%{http_code}" -u "$ADMIN_AUTH" \
      -X POST "$BASE_URL/product/checking" \
      -H "Content-Type: application/json" \
      -d '{
        "accountType": "CC",
        "accountNumber": "3312345678",
        "balance": 1000.0,
        "gmfExempt": false,
        "owner": {
          "id": "'$CLIENT_ID'"
        }
      }')
    
    HTTP_CODE="${RESPONSE: -3}"
    BODY="${RESPONSE%???}"
    
    if [ "$HTTP_CODE" = "201" ]; then
        PRODUCT_ID=$(echo "$BODY" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
        ACCOUNT_NUMBER=$(echo "$BODY" | grep -o '"accountNumber":"[^"]*"' | cut -d'"' -f4)
        echo -e "${GREEN}✓ PASS${NC} (ID: $PRODUCT_ID, Account: $ACCOUNT_NUMBER)"
    else
        show_result 1
        echo "HTTP Code: $HTTP_CODE"
        echo "Response: $BODY"
    fi
    echo ""

    echo -n "2.2 Crear Cuenta de Ahorros: "
    RESPONSE=$(curl -s -w "%{http_code}" -u "$ADMIN_AUTH" \
      -X POST "$BASE_URL/product/savings" \
      -H "Content-Type: application/json" \
      -d '{
        "accountType": "CA",
        "accountNumber": "5387654321",
        "balance": 500.0,
        "gmfExempt": true,
        "owner": {
          "id": "'$CLIENT_ID'"
        }
      }')
    
    HTTP_CODE="${RESPONSE: -3}"
    if [ "$HTTP_CODE" = "201" ]; then
        show_result 0
    else
        show_result 1
        echo "HTTP Code: $HTTP_CODE"
    fi
fi

if [ -n "$PRODUCT_ID" ]; then
    echo -n "2.3 Desactivar Cuenta: "
    RESPONSE=$(curl -s -w "%{http_code}" -u "$ADMIN_AUTH" \
      -X PUT "$BASE_URL/product/deactivate/$PRODUCT_ID")
    
    HTTP_CODE="${RESPONSE: -3}"
    if [ "$HTTP_CODE" = "200" ]; then
        show_result 0
    else
        show_result 1
        echo "HTTP Code: $HTTP_CODE"
    fi

    echo -n "2.4 Activar Cuenta: "
    RESPONSE=$(curl -s -w "%{http_code}" -u "$ADMIN_AUTH" \
      -X PUT "$BASE_URL/product/activate/$PRODUCT_ID")
    
    HTTP_CODE="${RESPONSE: -3}"
    if [ "$HTTP_CODE" = "200" ]; then
        show_result 0
    else
        show_result 1
        echo "HTTP Code: $HTTP_CODE"
    fi
fi

if [ -n "$ACCOUNT_NUMBER" ]; then
    echo -n "2.5 Obtener Producto por Número de Cuenta (como USER): "
    RESPONSE=$(curl -s -w "%{http_code}" -u "$USER_AUTH" \
      -X GET "$BASE_URL/product/$ACCOUNT_NUMBER")
    
    HTTP_CODE="${RESPONSE: -3}"
    if [ "$HTTP_CODE" = "200" ]; then
        show_result 0
    else
        show_result 1
        echo "HTTP Code: $HTTP_CODE"
    fi

    echo -n "2.6 Obtener Producto por Número de Cuenta (como ADMIN): "
    RESPONSE=$(curl -s -w "%{http_code}" -u "$ADMIN_AUTH" \
      -X GET "$BASE_URL/product/$ACCOUNT_NUMBER")
    
    HTTP_CODE="${RESPONSE: -3}"
    if [ "$HTTP_CODE" = "200" ]; then
        show_result 0
    else
        show_result 1
        echo "HTTP Code: $HTTP_CODE"
    fi
fi

echo "=== 3. TRANSACCIÓN ENDPOINTS (Requiere autenticación) ==="

if [ -n "$PRODUCT_ID" ]; then
    echo -n "3.1 Realizar Depósito (como USER): "
    RESPONSE=$(curl -s -w "%{http_code}" -u "$USER_AUTH" \
      -X POST "$BASE_URL/transaction" \
      -d "originId=$PRODUCT_ID&type=DEPOSIT&amount=100.0")
    
    HTTP_CODE="${RESPONSE: -3}"
    if [ "$HTTP_CODE" = "200" ]; then
        show_result 0
    else
        show_result 1
        echo "HTTP Code: $HTTP_CODE"
    fi

    echo -n "3.2 Realizar Retiro (como ADMIN): "
    RESPONSE=$(curl -s -w "%{http_code}" -u "$ADMIN_AUTH" \
      -X POST "$BASE_URL/transaction" \
      -d "originId=$PRODUCT_ID&type=WITHDRAWAL&amount=50.0")
    
    HTTP_CODE="${RESPONSE: -3}"
    if [ "$HTTP_CODE" = "200" ]; then
        show_result 0
    else
        show_result 1
        echo "HTTP Code: $HTTP_CODE"
    fi
fi

echo "=== 4. TESTS DE AUTORIZACIÓN ==="

echo -n "4.1 Acceso sin autenticación (debe fallar): "
RESPONSE=$(curl -s -w "%{http_code}" \
  -X GET "$BASE_URL/client/00000000-0000-0000-0000-000000000000")

HTTP_CODE="${RESPONSE: -3}"
if [ "$HTTP_CODE" = "401" ]; then
    show_result 0
else
    show_result 1
    echo "Expected 401, got: $HTTP_CODE"
fi

echo -n "4.2 USER intentando crear cliente (debe fallar): "
RESPONSE=$(curl -s -w "%{http_code}" -u "$USER_AUTH" \
  -X POST "$BASE_URL/client" \
  -H "Content-Type: application/json" \
  -d '{"names": "Test", "surname": "User", "email": "test@test.com"}')

HTTP_CODE="${RESPONSE: -3}"
if [ "$HTTP_CODE" = "403" ]; then
    show_result 0
else
    show_result 1
    echo "Expected 403, got: $HTTP_CODE"
fi

echo "=== 5. CLEANUP (Opcional) ==="

if [ -n "$PRODUCT_ID" ]; then
    echo -n "5.1 Cancelar Cuenta: "
    RESPONSE=$(curl -s -w "%{http_code}" -u "$ADMIN_AUTH" \
      -X PUT "$BASE_URL/product/cancel/$PRODUCT_ID")
    
    HTTP_CODE="${RESPONSE: -3}"
    if [ "$HTTP_CODE" = "200" ]; then
        show_result 0
    else
        show_result 1
        echo "HTTP Code: $HTTP_CODE"
    fi
fi

if [ -n "$CLIENT_ID" ]; then
    echo -n "5.2 Eliminar Cliente: "
    RESPONSE=$(curl -s -w "%{http_code}" -u "$ADMIN_AUTH" \
      -X DELETE "$BASE_URL/client/$CLIENT_ID")
    
    HTTP_CODE="${RESPONSE: -3}"
    if [ "$HTTP_CODE" = "204" ]; then
        show_result 0
    else
        show_result 1
        echo "HTTP Code: $HTTP_CODE"
    fi
fi

echo "=== SMOKE TEST COMPLETADO ==="
echo ""
echo "Notas:"
echo "- Asegúrate de que la aplicación esté ejecutándose en $BASE_URL"
echo "- Los usuarios configurados son: admin/admin (ADMIN) y user/password (USER)"
echo "- Algunos tests pueden fallar si hay problemas de base de datos o lógica de negocio"
echo "- Los IDs generados se usan en tests subsecuentes"