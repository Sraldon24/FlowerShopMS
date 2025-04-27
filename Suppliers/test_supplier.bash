#!/usr/bin/env bash

# Host and Port Settings
: ${HOST=localhost}
: ${PORT=8081}

echo "🚀 Testing: GET all suppliers at http://$HOST:$PORT/api/v1/suppliers"

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

# ================= Actual Test =================

echo "📋 Sending request to list all suppliers..."

# Add delay in case server needs a second to boot
sleep 1

assertCurl 200 "curl -s http://$HOST:$PORT/api/v1/suppliers"

# ================= Count Suppliers =================

supplierCount=$(echo "$RESPONSE" | jq '._embedded.supplierResponseModelList | length')
echo "📦 Number of suppliers returned: $supplierCount"

echo "✅ GET all suppliers test passed successfully!"
