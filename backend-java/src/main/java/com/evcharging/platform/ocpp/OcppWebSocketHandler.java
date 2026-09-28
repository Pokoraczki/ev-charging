package com.evcharging.platform.ocpp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OcppWebSocketHandler extends TextWebSocketHandler {

    // Aktív töltőoszlop kapcsolatok tárolása memóriában (ChargePointId -> WebSocket session)
    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String chargePointId = getChargePointId(session);
        sessions.put(chargePointId, session);
        System.out.println("[OCPP] Töltőoszlop csatlakozott: " + chargePointId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payloadStr = message.getPayload();
        String chargePointId = getChargePointId(session);
        System.out.println("[OCPP Bejövő üzenet от " + chargePointId + "]: " + payloadStr);

        // OCPP 1.6-J üzenetstruktúra: [2, "UniqueId", "Action", {Payload}]
        JsonNode jsonArray = objectMapper.readTree(payloadStr);
        if (jsonArray.isArray() && jsonArray.size() >= 3) {
            int messageTypeId = jsonArray.get(0).asInt();
            String uniqueId = jsonArray.get(1).asText();
            String action = jsonArray.get(2).asText();

            if (messageTypeId == 2) { // CALL üzenet a töltőtől
                handleOcppCall(session, uniqueId, action, jsonArray.get(3), chargePointId);
            }
        }
    }

    private void handleOcppCall(WebSocketSession session, String uniqueId, String action, JsonNode payload, String chargePointId) throws IOException {
        String responsePayload = "{}";

        switch (action) {
            case "BootNotification":
                // A töltő jelentkezik be
                System.out.println("[OCPP] BootNotification érkezett innen: " + chargePointId);
                responsePayload = "{\"status\":\"Accepted\",\"currentTime\":\"" + java.time.Instant.now().toString() + "\",\"interval\":300}";
                break;

            case "StatusNotification":
                // A töltő státúsváltása (pl. Available, Charging, Faulted)
                String status = payload.path("status").asText();
                System.out.println("[OCPP] StatusNotification: Oszlop " + chargePointId + " státusza -> " + status);
                responsePayload = "{}";
                break;

            case "StartTransaction":
                // Töltés indítási kérés a fizikai oszloptól
                System.out.println("[OCPP] StartTransaction érkezett a töltőtől.");
                // Itt kapcsolódik majd az Atomic State Check logika!
                responsePayload = "{\"transactionId\": 12345, \"idTagInfo\": {\"status\": \"Accepted\"}}";
                break;

            case "StopTransaction":
                System.out.println("[OCPP] StopTransaction érkezett a töltőtől.");
                responsePayload = "{\"idTagInfo\": {\"status\": \"Accepted\"}}";
                break;

            default:
                System.out.println("[OCPP] Ismeretlen akció: " + action);
        }

        // Válasz küldése a töltőnek OCPP CALLRESULT formátumban: [3, "UniqueId", {Payload}]
        String responseMessage = "[3, \"" + uniqueId + "\", " + responsePayload + "]";
        session.sendMessage(new TextMessage(responseMessage));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String chargePointId = getChargePointId(session);
        sessions.remove(chargePointId);
        System.out.println("[OCPP] Töltőoszlop levált: " + chargePointId);
    }

    private String getChargePointId(WebSocketSession session) {
        String path = session.getUri().getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }

    // Külső hívás a szerverről a töltő felé (pl. távoli indítás)
    public static void sendRemoteCommand(String chargePointId, String action, String payloadJson) throws IOException {
        WebSocketSession session = sessions.get(chargePointId);
        if (session != null && session.isOpen()) {
            // [2, "ServerUniqueId", "Action", {Payload}]
            String message = "[2, \"" + java.util.UUID.randomUUID().toString() + "\", \"" + action + "\", " + payloadJson + "]";
            session.sendMessage(new TextMessage(message));
        }
    }
}