//Les imports habituels

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;

public class Fenetre extends JFrame {
    Peer peer;
    private JPanel container = new JPanel();
    private JTextField jTextField = new JTextField();
    private JTextField jTextField1 = new JTextField();
    private JTextField jTextField2 = new JTextField();
    private JTextField jTextField3 = new JTextField();
    private JLabel label = new JLabel("Un JTextField");
    private JButton b = new JButton ("Thread Listen");
    private JButton b1 = new JButton ("Thread connect");

    public Fenetre(){
        System.out.println("fenetre va souvrir");
        this.setTitle("Animation");
        this.setSize(300, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        container.setBackground(Color.white);
        container.setLayout(new BorderLayout());
        JPanel top = new JPanel();
        Font police = new Font("Arial", Font.BOLD, 14);
        jTextField.setFont(police);
        jTextField.setPreferredSize(new Dimension(150,30));
        jTextField1.setFont(police);
        jTextField1.setPreferredSize(new Dimension(150,30));
        jTextField2.setFont(police);
        jTextField2.setPreferredSize(new Dimension(150,30));
        jTextField3.setFont(police);
        jTextField3.setPreferredSize(new Dimension(150,30));
        jTextField3.setPreferredSize(new Dimension(150,30));
        b.addActionListener(new BoutonListener());
        top.add(label);
        top.add(jTextField);
        top.add(jTextField1);
        top.add(jTextField2);
        top.add(jTextField3);
        top.add(b);
        top.add(b1);
        b1.addActionListener(e -> {
            if (!(peer.equals(null))){
                try {
                    peer.connectToPeer(jTextField3.getText(), Integer.valueOf(jTextField2.getText()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        this.setContentPane(top);
        this.setVisible(true);
    }

    class BoutonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int port = Integer.valueOf(jTextField.getText());
            String ip = jTextField1.getText();
            System.out.println("Port "+port);
            System.out.println("Ip : "+ip);

            try {
                 peer = new Peer (port,ip);
                new ListeningThread(peer).start();
            } catch (IOException e1) {
                e1.printStackTrace();
            }


        }
    }
}