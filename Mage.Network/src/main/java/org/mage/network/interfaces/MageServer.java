package org.mage.network.interfaces;

import java.util.List;
import java.util.UUID;
import mage.interfaces.ServerState;
import mage.remote.DisconnectReason;
import mage.utils.MageVersion;
import mage.view.RoomView;

/**
 *
 * @author BetaSteward
 */
public interface MageServer {
    
    boolean registerClient(String userName, String sessionId, MageVersion version);
    void disconnect(String sessionId, DisconnectReason reason);

    void receiveChatMessage(UUID chatId, String sessionId, String message);
    void joinChat(UUID chatId, String sessionId);
    void leaveChat(UUID chatId, String sessionId);
    UUID getRoomChatId(UUID roomId);
    void receiveBroadcastMessage(String message, String sessionId);
    
    ServerState getServerState();

    List<String> getServerMessages();
    RoomView getRoom(UUID roomId);

    void pingTime(long milliSeconds, String sessionId);
    
}