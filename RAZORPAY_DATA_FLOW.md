# Razorpay Payment Data Flow - Detailed Analysis

This document explains **exactly what data is sent to Razorpay servers** during payment processing.

---

## Payment Flow Overview

```
User → Android App → Backend Server → Razorpay API → Payment Gateway → Backend Webhook
```

---

## Step 1: User Initiates Payment (Android App → Backend)

**Endpoint:** `POST /initiate_gw_payment`

### Data Sent FROM Android App:

```java
// File: SM/app/src/main/java/com/tripleseven/android/RazorpayPaymentActivity.java
// Lines 125-131

params.put("amount", amount);              // e.g., "500" (INR)
params.put("mobile", userMobile);          // e.g., "9876543210"
params.put("session", sessionToken);       // User session token
```

**Example Request:**
```
POST https://samrat-satta.com/initiate_gw_payment
Content-Type: application/x-www-form-urlencoded

amount=500
mobile=9876543210
session=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...
```

---

## Step 2: Backend Creates Razorpay Order (Backend → Razorpay)

**File:** `Matka/service/RazorpayService.py`

### Data Sent TO Razorpay API:

```python
# Lines 62-74

order_data = {
    "amount": 50000,                    # Amount in PAISE (500 INR = 50000 paise)
    "currency": "INR",                  # Always INR
    "receipt": "samrat_12345",          # Internal transaction ID
    "notes": {
        "transaction_id": "12345",      # Internal transaction ID
        "user_id": "789",               # Internal user ID
        "type": "deposit",              # Transaction type
        "project": "samrat",            # Project identifier
        "source": "samrat-satta.com"    # Source website
    }
}
```

**Actual API Call:**
```python
# Razorpay SDK internally makes this call:
POST https://api.razorpay.com/v1/orders
Authorization: Basic <base64(key_id:key_secret)>
Content-Type: application/json

{
    "amount": 50000,
    "currency": "INR",
    "receipt": "samrat_12345",
    "notes": {
        "transaction_id": "12345",
        "user_id": "789",
        "type": "deposit",
        "project": "samrat",
        "source": "samrat-satta.com"
    }
}
```

### Razorpay Response:

```json
{
    "id": "order_NZxBwZZ1234567",
    "entity": "order",
    "amount": 50000,
    "amount_paid": 0,
    "amount_due": 50000,
    "currency": "INR",
    "receipt": "samrat_12345",
    "status": "created",
    "attempts": 0,
    "notes": {
        "transaction_id": "12345",
        "user_id": "789",
        "type": "deposit",
        "project": "samrat",
        "source": "samrat-satta.com"
    },
    "created_at": 1717689600
}
```

---

## Step 3: Backend Returns Order Details (Backend → Android App)

**File:** `Matka/api/wallet_razorpay.py` (Lines 74-86)

### Data Sent BACK to Android App:

```json
{
    "success": "1",
    "data": {
        "order_id": "order_NZxBwZZ1234567",
        "amount": 50000,
        "currency": "INR",
        "key_id": "rzp_live_XXXXXXXXXXXXX",
        "transaction_id": 12345,
        "user_name": "John Doe",
        "user_email": "user@samrat777.com",
        "user_phone": "9876543210"
    }
}
```

---

## Step 4: Android App Opens Razorpay Checkout (Android → Razorpay SDK)

**File:** `SM/app/src/main/java/com/tripleseven/android/RazorpayPaymentActivity.java`

### Data Sent TO Razorpay SDK:

```java
// Lines 148-168

JSONObject options = new JSONObject();

// Required fields
options.put("name", "Samrat 777");                       // Merchant name
options.put("description", "Add Money to Wallet");       // Transaction description
options.put("order_id", "order_NZxBwZZ1234567");        // Order ID from backend
options.put("currency", "INR");                          // Currency
options.put("amount", 50000);                            // Amount in paise

// Optional: Prefill user details
JSONObject prefill = new JSONObject();
prefill.put("contact", "9876543210");                    // User's mobile number
options.put("prefill", prefill);

// Optional: Theme customization
JSONObject theme = new JSONObject();
theme.put("color", "#667eea");                           // Brand color
options.put("theme", theme);

checkout.open(this, options);
```

### What Razorpay SDK Does Internally:

The Razorpay Android SDK sends this data to **Razorpay's servers**:

