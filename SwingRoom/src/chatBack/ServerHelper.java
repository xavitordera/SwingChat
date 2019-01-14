package chatBack;

import java.util.concurrent.ConcurrentHashMap;
import chatBack.MySocket;
import java.util.Collection;


public class ServerHelper {
    private static ConcurrentHashMap<String, MySocket> clients;

    private ServerHelper() {
        clients = new ConcurrentHashMap<>();
    }

    public static ServerHelper getInstance() {
        return new ServerHelper();
    }
       
    
    public static boolean isNickUsed(String nick) {
        for (String clientNick : clients.keySet()) {
            if (nick.equals(clientNick)) {
                return true;
            }
        }
        return false;
    }

    protected void addNewUser(String nick, MySocket socket) {
        clients.put(nick, socket);
    }

    protected Collection<MySocket> getClientSocketsCollection() {
        return clients.values();
    }
}