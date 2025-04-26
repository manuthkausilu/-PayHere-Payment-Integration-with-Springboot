package com.example.payhere_test.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;

import java.text.DecimalFormat;
import java.util.Map;

import static com.example.payhere_test.util.PayHereHashUtil.getMd5;

@CrossOrigin
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${payhere.merchant.id}")
    private String merchantId;

    @Value("${payhere.merchant.secret}")
    private String merchantSecret;

    @PostMapping("/hash")
    public ResponseEntity<Map<String, String>> generateHash(@RequestBody Map<String, String> payload) {
        String orderId = payload.get("order_id");
        String amountStr = payload.get("amount");
        String currency = payload.get("currency");

        double amount = Double.parseDouble(amountStr);
        DecimalFormat df = new DecimalFormat("0.00");
        String amountFormatted = df.format(amount);

        String hash = getMd5(merchantId + orderId + amountFormatted + currency + getMd5(merchantSecret));
        System.out.println(hash);

        return ResponseEntity.ok(Map.of(
                "hash", hash,
                "merchant_id", merchantId
        ));
    }


    @PostMapping("/notify")
    public ResponseEntity<String> handleNotify(HttpServletRequest request) {
        try {
            String merchantId = request.getParameter("merchant_id");
            String orderId = request.getParameter("order_id");
            String payhereAmount = request.getParameter("payhere_amount");
            String payhereCurrency = request.getParameter("payhere_currency");
            String statusCode = request.getParameter("status_code");
            String md5sig = request.getParameter("md5sig");

            System.out.println("Received Notification: ");
            System.out.println("merchant_id: " + merchantId);
            System.out.println("order_id: " + orderId);
            System.out.println("status_code: " + statusCode);

            String localMd5sig = getMd5(merchantId + orderId + payhereAmount + payhereCurrency + statusCode + getMd5(merchantSecret));

            if (localMd5sig.equalsIgnoreCase(md5sig) && "2".equals(statusCode)) {
                System.out.println("Payment SUCCESS for Order: " + orderId);
                // TODO: Update your database here
                return ResponseEntity.ok("Payment successful");
            } else {
                System.out.println("Invalid payment notification or payment not successful");
                return ResponseEntity.status(400).body("Invalid notification");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error");
        }
    }



    @GetMapping("/return")
    public RedirectView handleReturn() {
        return new RedirectView("/payment-success.html");
    }

    @GetMapping("/cancel")
    public RedirectView handleCancel() {
        return new RedirectView("/payment-cancelled.html");
    }
}