```
POST https://api.razorpay.com/v1/checkout/preferences
Authorization: Basic <key_id>
Content-Type: application/json

{
    "key": "rzp_live_XXXXXXXXXXXXX",
    "order_id": "order_NZxBwZZ1234567",
    "amount": 50000,
    "currency": "INR",
    "name": "Samrat 777",
    "description": "Add Money to Wallet",
    "prefill": {
        "contact": "9876543210"
    },
    "theme": {
        "color": "#667eea"
    }
}
```

---

## Step 5: User Completes Payment (UPI/Card/etc.)

When the user selects a payment method and completes payment:

### Data Razorpay Collects:

1. **Payment Method:** UPI / Card / NetBanking / Wallet
2. **UPI ID** (if UPI): user@bank
3. **Card Details** (if Card): Encrypted card number, CVV, expiry
4. **Banking Details:** Razorpay handles this, NOT your app
5. **Device Info:** IP address, device fingerprint (for fraud detection)

⚠️ **IMPORTANT:** Your app does **NOT** see or store any payment credentials. Razorpay handles this securely.

---

## Step 6: Payment Success (Razorpay SDK → Android App)

After successful payment, Razorpay SDK returns:

```java
// Callback: onPaymentSuccess()
razorpayPaymentId = "pay_NZxCabc1234567"

// User is redirected to HomeScreen (no verification done in client!)
```

⚠️ **SECURITY CONCERN:** The app currently **DOES NOT verify** payment on the server side!

---

## Step 7: Webhook Notification (Razorpay → Backend)

**Endpoint:** `POST /razorpay_webhook`

### Data Sent FROM Razorpay:

```json
Headers:
  X-Razorpay-Signature: <HMAC-SHA256 signature>

Body:
{
    "entity": "event",
    "account_id": "acc_xxxxxxxxxxxxx",
    "event": "payment.captured",
    "contains": ["payment"],
    "payload": {
        "payment": {
            "entity": {
                "id": "pay_NZxCabc1234567",
                "entity": "payment",
                "amount": 50000,
                "currency": "INR",
                "status": "captured",
                "order_id": "order_NZxBwZZ1234567",
                "invoice_id": null,
                "international": false,
                "method": "upi",
                "amount_refunded": 0,
                "refund_status": null,
                "captured": true,
                "description": "Add Money to Wallet",
                "card_id": null,
                "bank": null,
                "wallet": null,
                "vpa": "user@oksbi",
                "email": "user@samrat777.com",
                "contact": "+919876543210",
                "notes": {
                    "transaction_id": "12345",
                    "user_id": "789",
                    "type": "deposit",
                    "project": "samrat",
                    "source": "samrat-satta.com"
                },
                "fee": 1180,
                "tax": 180,
                "error_code": null,
                "error_description": null,
                "error_source": null,
                "error_step": null,
                "error_reason": null,
                "acquirer_data": {
                    "rrn": "123456789012"
                },
                "created_at": 1717689700
            }
        }
    },
    "created_at": 1717689701
}
```

---

## Summary: What Data Goes to Razorpay

### ✅ Data You Send to Razorpay:

1. **Amount** (in paise)
2. **Currency** (INR)
3. **Receipt ID** (internal transaction reference)
4. **Notes** (custom metadata):
   - Internal transaction ID
   - Internal user ID
   - Transaction type
   - Project name
   - Source website
5. **Merchant Details**:
   - Merchant name ("Samrat 777")
   - Description ("Add Money to Wallet")
6. **User Info** (prefill only):
   - Mobile number (for convenience)

### ❌ Data You DON'T Send:

- User passwords
- Bank account numbers
- Card details
- UPI PINs
- Any other sensitive data

### 🔐 Data Razorpay Collects Directly:

When the user makes payment, Razorpay **directly** collects:
- Payment method (UPI/Card/etc.)
- UPI ID or Card details (encrypted)
- Device fingerprint
- IP address

---

## Security Notes

1. **API Keys:**
   - `key_id` (public): Safe to expose in frontend
   - `key_secret` (private): NEVER exposed, only used server-side

2. **Webhook Signature:**
   - Every webhook is signed with HMAC-SHA256
   - Backend verifies signature to prevent fake payments

3. **PCI DSS Compliance:**
   - Razorpay is PCI DSS compliant
   - Your app never touches card data

---

**Last Updated:** June 7, 2026
