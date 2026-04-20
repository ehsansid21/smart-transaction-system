package transaction.example.transmansys.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class ExchangeRateService {

    private static final String API_URL = "https://open.er-api.com/v6/latest/INR";
    private final RestTemplate restTemplate = new RestTemplate();

    @SuppressWarnings("unchecked")
    public Map<String, Object> getRates() {
        try {
            Map<String, Object> response = restTemplate.getForObject(API_URL, Map.class);
            if (response != null && response.containsKey("rates")) {
                return (Map<String, Object>) response.get("rates");
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch exchange rates: " + e.getMessage());
        }
        return null;
    }

    public Double convertToINR(Double amount, String fromCurrency) {
        if (fromCurrency == null || "INR".equalsIgnoreCase(fromCurrency)) {
            return amount;
        }
        
        Map<String, Object> rates = getRates();
        if (rates != null && rates.containsKey(fromCurrency.toUpperCase())) {
            Double rate = Double.valueOf(rates.get(fromCurrency.toUpperCase()).toString());
            // The API returns rates with base INR (e.g., 1 INR = 0.012 USD)
            // To convert USD to INR, divide by the rate.
            return amount / rate;
        }
        throw new RuntimeException("Currency conversion failed or currency not supported: " + fromCurrency);
    }
}
