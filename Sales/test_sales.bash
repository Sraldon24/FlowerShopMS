#!/usr/bin/env bash

: ${HOST=localhost}
: ${PORT=8080}
BASE_URL="http://$HOST:$PORT/api/v1/purchases"

echo "🚀 Testing Sales Microservice at $BASE_URL"

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

# ================= GET all =================
echo "📋 Listing all purchases..."
assertCurl 200 "curl -s $BASE_URL"
purchaseId=$(echo "$RESPONSE" | jq -r '.[0].purchaseOrderId // empty')
echo "🆔 First ID: $purchaseId"

# ================= POST =================
echo "📝 Creating a new purchase..."
payload='{
  "inventoryId": "inv-2001",
  "flowerIdentificationNumber": "flw-3001",
  "supplierId": "sup-1234",
  "employeeId": "emp-1001",
  "paymentId": "PAY-ABC123",
  "salePrice": 59.90,
  "currency": "CAD",
  "payment_currency": "CAD",
  "saleOfferDate": "2025-03-15",
  "salePurchaseStatus": "PENDING",
  "financingAgreementDetails": {
    "numberOfMonthlyPayments": 0,
    "monthlyPaymentAmount": 0.0,
    "downPaymentAmount": 0.0
  }
}'

assertCurl 200 "curl -s -X POST $BASE_URL -H \"Content-Type: application/json\" -d '$payload'"
newId=$(echo "$RESPONSE" | jq -r '.purchaseOrderId')
echo "🆕 Created purchaseOrderId: $newId"

# ================= GET by ID =================
echo "🔍 Retrieving purchase by ID: $newId"
assertCurl 200 "curl -s $BASE_URL/$newId"

# ================= PUT =================
echo "✏️ Updating the purchase $newId..."
updatedPayload=$(echo "$payload" | jq '.salePrice = 89.99')
assertCurl 200 "curl -s -X PUT $BASE_URL/$newId -H \"Content-Type: application/json\" -d '$updatedPayload'"

# ================= DELETE =================
echo "🗑️ Deleting purchase ID: $newId"
assertCurl 200 "curl -s -X DELETE $BASE_URL/$newId"

echo "✅ ALL TESTS PASSED"
