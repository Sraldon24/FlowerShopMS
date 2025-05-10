#!/usr/bin/env bash

: ${HOST=localhost}
: ${PORT=8080}

echo "🚀 Testing Inventory Microservice via API Gateway at http://$HOST:$PORT/api/v1/inventories"

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

echo "📋 Sending request to list all inventories..."
sleep 1

assertCurl 200 "curl -s http://$HOST:$PORT/api/v1/inventories"

inventoryCount=$(echo "$RESPONSE" | jq 'length')
echo "📦 Number of inventories returned: $inventoryCount"

echo "✅ GET all inventories test passed successfully!"

inventoryId="inv-2003"
sleep 1

echo "🗑️ Deleting Inventory ID: $inventoryId"
assertCurl 200 "curl -X DELETE http://$HOST:$PORT/api/v1/inventories/$inventoryId -s -o /dev/null"

echo "✅ DELETE inventory by ID test passed successfully!"
