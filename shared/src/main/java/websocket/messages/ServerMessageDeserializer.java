package websocket.messages;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ServerMessageDeserializer implements JsonDeserializer<ServerMessage> {

    @Override
    public ServerMessage deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        JsonObject object = json.getAsJsonObject();
        String serverMessageType = object.get("serverMessageType").getAsString();

        return switch(serverMessageType) {
            case "load" -> context.deserialize(json, LoadGameMessage.class);
            case "error" -> context.deserialize(json, ErrorMessage.class);
            case "notification" -> context.deserialize(json, NotificationMessage.class);
            default -> throw new JsonParseException("Error: unknown message type: " + type);
        };
    }
}
