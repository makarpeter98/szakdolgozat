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
    //static Map<String, String> message;
    static Map<String,MessageData> message;
    static List<String> threadUserNameList;
    static Map<String,MessageData> messageTest;
    static Map<Integer,Boolean> ThreadRunSetter;
    static int counter = 0;
	
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
        
    }
    
    class ServerHandler extends Thread{
        
        @Override
        public void run(){
    
            while(true) {
                try {
                    MainFrame.ActiveThreadsLabel.setText(" "+MainFrame.ThreadRunSetter.size()+" ");
                    UserListLabel.setText("<html>");
                    for (int i = 0; i < MainFrame.threadUserNameList.size(); i++) {
                        UserListLabel.setText(UserListLabel.getText()+"<br></br>"+MainFrame.threadUserNameList.get(i));
                    }
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
        }
    }
    
    class ServerSetter extends Thread{
    
        @Override
        public void run(){
        
            try {
                
                MainFrame.serverSocket = new ServerSocket(10000);
                //message = new HashMap<String, String>();
                message = new HashMap<String,MessageData>();
                threadUserNameList = new ArrayList<String>();
                MainFrame.ThreadRunSetter = new HashMap<Integer,Boolean>();
                MessageListLabel.setText("<html>");
                ServerInfoLabel.setText("<html>");
                UserListLabel.setText("<html>");
                while (true) {
                    //System.out.println("Kliensre vár!");
                    ServerInfoLabel.setText(ServerInfoLabel.getText()+"A szerver kliensre vár!<br></br>");
                    Socket clientSocket = serverSocket.accept();
                    new ServerHandler().start();
                    new Reader(clientSocket, MainFrame.counter).start();
                    MainFrame.counter++;
                }
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
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
        int counter;
        String threadUserName;
        Map<String,MessageData> messageTest;
        public Boolean kill;
        
        private Sender(int threadId,Socket writerSocket,Boolean kill) {
            this.counter = threadId;
            this.messageTest = MainFrame.message;
            this.threadUserName = threadUserNameList.get(this.counter);
            this.writerSocket = writerSocket;
            this.kill = kill;
            ServerInfoLabel.setText(ServerInfoLabel.getText() +"<br></br>A Sender szál elindult!");
            MainFrame.ThreadRunSetter.put(counter+1, false);
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
                        MessageListLabel.setText(MessageListLabel.getText() + "<br></br>Kimenet: "+counter+": "+sourceUserName+"->"+targetUserName+": "+message);
                        PrintWriter pr = new PrintWriter(this.writerSocket.getOutputStream());
                        pr.println(message);
                        pr.flush();
                        messageTest.remove(threadUserName);
                    }
                }
                catch(Exception e)
                {
                }
                try{
                
                    if(MainFrame.ThreadRunSetter.get(counter+1)) {
                    ServerInfoLabel.setText(ServerInfoLabel.getText()+"<br></br>A sender_"+this.counter+" szál leáll!");
                    MainFrame.ThreadRunSetter.remove(counter+1);
                    break;
                    //ServerInfoLabel.setText(ServerInfoLabel.getText() +"<br></br>A Sender szál leáll!");
                    //Thread.currentThread().interrupt();
                    }
                    
                }
                catch(Exception e){
                
                }
                
            }
        }
    }

    class Reader extends Thread {

        Socket clientSocket = null;
        final int counter;
        String threadUserName = null;
        Boolean kill = false;
        Thread Sender;

        private Reader(Socket clientSocket, int counter) {
            this.clientSocket = clientSocket;
            this.counter = counter;
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
                            MainFrame.ThreadRunSetter.put(this.counter, false);
                            MainFrame.threadUserNameList.add(this.threadUserName);
                            //System.out.println(this.threadUserName + " csatlakozott!\nÖsszes kliens felhasználó:");
                            
                            ServerInfoLabel.setText(ServerInfoLabel.getText()+this.threadUserName + " csatlakozott!");
                            
                            Sender = new Sender(this.counter, clientSocket,kill);
                            Sender.start();
                            
                        } else {
                            /*
                                Kerka
                            
                                Egy @ karakter választja el a címzettet a feladótól.
                            */
                            String[] lineParts = line.split("@");
                            String messageKey = lineParts[0];
                            //message.put(messageKey, lineParts[1]);
                            System.out.println("Reader_"+this.counter+" "+this.threadUserName+"->"+messageKey+": "+lineParts[1]);
                            MessageListLabel.setText(MessageListLabel.getText()+"<br></br>Bemenet: Reader_"+this.counter+" "+this.threadUserName+"->"+messageKey+": "+lineParts[1]);
                            message.put(messageKey,new MessageData(messageKey,this.threadUserName,lineParts[1]));
                            
                            /*
                                Berakjuk a megfelelő helyre a map-ben.
                             */
                            
                        }
                    }

                } catch (IOException ex) {
                    connectCheck = false;
                    System.out.println("Kliens_" + this.counter + " lecsatlakozott!\n\n<==========>\n");
                    
                    ServerInfoLabel.setText(ServerInfoLabel.getText()+"<br></br>"+this.threadUserName+" lecsatlakozott!");
                    UserListLabel.setText("<html>");
                    for (int i = 0; i < MainFrame.threadUserNameList.size(); i++) {
                        UserListLabel.setText(UserListLabel.getText()+"<br></br>"+MainFrame.threadUserNameList.get(i));
                        //System.out.println(TestFrame.threadUserNameList.get(i));

                    }
                    ServerInfoLabel.setText(ServerInfoLabel.getText()+"<br></br>A reader_"+this.counter+" szál leáll!");
                    //this.Sender.interrupt();
                    try{
                        MainFrame.threadUserNameList.remove(this.counter);
                        MainFrame.ThreadRunSetter.replace(this.counter+1, true);
                        MainFrame.message.remove(this.threadUserName);
                        MainFrame.threadUserNameList.remove(this.threadUserName);
                        MainFrame.counter--;
                    }
                    catch(Exception e){}
                    MainFrame.ThreadRunSetter.remove(this.counter);
                    break;
                    
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
        ActiveThreadsLabelMarker = new javax.swing.JLabel();
        ActiveThreadsLabel = new javax.swing.JLabel();

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

        ActiveThreadsLabelMarker.setText(" Aktív szálak:");

        ActiveThreadsLabel.setText("0");
        ActiveThreadsLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ActiveThreadsLabelMarker, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ActiveThreadsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ActiveThreadsLabelMarker, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ActiveThreadsLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
    public static javax.swing.JLabel ActiveThreadsLabel;
    private javax.swing.JLabel ActiveThreadsLabelMarker;
    public static javax.swing.JLabel MessageListLabel;
    public static javax.swing.JLabel ServerInfoLabel;
    public static javax.swing.JLabel UserListLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
