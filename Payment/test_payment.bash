#!/usr/bin/env bash

# Host and Port Settings (API Gateway)
: ${HOST=localhost}
: ${PORT=8080}

echo "🚀 Testing Payment Microservice via API Gateway at http://$HOST:$PORT/api/v1/payments"

# ================= Helper Function =================

function assertCurl() {
  local expectedHttpCode=$1
  local curlCmd="$2 -w \"%{http_code}\""
  local result=$(eval $curlCmd)
  local httpCode="${result:(-3)}"
  RESPONSE='' && (( ${#result} > 3 )) && RESPONSE="${result%???}"

  if [ "$httpCode" = "$expectedHttpCode" ]; then
    echo "✅ Test OK (HTTP Code: $httpCode)"
  else
    echo "❌ Test FAILED, Expected HTTP Code: $expectedHttpCode, Got: $httpCode"
    echo "- Command: $curlCmd"
    echo "- Response: $RESPONSE"
    exit 1
  fi
}

# ================= GET ALL PAYMENTS =================

echo "📋 Sending request to list all payments..."
sleep 1

assertCurl 200 "curl -s http://$HOST:$PORT/api/v1/payments"

paymentCount=$(echo "$RESPONSE" | jq 'length')
echo "📦 Number of payments returned: $paymentCount"

echo "✅ GET all payments test passed successfully!"

# ================= DELETE PAYMENT BY ID =================

paymentId="PAY-DEF456"

sleep 1

echo "🗑️ Deleting Payment ID: $paymentId"
assertCurl 200 "curl -X DELETE http://$HOST:$PORT/api/v1/payments/$paymentId"

echo "✅ DELETE payment by ID test passed successfully!"
