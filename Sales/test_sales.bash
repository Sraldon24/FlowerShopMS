#!/usr/bin/env bash

# Host and Port Settings
: ${HOST=localhost}
: ${PORT=8084}

echo "🚀 Testing Sales Microservice at http://$HOST:$PORT/api/v1/purchases"

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

# ================= GET all purchases =================

echo "📋 Sending request to list all purchases..."
sleep 1

assertCurl 200 "curl -s http://$HOST:$PORT/api/v1/purchases"

# Extracting one purchase ID
purchaseCount=$(echo "$RESPONSE" | jq 'length')
echo "📦 Number of purchases returned: $purchaseCount"

if (( purchaseCount > 0 )); then
  purchaseId=$(echo "$RESPONSE" | jq -r '.[0].purchaseOrderId')
  echo "🆔 Using purchase ID for DELETE test: $purchaseId"
else
  echo "⚠️ No purchases available to test DELETE."
  exit 1
fi

# ================= DELETE a purchase =================

sleep 1
echo "🗑️ Sending DELETE request for purchase ID: $purchaseId"
assertCurl 200 "curl -s -X DELETE http://$HOST:$PORT/api/v1/purchases/$purchaseId"

# ================= Verify Deletion =================

sleep 1
echo "🔍 Verifying purchase deletion with GET..."
assertCurl 404 "curl -s http://$HOST:$PORT/api/v1/purchases/$purchaseId"

echo "🎉 All Sales microservice tests passed successfully!"
