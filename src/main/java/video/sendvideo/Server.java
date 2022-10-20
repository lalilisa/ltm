package video.sendvideo;


import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

public class Server {
    static int imagenb = 0;
    static MulticastSocket multicastSocket;
    static SocketAddress socketAddress;
    public static void main(String[] args) throws Exception {
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(176,144));
        webcam.open();
        socketAddress=new InetSocketAddress(InetAddress.getByName("192.168.1.29"),8080);
        multicastSocket=new MulticastSocket(8080);
        InetAddress group = InetAddress.getByName("224.0.0.251");
        multicastSocket.joinGroup(group);
        System.out.println("waiting......");
        BufferedImage image =webcam.getImage();
        ImageIcon imageIcon=new ImageIcon(image);
        JFrame jFrame=new JFrame("Webcam");
        jFrame.setSize(500,500);
        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);
        jFrame.add(panel);
        jFrame.setVisible(true);
        JLabel jLabel=new JLabel();
        jLabel.setVisible(true);
        jFrame.add(jLabel);
        Timer timer=new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {


            BufferedImage     bufferedImage=webcam.getImage();
            ImageIcon icon = new ImageIcon(bufferedImage);
            jLabel.setIcon(icon);
            Image scaleImage = icon.getImage().getScaledInstance(120, 120,Image.SCALE_DEFAULT);
            ImageIcon sendIcon=new ImageIcon(scaleImage);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(sendIcon);
            oos.flush();
            byte[] buf = baos.toByteArray();
            System.out.println(buf.length);
            DatagramPacket dp = new DatagramPacket(buf, buf.length,group,8080);
            multicastSocket.send(dp);

                } catch (Exception ex) {
                    System.out.println("Exception caught: " + ex);
                    System.exit(0);
                }
            }

        });
        timer.setInitialDelay(0);
        timer.setCoalesce(true);
        timer.start();
//        BufferedImage bufferedImage;
//        while (true){
//
//             bufferedImage=webcam.getImage();
//            ImageIcon icon = new ImageIcon(bufferedImage);
//            jLabel.setIcon(icon);
//            Image scaleImage = icon.getImage().getScaledInstance(120, 120,Image.SCALE_DEFAULT);
//            ImageIcon sendIcon=new ImageIcon(scaleImage);
//            jFrame.add(jLabel);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream oos = new ObjectOutputStream(baos);
//            oos.writeObject(sendIcon);
//            oos.flush();
//            byte[] buf = baos.toByteArray();
//            System.out.println(buf.length);
//            DatagramPacket dp = new DatagramPacket(buf, buf.length,group,8080);
//            multicastSocket.send(dp);
//        }

    }
}
