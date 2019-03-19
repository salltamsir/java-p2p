import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Peer {
    private static final String POISON_PILL = "exit";
    Selector selector ;
    ServerSocketChannel serverSocket ;
    ByteBuffer buffer;
    ByteBuffer clientBuffer;
    Set<SocketChannel> set;
    TreeMap<String,SocketChannel> clientList;
    TreeMap<String,ArrayList<SocketChannel>> salonList;
    TreeMap<String,Peer> peerList;
    Handler handler;
    ChatModel chatModel;
    private int port;
    private String  ip;

    public Peer (int port, String ip) throws IOException {
        this.ip=ip;
        this.port=port;
        buffer = ByteBuffer.allocate(256);
        clientBuffer = ByteBuffer.allocate(256);
        set = new HashSet<>();
        clientList = new TreeMap<>();
        salonList = new TreeMap<>();
        handler = new Handler(clientList,salonList,new ChatModel(new ChatOutput()));

        initPeerListening();

    }
    public void connectToPeer (String ip, int port) throws IOException {
        this.clientBuffer = ByteBuffer.allocate(512);
        SocketChannel socketChannel  = SocketChannel.open();
        clientList.put(ip,socketChannel);
        socketChannel.connect(new InetSocketAddress(ip, port));

        new Thread(()->{
            while (true){
                try {
                    if ((socketChannel.read(clientBuffer)>0))
                        System.out.println("receive");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                clientBuffer.flip();
            }
        });

    }

    public void initPeerListening () throws IOException {
        selector = Selector.open();serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(ip, port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    }


    public void accept () throws IOException {
        while (true) {
            int test = selector.select();
            System.out.println(test+" clients");
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            int  i =  1;
            while (iter.hasNext()) {
                System.out.println("Numero "+(i++));
                SelectionKey key = iter.next();

                if (key.isAcceptable()) {
                    register(selector, serverSocket);
                }

                if (key.isReadable()) {
                    repeat( key);
                }
                iter.remove();
            }
        }
    }

    private  void repeat(SelectionKey key) throws IOException {

        SocketChannel client = (SocketChannel) key.channel();
        int n = client.read(buffer);
        if (n<0){
            key.cancel();
            client.close();
            System.out.println("Deconnexion");
            return;
        }
        client.read(buffer);
        buffer.flip();
        String action = new String(buffer.array()).trim();

        handler.input(Handler.decoupage (action),client,buffer);

        buffer.clear();
        if (new String(buffer.array()).trim().equals(POISON_PILL)) {
            client.close();
            System.out.println("Not accepting client messages anymore");
        }
    }

    private  void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {

        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        set.add(client);

    }

    private  void register(Selector selector, ServerSocketChannel serverSocket, String name)
            throws IOException {

        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        clientList.put(name,client);

    }



}