#!/usr/bin/env bash

# Host and Port Settings
: ${HOST=localhost}
: ${PORT=8080}

echo "🚀 Testing Supplier Microservice at http://$HOST:$PORT/api/v1/suppliers"

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

# ================= Actual Tests =================

echo "📋 Sending request to list all suppliers..."
sleep 1

assertCurl 200 "curl -s http://$HOST:$PORT/api/v1/suppliers"

# ================= Count Suppliers =================

supplierCount=$(echo "$RESPONSE" | jq '._embedded.supplierResponseModelList | length')
echo "📦 Number of suppliers returned: $supplierCount"

echo "✅ GET all suppliers test passed successfully!"

# ================= Test GET Supplier by ID =================

supplierId="sup-5678"

sleep 1

echo "🔎 Testing GET for Supplier ID: $supplierId"
assertCurl 200 "curl -s http://$HOST:$PORT/api/v1/suppliers/$supplierId"

# ================= Test PUT (Update Existing Supplier) =================

sleep 1

echo "🛠️ Testing PUT (update existing supplier)..."

updatedSupplierJson='{
  "supplierId": "'"$supplierId"'",
  "companyName": "Mohamed Updated",
  "contactPerson": "Bob Bloom",
  "emailAddress": "bob@petalparadise.com",
  "streetAddress": "456 Rose Ave",
  "postalCode": "M2B 2B2",
  "city": "Toronto",
  "province": "Ontario",
  "phoneNumbers": [
    {
      "type": "WORK",
      "number": "416-234-5678"
    }
  ],
  "password1": "Mohamed",
  "password2": "Mohamed"
}'

RESPONSE=$(curl -s -X PUT http://$HOST:$PORT/api/v1/suppliers/$supplierId \
  -H "Content-Type: application/json" \
  -d "$updatedSupplierJson")

updatedCompanyName=$(echo "$RESPONSE" | jq -r '.companyName')

if [ "$updatedCompanyName" = "Mohamed Updated" ]; then
    echo "✅ PUT (update existing supplier) test passed!"
else
    echo "❌ PUT failed! Response: $RESPONSE"
    exit 1
fi

# ================= Verify Update with GET =================

sleep 1

echo "🔎 Verifying updated supplier with GET..."

assertCurl 200 "curl -s http://$HOST:$PORT/api/v1/suppliers/$supplierId"

verifiedCompanyName=$(echo "$RESPONSE" | jq -r '.companyName')

if [ "$verifiedCompanyName" = "Mohamed Updated" ]; then
    echo "✅ Verification after PUT succeeded! (companyName = Mohamed Updated)"
else
    echo "❌ Verification failed! Response: $RESPONSE"
    exit 1
fi

echo "🎉 All tests passed successfully!"
