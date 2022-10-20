package video.sendvideo;


import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

public class ClientRe {
    static byte[] buf = new byte[60000];
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        MulticastSocket multicastSocket = new MulticastSocket(8080);
        InetAddress group = InetAddress.getByName("224.0.0.251");
        multicastSocket.joinGroup(group);
        JFrame jFrame = new JFrame();
        jFrame.setVisible(true);
        jFrame.setSize(300,150);
        JLabel jLabel = new JLabel();
        jFrame.add(jLabel);
        jLabel.setVisible(true);
        DatagramPacket rcvdp = new DatagramPacket(buf, buf.length);
        Timer timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    System.out.println("recive video....");
                    multicastSocket.receive(rcvdp);
                    ByteArrayInputStream bais = new ByteArrayInputStream(rcvdp.getData());
                    System.out.println(rcvdp.getData().length);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    ImageIcon imageIcon1 = (ImageIcon) ois.readObject();
                    Image scaleImage = imageIcon1.getImage().getScaledInstance(300, 150,Image.SCALE_DEFAULT);
                    ImageIcon imageIcon2 = new ImageIcon(scaleImage);
                    jLabel.setIcon(imageIcon2);
                }
                catch (Exception exception){
                    System.out.println("ssss");
                    System.out.println(exception);
                }
            }
        });
        timer.start();
    }
}