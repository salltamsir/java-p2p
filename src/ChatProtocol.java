public interface ChatProtocol {

    default void sendPrivateMessage(String from, String to, String msg){
    }
}