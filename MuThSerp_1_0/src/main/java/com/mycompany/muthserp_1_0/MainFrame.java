/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.muthserp_1_0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sunshine
 */
public class MainFrame extends javax.swing.JFrame {
    
    static ServerSocket serverSocket;
    static Map<String, String> message;
    static Map<String,MessageData> messageTest;
    static List<String> threadUserNameList;
    static Map<Integer,Boolean> threadToKillMap;
    static Integer threadCounter = 0;
    
    class MessageData{
    
        String targetUser;
        String sourceUser;
        String message;

        private MessageData(String targetUser, String threadUserName, String message) {
            this.sourceUser = threadUserName;
            this.targetUser = targetUser;
            this.message = message;
        }
    
    }
    
    private void start() throws IOException {
        new ServerSetter().start();
        new ServerInfo().start();
        
    }
    
    class ServerSetter extends Thread{
    
        @Override
        public void run(){
        
            try {
                
                MainFrame.serverSocket = new ServerSocket(10000);
                message = new HashMap<String, String>();
                messageTest = new HashMap<String,MessageData>();
                threadUserNameList = new ArrayList<String>();
                threadToKillMap = new HashMap<Integer,Boolean>();
                MessageListLabel.setText("<html>");
                ServerInfoLabel.setText("<html>");
                UserListLabel.setText("<html>");
                while (true) {
                    //System.out.println("Kliensre vár!");
                    ServerInfoLabel.setText(ServerInfoLabel.getText()+"A szerver kliensre vár!<br></br>");
                    Socket clientSocket = serverSocket.accept();
                    new Reader(clientSocket, message).start();
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        
        }
        
    }
    
    class ServerInfo extends Thread {
    
    
        @Override
        public void run(){
        
            while(true){
            
                try {
                    MainFrame.UserdThreadsLabel.setText(threadCounter.toString());
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
        
    }
    
    class TestThread extends Thread {
    
    
        @Override
        public void run(){
        
            for(int i = 0; i < 10; i++) {
            
                MessageListLabel.setText(""+i);
                System.out.println(""+i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            }
        
        }
        
    }
    
    class Sender extends Thread {
        
        Socket writerSocket;
        //int threadId;
        String threadUserName;
        Map<String,MessageData> messageTest;
        Boolean kill;
        
        private Sender(Socket writerSocket,Boolean kill) {
            this.messageTest = MainFrame.messageTest;
            //this.threadUserName = threadUserNameList.get(this.threadId);
            this.writerSocket = writerSocket;
            this.kill = kill;
            ServerInfoLabel.setText(ServerInfoLabel.getText() +"<br></br>A Sender szál elindult!");
        }
        
        @Override
        public void run() {
            while(true){
                
                try
                {
                    if(this.messageTest.get(threadUserName).message != null){
                        String message = this.messageTest.get(threadUserName).message;
                        String targetUserName = this.messageTest.get(threadUserName).targetUser;
                        String sourceUserName = this.messageTest.get(threadUserName).sourceUser;
                        MessageListLabel.setText(MessageListLabel.getText() + "<br></br>Kimenet: "+sourceUserName+"->"+targetUserName+": "+message);
                        //System.out.println(threadId+": "+sourceUserName+"->"+targetUserName+": "+message);
                        PrintWriter pr = new PrintWriter(this.writerSocket.getOutputStream());
                        pr.println(message);
                        pr.flush();
                        messageTest.remove(threadUserName);
                    }
                }
                catch(Exception e)
                {
                }
                if(this.kill) {
                    System.out.println(this.kill);
                    //ServerInfoLabel.setText(ServerInfoLabel.getText() +"<br></br>A Sender szál leáll!");
                    //Thread.currentThread().interrupt();
                }
            }
        }
    }

    class Reader extends Thread {

        Socket clientSocket = null;
        String threadUserName = null;
        Map<String, String> message;
        Boolean kill = false;
        Thread Sender;

        private Reader(Socket clientSocket, Map<String, String> message) {
            this.clientSocket = clientSocket;
            this.message = message;
            MainFrame.threadCounter++;
        }

        @Override
        public void run() {
            boolean connectCheck = true;
            boolean first = true;
            while (connectCheck) {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    /*
                        Beolvas a sockettől.
                     */
                    InputStream input = this.clientSocket.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
                    String line = bufferedReader.readLine();

                    /*
                        Ha nincs bejövő infó akkor a kliens nem elérhető.
                     */
                    if (line == null) {
                        connectCheck = false;
                    } else {
                        if (first) {
                            /*
                               Az első sor tartalmazza a kliens azonosítóját.
                            */
                            first = false;
                            this.threadUserName = line;
                            //MainFrame.threadUserNameList.add(this.threadUserName);
                            //System.out.println(this.threadUserName + " csatlakozott!\nÖsszes kliens felhasználó:");
                            UserListLabel.setText("<html>");
                            ServerInfoLabel.setText(ServerInfoLabel.getText()+this.threadUserName + " csatlakozott!");
                            for (int i = 0; i < MainFrame.threadUserNameList.size(); i++) {
                                UserListLabel.setText(UserListLabel.getText()+"<br></br>"+MainFrame.threadUserNameList.get(i));
                                //System.out.println(TestFrame.threadUserNameList.get(i));

                            }
                            Sender = new Sender(clientSocket,kill);
                            Sender.start();
                            
                            
                        } else {
                            /*
                                Kerka
                            
                                Egy @ karakter választja el a címzettet a feladótól.
                            */
                            String[] lineParts = line.split("@");
                            String messageKey = lineParts[0];
                            message.put(messageKey, lineParts[1]);
                            System.out.println("Reader_"+MainFrame.threadCounter+" "+this.threadUserName+"->"+messageKey+": "+lineParts[1]);
                            MessageListLabel.setText(MessageListLabel.getText()+"<br></br>Bemenet: Reader_"+MainFrame.threadCounter+" "+this.threadUserName+"->"+messageKey+": "+lineParts[1]);
                            messageTest.put(messageKey,new MessageData(messageKey,this.threadUserName,lineParts[1]));
                            
                            /*
                                Berakjuk a megfelelő helyre a map-ben.
                             */
                            
                        }
                    }

                } catch (IOException ex) {
                    connectCheck = false;
                    System.out.println("Kliens_" + MainFrame.threadCounter + " lecsatlakozott!\n\n<==========>\n");
                    threadUserNameList.remove(MainFrame.threadCounter);
                    ServerInfoLabel.setText(ServerInfoLabel.getText()+"<br></br>"+this.threadUserName+" lecsatlakozott!");
                    UserListLabel.setText("<html>");
                    for (int i = 0; i < MainFrame.threadUserNameList.size(); i++) {
                        UserListLabel.setText(UserListLabel.getText()+"<br></br>"+MainFrame.threadUserNameList.get(i));
                        //System.out.println(TestFrame.threadUserNameList.get(i));

                    }
                    ServerInfoLabel.setText(ServerInfoLabel.getText()+"<br></br>A reader_"+MainFrame.threadCounter+" szál leáll!");
                    //this.Sender.interrupt();
                    //ServerInfoLabel.setText(ServerInfoLabel.getText()+"<br></br>A sender_"+MainFrame.threadCounter+" szál leáll!");
                    //this.Sender.stop();
                    //this.stop();
                    //System.out.println("Sender_"+this.counter+"\n"+this.Sender.isAlive());
                    
                    //Thread.currentThread().interrupt();
                }
            }

        }
    ;

    }
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ServerInfoLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        MessageListLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        UserListLabel = new javax.swing.JLabel();
        UserdThreadsLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ServerInfoLabel.setToolTipText("");
        ServerInfoLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ServerInfoLabel.setBorder(javax.swing.BorderFactory.createTitledBorder("Szerver info"));
        ServerInfoLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jScrollPane1.setViewportView(ServerInfoLabel);

        MessageListLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        MessageListLabel.setBorder(javax.swing.BorderFactory.createTitledBorder("Üzenetek"));
        MessageListLabel.setFocusTraversalPolicyProvider(true);
        MessageListLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jScrollPane2.setViewportView(MessageListLabel);

        UserListLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        UserListLabel.setBorder(javax.swing.BorderFactory.createTitledBorder("Csatlakoztatott kliensek"));
        jScrollPane3.setViewportView(UserListLabel);

        UserdThreadsLabel.setText(" 0 ");
        UserdThreadsLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(UserdThreadsLabel)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(UserdThreadsLabel)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainFrame testFrame = new MainFrame();
                testFrame.setVisible(true);
                    
                try {
                    testFrame.start();
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel MessageListLabel;
    public static javax.swing.JLabel ServerInfoLabel;
    public static javax.swing.JLabel UserListLabel;
    public static javax.swing.JLabel UserdThreadsLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
