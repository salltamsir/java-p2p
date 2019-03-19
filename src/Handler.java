import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class Handler {

    TreeMap<String,SocketChannel> clientList;
    TreeMap<String, ArrayList<SocketChannel>> salonList;
    ChatModel chatmodel;

    public Handler(TreeMap<String,SocketChannel> clientList, TreeMap<String, ArrayList<SocketChannel>> salonList, ChatModel chatModel){
        this.clientList=clientList;
        this.salonList=salonList;
        this.chatmodel=chatModel;
    }


    public void input(String [] words, SocketChannel socketChannel, ByteBuffer byteBuffer) throws IOException,IndexOutOfBoundsException {
        String dest;
        switch (words[0].toUpperCase()){
            case "LOG" :
                dest = assemblage(words,1);
                if(clientList.get(dest)!=null){
                    System.out.println("Nom existe deja");
                    return ;
                }
                clientList.put(assemblage(words,1),socketChannel);
                break;
            case "MSG" :
                if(socketChannel==null){
                    System.out.println("Client nexiste pas");
                    return ;
                }
                chatmodel.sendPrivateMessage(clientList.get(words[1]),byteBuffer);
                break;

            case "CREATE" :
                switch (words[1].toUpperCase()){
                    case "ROOM":
                        if(salonList.get(words[2])==null){
                            System.out.println("Ce salon existe deja");
                            return ;
                        }
                        chatmodel.createRoom(salonList,socketChannel,words[2]);
                        break;
                }
                break;

            case "ENTER" :
                switch (words[1].toUpperCase()){
                    case "ROOM":
                        chatmodel.enterRoom(salonList,socketChannel,words[2]);
                        break;
                }
                break;

            case "LEAVE" :
                switch (words[1].toUpperCase()){
                    case  "ROOM":
                        chatmodel.leaveRoom(salonList,socketChannel,words[2]);
                        System.out.println(" Left the room");
                }
                break;
            case "MSGROOM" :
                chatmodel.sendRoomMessage(words[1],byteBuffer,salonList);
                break;

        }
    }




    public static  String[] decoupage (String texte){
        String[] words = texte.split("\\s+");
        for (int i = 0; i < words.length; i++) {

            // You may want to check for a non-word character before blindly
            // performing a replacement
            // It may also be necessary to adjust the character class
            words[i] = words[i].replaceAll("[^\\w]", "");
        }
        return words;
    }

    public String assemblage (String [] words, int debut) {
        String texte= "";
        for (int i= debut; i<words.length;++i){
            texte+=words[i];
        }
        return texte;
    }
}