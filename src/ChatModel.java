import sun.rmi.runtime.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;

public class ChatModel {
    private ChatOutput chatOutput;

    public ChatModel(ChatOutput chatOutput){
        this.chatOutput = chatOutput;
    }




    public  void sendPrivateMessage(SocketChannel socketChannel, ByteBuffer byteBuffer) throws IOException {
        chatOutput.sendPrivateMessage(socketChannel,byteBuffer);
    }
    public void sendRoomMessage(String name, ByteBuffer byteBuffer, TreeMap<String, ArrayList<SocketChannel>> salonList) throws IOException {
        chatOutput.sendRoomMessage(name,byteBuffer,salonList);
    }
    public void enterRoom ( TreeMap<String, ArrayList<SocketChannel>> salonList, SocketChannel socketChannel, String name){
        ArrayList<SocketChannel> salon = salonList.get(name);
        if(salon==null){
            System.out.println("Room doesn't exist");
        }
        else{
            salon.add(socketChannel);
        }
    }
    public void createRoom(TreeMap<String, ArrayList<SocketChannel>> salonList, SocketChannel socketChannel, String name){
        ArrayList<SocketChannel> salon = salonList.get(name);
        if(salon==null){
            salonList.put(name,new ArrayList<SocketChannel>());
            salonList.get(name).add(socketChannel);
        }
        else{
            salon.add(socketChannel);
        }
    }

    public void leaveRoom(TreeMap<String, ArrayList<SocketChannel>> salonList, SocketChannel socketChannel, String name){
        ArrayList<SocketChannel> salon = salonList.get(name);
        if(salon==null){
            System.out.println("Room doesn't exist");
        }
        else{
            salon.remove(socketChannel);
            if(salon.size()==0){
                salonList.remove(name);
                System.out.println("Salon supprimé");
            }
        }
    }
}