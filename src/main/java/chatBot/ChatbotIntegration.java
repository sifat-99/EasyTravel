package chatBot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;

public class ChatbotIntegration {

    private static final String API_URL = "http://localhost:11434/api/generate";

    public static String generateCompletion(String model, String prompt, String suffix, List<String> images, String format, String options, String system, String template, boolean stream, boolean raw, String keepAlive, String context) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            StringBuilder jsonInputString = new StringBuilder();
            jsonInputString.append("{");
            jsonInputString.append("\"model\": \"").append(model).append("\",");
            jsonInputString.append("\"prompt\": \"").append(prompt).append("\",");
            if (suffix != null) jsonInputString.append("\"suffix\": \"").append(suffix).append("\",");
            if (images != null && !images.isEmpty()) {
                jsonInputString.append("\"images\": [");
                for (String image : images) {
                    jsonInputString.append("\"").append(Base64.getEncoder().encodeToString(image.getBytes())).append("\",");
                }
                jsonInputString.deleteCharAt(jsonInputString.length() - 1); // Remove trailing comma
                jsonInputString.append("],");
            }
            if (format != null) jsonInputString.append("\"format\": \"").append(format).append("\",");
            if (options != null) jsonInputString.append("\"options\": \"").append(options).append("\",");
            if (system != null) jsonInputString.append("\"system\": \"").append(system).append("\",");
            if (template != null) jsonInputString.append("\"template\": \"").append(template).append("\",");
            jsonInputString.append("\"stream\": ").append(stream).append(",");
            jsonInputString.append("\"raw\": ").append(raw).append(",");
            if (keepAlive != null) jsonInputString.append("\"keep_alive\": \"").append(keepAlive).append("\",");
            if (context != null) jsonInputString.append("\"context\": \"").append(context).append("\",");
            jsonInputString.deleteCharAt(jsonInputString.length() - 1); // Remove trailing comma
            jsonInputString.append("}");

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString();
                }
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    System.err.println("HTTP error code: " + responseCode);
                    System.err.println("Error response: " + errorResponse.toString());
                }
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static void main(String[] args) {
//        String model = "llama3.2:1b"; // Ensure this model is available on the server
//        String prompt = "what is travel? in 10 word";
//        String response = generateCompletion(model, prompt, null, null, null, null, null, null, false, false, null, null);
//        //System.out.println("Response: " + response);
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            ChatbotResponse chatbotResponse = objectMapper.readValue(response, ChatbotResponse.class);
//            System.out.println("Response: " + chatbotResponse.getResponse());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}