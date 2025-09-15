#!/bin/bash

# Quick Smoke Test - Client Management API
# Ejecuta tests básicos para verificar que la API está funcionando

BASE_URL="http://localhost:8080"

echo "🚀 Quick Smoke Test - Client Management API"
echo "Base URL: $BASE_URL"
echo ""

# Test 1: Health check básico (sin auth)
echo -n "1. Health Check (sin auth): "
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/client/test" 2>/dev/null)
if [ "$HTTP_CODE" = "401" ] || [ "$HTTP_CODE" = "403" ]; then
    echo "✅ API responde (requiere auth como esperado)"
else
    echo "⚠️  Respuesta inesperada: $HTTP_CODE"
fi

# Test 2: Crear cliente con admin
echo -n "2. Crear Cliente (admin): "
RESPONSE=$(curl -s -w "%{http_code}" -u "admin:admin" \
  -X POST "$BASE_URL/client" \
  -H "Content-Type: application/json" \
  -d '{
    "documentType": "CC",
    "numberId": 12345678,
    "names": "Test",
    "surname": "User",
    "email": "test@example.com",
    "birthDate": "1990-01-01"
  }' 2>/dev/null)

HTTP_CODE="${RESPONSE: -3}"
if [ "$HTTP_CODE" = "201" ]; then
    CLIENT_ID=$(echo "${RESPONSE%???}" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
    echo "✅ Cliente creado (ID: ${CLIENT_ID:0:8}...)"
else
    echo "❌ Error: $HTTP_CODE"
    exit 1
fi

# Test 3: Obtener cliente
echo -n "3. Obtener Cliente: "
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -u "admin:admin" \
  -X GET "$BASE_URL/client/$CLIENT_ID" 2>/dev/null)
if [ "$HTTP_CODE" = "200" ]; then
    echo "✅ Cliente obtenido"
else
    echo "❌ Error: $HTTP_CODE"
fi

# Test 4: Crear producto
echo -n "4. Crear Producto: "
RESPONSE=$(curl -s -w "%{http_code}" -u "admin:admin" \
  -X POST "$BASE_URL/product/checking" \
  -H "Content-Type: application/json" \
  -d '{
    "accountType": "CC",
    "accountNumber": "3312345678",
    "balance": 1000.0,
    "gmfExempt": false,
    "owner": {"id": "'$CLIENT_ID'"}
  }' 2>/dev/null)

HTTP_CODE="${RESPONSE: -3}"
if [ "$HTTP_CODE" = "201" ]; then
    PRODUCT_ID=$(echo "${RESPONSE%???}" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
    echo "✅ Producto creado (ID: ${PRODUCT_ID:0:8}...)"
else
    echo "❌ Error: $HTTP_CODE"
fi

# Test 5: Realizar transacción
if [ -n "$PRODUCT_ID" ]; then
    echo -n "5. Realizar Transacción: "
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -u "user:password" \
      -X POST "$BASE_URL/transaction" \
      -d "originId=$PRODUCT_ID&type=DEPOSIT&amount=100.0" 2>/dev/null)
    if [ "$HTTP_CODE" = "200" ]; then
        echo "✅ Transacción exitosa"
    else
        echo "❌ Error: $HTTP_CODE"
    fi
fi

# Test 6: Test de autorización
echo -n "6. Test Autorización (user->admin endpoint): "
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -u "user:password" \
  -X POST "$BASE_URL/client" \
  -H "Content-Type: application/json" \
  -d '{"names":"Test"}' 2>/dev/null)
if [ "$HTTP_CODE" = "403" ]; then
    echo "✅ Autorización funcionando"
else
    echo "⚠️  Respuesta inesperada: $HTTP_CODE"
fi

echo ""
echo "🎉 Quick test completado!"
echo ""
echo "Para tests más detallados ejecuta: ./smoke-test-curl.sh"
echo "Para comandos individuales revisa: curl-commands.md"