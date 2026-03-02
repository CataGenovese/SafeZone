#!/bin/bash

echo "🧪 Testing Register Endpoint"
echo "=============================="
echo ""

# URL del endpoint
URL="http://localhost:8090/api/auth/register"

# Datos de prueba
DATA='{
  "email": "test@usuario.com",
  "password": "password123"
}'

echo "📍 URL: $URL"
echo "📦 Data: $DATA"
echo ""
echo "📡 Enviando request..."
echo ""

# Hacer el request
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$URL" \
  -H "Content-Type: application/json" \
  -d "$DATA")

# Separar el body del código de estado
HTTP_BODY=$(echo "$RESPONSE" | sed '$d')
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)

echo "📊 HTTP Status Code: $HTTP_CODE"
echo ""
echo "📄 Response Body:"
echo "$HTTP_BODY" | jq '.' 2>/dev/null || echo "$HTTP_BODY"
echo ""

if [ "$HTTP_CODE" = "201" ]; then
    echo "✅ ¡Registro exitoso!"
elif [ "$HTTP_CODE" = "400" ]; then
    echo "⚠️  Error en los datos o email duplicado"
elif [ "$HTTP_CODE" = "401" ]; then
    echo "❌ Error 401 - Problema de autenticación (verifica SecurityConfig)"
elif [ "$HTTP_CODE" = "404" ]; then
    echo "❌ Error 404 - Endpoint no encontrado"
else
    echo "❌ Error $HTTP_CODE"
fi

echo ""
echo "=============================="

