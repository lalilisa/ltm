
package video.chat;

import java.awt.Font;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class GUICHat extends javax.swing.JFrame {

    private MulticastSocket socket=null;
    private InetAddress group;
    private int port=8001;
    private Thread thread;
    public GUICHat() throws UnknownHostException {
        this.group = InetAddress.getByName("224.0.0.251");
        initComponents();
    }

 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        message = new javax.swing.JTextArea();
        textEnterMessage = new javax.swing.JTextField();
        buttonSendMessage = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        message.setColumns(20);
        message.setRows(5);
        jScrollPane1.setViewportView(message);

        textEnterMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textEnterMessageActionPerformed(evt);
            }
        });

        buttonSendMessage.setText("Gửi");
        buttonSendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSendMessageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(textEnterMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
                .addComponent(buttonSendMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(133, 133, 133))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonSendMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textEnterMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 66, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textEnterMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textEnterMessageActionPerformed
       
    }//GEN-LAST:event_textEnterMessageActionPerformed

    private void buttonSendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSendMessageActionPerformed
        try {
            String message=textEnterMessage.getText();
            System.out.println(message);
            sendMessgae(socket,message,"name");
            textEnterMessage.setText("");
        } catch (IOException ex) {
            Logger.getLogger(GUICHat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonSendMessageActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GUICHat chat=new GUICHat();
                    chat.Chat();
                    chat.setVisible(true);
                    
                } catch (UnknownHostException ex) {
                    Logger.getLogger(GUICHat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
    }
     public void Chat(){
        try
        {
            InetAddress group = this.group;
            int port = this.port;
            this.socket = new MulticastSocket(port);
            this.socket.setTimeToLive(0);
            this.socket.joinGroup(group);
            System.out.println("Connected Success");
            readThread();
        }
        catch(SocketException se)
        {
            System.out.println("Error creating socket");
            se.printStackTrace();
        }
        catch(IOException ie)
        {
            System.out.println("Error reading/writing from/to socket");
            ie.printStackTrace();
        }
    }
     public void sendMessgae(MulticastSocket socket,String message,String nickName) throws IOException{
          
                if(message.equalsIgnoreCase(TERMINATE))
                {
                    finished = true;
                    socket.leaveGroup(group);
                    socket.close();
                  
                }
                System.out.println(nickName);
                message = nickName+ ": " + message;
                System.out.println(message);
                byte[] buffer = message.getBytes();
                DatagramPacket datagram = new
                        DatagramPacket(buffer,buffer.length,group,port);
                socket.send(datagram);
           
     }
     void readThread(){
        thread=new Thread(){
            @Override
        public void run()
       {   
            while(!Constain.finished)
            {
                byte[] buffer = new byte[1000];
                DatagramPacket datagram = new
                        DatagramPacket(buffer,buffer.length,group,port);
                String messages;
                try
                {
                    socket.receive(datagram);
                    messages = new
                            String(buffer,0,datagram.getLength(),"UTF-8");
                    System.out.println("nhan "+messages);
                    message.append(messages+"\n");
                }
                catch(IOException e)
                {
                    System.out.println("Socket closed!");
                }
            }
                }

            };
        thread.start();
     }
    private static final String TERMINATE = "Exit";
    static String name;
    static volatile boolean finished = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonSendMessage;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea message;
    private javax.swing.JTextField textEnterMessage;
    // End of variables declaration//GEN-END:variables

    public JButton getButtonSendMessage() {
        return buttonSendMessage;
    }

    public void setButtonSendMessage(JButton buttonSendMessage) {
        this.buttonSendMessage = buttonSendMessage;
    }

    public JTextArea getMessage() {
        return message;
    }

    public void setMessage(JTextArea message) {
        this.message = message;
    }

    public JTextField getTextEnterMessage() {
        return textEnterMessage;
    }

    public void setTextEnterMessage(JTextField textEnterMessage) {
        this.textEnterMessage = textEnterMessage;
    }
    
}


