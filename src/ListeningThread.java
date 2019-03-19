import java.io.IOException;

public class ListeningThread extends Thread {

    private Peer peer;

    public ListeningThread (Peer peer){
        this.peer = peer;
    }

    @Override
    public void run (){
        try {
            peer.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
