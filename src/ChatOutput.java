import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class ChatOutput implements ChatProtocol {




    public void sendPrivateMessage(SocketChannel socketChannel, ByteBuffer byteBuffer) throws IOException {
        socketChannel.write(byteBuffer);
    }
    public void sendRoomMessage(String name, ByteBuffer byteBuffer, TreeMap<String, ArrayList<SocketChannel>> salonList) throws IOException {
        ArrayList<SocketChannel> salon = salonList.get(name);
        if(salon==null){
            System.out.println("Room doesnt exist");
            return ;
        }

        salon.stream().forEach(x-> {
            try {
                x.write(byteBuffer);
                byteBuffer.flip();
                System.out.println("socket : "+x);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}