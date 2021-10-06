/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mp.guiclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pi
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    static MainFrame mainFrame;
    static Socket MainSocket = null;
    static int Port = 10000;
    static String Ip = null;
    static String UserName = "";
    static Boolean Connect = false;
    static String TargetUserName = "";
    static Boolean Send = false;
    static Boolean Forward = false;
    static Boolean Backward = false;
    static Boolean Right = false;
    static Boolean Left = false;
    static Boolean ForwardSend = false;
    static Boolean BackwardSend= false;
    static Boolean RightSend = false;
    static Boolean LeftSend = false;
    static String OutputMessage = "";
    static String InputMessage = "";
    
    public String pakosito(String input) {

        String output = null;
        StringBuilder myStringBuilder = new StringBuilder(input);
        Boolean found = false;
        char[] ekezetek = {'á', 'é', 'í', 'ó', 'ö', 'ő', 'ú', 'ü', 'ű', 'Á', 'É', 'Í', 'Ó', 'Ö', 'Ő', 'Ú', 'Ü', 'Ű', '@'};
        char[] pakobetuk = {'a', 'e', 'i', 'o', 'o', 'o', 'u', 'u', 'u', 'A', 'E', 'I', 'O', 'O', 'O', 'U', 'U', 'U', '.'};
        for (int i = 0; i < input.length(); i++) {
            for (int j = 0; j < ekezetek.length; j++) {

                if (input.charAt(i) == ekezetek[j]) {
                    myStringBuilder.setCharAt(i, pakobetuk[j]);
                    found = true;
                }

            }
        }
        if (found) {
            clientInfoWriter(false,"Ekezetet talaltam!");
        }
        input = myStringBuilder.toString();
        return input;
    }

    public void clientInfoWriter(Boolean pakosit,String input) {
        if(pakosit)MainFrame.ClientInfoLabel.setText(pakosito(MainFrame.ClientInfoLabel.getText() + "<br></br>" + input));
        else MainFrame.ClientInfoLabel.setText(MainFrame.ClientInfoLabel.getText() + "<br></br>" + input);
    }
    
    class Reader extends Thread{

        Socket socket = null;
        Reader(Socket mainSocket) {
            this.socket = mainSocket;
        }

        @Override
        public void run() {
            while(true){
                try {
                    
                    PrintWriter pr = new PrintWriter(socket.getOutputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String input = reader.readLine();
                    MainFrame.OutputLabel.setText(MainFrame.OutputLabel.getText()+"<br></br>" + input);
                    
                }catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
    
    class Writer extends Thread{

        Socket socket = null;
        String message = null;

        Writer(Socket mainSocket) {
            this.socket = mainSocket;
        }

        @Override
        public void run() {
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            boolean first = true;
            while(true){
            
                try {
                    PrintWriter pr = new PrintWriter(this.socket.getOutputStream());
                    if(first){
                        pr.println(UserName);
                        pr.flush();
                        first = false;
                    }
                    if(!MainFrame.OutputMessage.equals("")){
                        clientInfoWriter(false,"IRÁNYVÁLTÁS!");
                        pr.println(TargetUserName+"@"+MainFrame.OutputMessage);
                        pr.flush();
                    }
                    else {
                        if(Send){
                            MainFrame.OutputMessage = MainFrame.InputTextArea.getText();
                            Thread.sleep(0);
                            if(MainFrame.OutputMessage.length()<3) {
                                clientInfoWriter(false,"Az üzenet túl rövid!");
                            }
                            else if(MainFrame.OutputMessage.contains("@")) {
                                clientInfoWriter(false,"Tiltott karaktert tartalmaz!");
                            }else {
                                pr.println(TargetUserName+"@"+MainFrame.OutputMessage);
                                pr.flush();
                            }
                            Send = false;
                            MainFrame.InputTextArea.setText("");
                        }
                        
                    }
                    MainFrame.OutputMessage = "";
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public static boolean SocketSetter() throws IOException {
        
        try {
            MainFrame.MainSocket = new Socket(MainFrame.Ip, MainFrame.Port);
            System.out.println("Sikeres csatlakozás a szerverre!");
            MainFrame.ClientInfoLabel.setText(MainFrame.ClientInfoLabel.getText()+"<br></br>Sikeres csatlakozás a szerverre!");
            return true;
        } catch (Exception e) {
            MainFrame.ClientInfoLabel.setText(MainFrame.ClientInfoLabel.getText()+"<br></br>Nem sikerült csatlakozi a szerverre, lehet, hogy offline!");
            return false;
        }

    }
    
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
        InputTextArea = new javax.swing.JTextArea();
        SendButton = new javax.swing.JButton();
        IPInputTextFiled = new javax.swing.JTextField();
        ConnectButton = new javax.swing.JButton();
        ServerIPLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        OutputLabel = new javax.swing.JLabel();
        UpLapbelMarker = new javax.swing.JLabel();
        BackwardLabelMarker = new javax.swing.JLabel();
        RightLabelMarker = new javax.swing.JLabel();
        LeftLabelMarker = new javax.swing.JLabel();
        UserNameInputTextField = new javax.swing.JTextField();
        UserNameSaveButton = new javax.swing.JButton();
        UserNameLabelMarker = new javax.swing.JLabel();
        TargetUserNameLabelMarker = new javax.swing.JLabel();
        TargetUserNameSaveButton = new javax.swing.JButton();
        TargetUserNameLabel = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        AllUserListLabel = new javax.swing.JLabel();
        ForwardButton = new javax.swing.JButton();
        BackwardButton = new javax.swing.JButton();
        RightButton = new javax.swing.JButton();
        LeftButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        ClientInfoLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        InputTextArea.setColumns(20);
        InputTextArea.setRows(5);
        jScrollPane1.setViewportView(InputTextArea);

        SendButton.setText("Küld!");
        SendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendButtonActionPerformed(evt);
            }
        });

        IPInputTextFiled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IPInputTextFiledActionPerformed(evt);
            }
        });

        ConnectButton.setText("Csatlakozás");
        ConnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectButtonActionPerformed(evt);
            }
        });

        ServerIPLabel.setText("Szerver IP:");

        OutputLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        OutputLabel.setBorder(javax.swing.BorderFactory.createTitledBorder("Bejövő üzenetek"));
        jScrollPane2.setViewportView(OutputLabel);

        UpLapbelMarker.setText("Előre:");

        BackwardLabelMarker.setText("Hátra:");

        RightLabelMarker.setText("Jobbra:");

        LeftLabelMarker.setText("Balra:");

        UserNameInputTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserNameInputTextFieldActionPerformed(evt);
            }
        });

        UserNameSaveButton.setText("Mentés");
        UserNameSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserNameSaveButtonActionPerformed(evt);
            }
        });

        UserNameLabelMarker.setText("Felhasználónév:");

        TargetUserNameLabelMarker.setText("Cél kliens felhasználóneve:");

        TargetUserNameSaveButton.setText("Mentés");
        TargetUserNameSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TargetUserNameSaveButtonActionPerformed(evt);
            }
        });

        AllUserListLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        AllUserListLabel.setBorder(javax.swing.BorderFactory.createTitledBorder("Felhasználók"));
        jScrollPane3.setViewportView(AllUserListLabel);

        ForwardButton.setText("NEM ");
        ForwardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ForwardButtonActionPerformed(evt);
            }
        });

        BackwardButton.setText("NEM ");
        BackwardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackwardButtonActionPerformed(evt);
            }
        });

        RightButton.setText("NEM ");
        RightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RightButtonActionPerformed(evt);
            }
        });

        LeftButton.setText("NEM ");
        LeftButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LeftButtonActionPerformed(evt);
            }
        });

        ClientInfoLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ClientInfoLabel.setBorder(javax.swing.BorderFactory.createTitledBorder("Kliens info"));
        ClientInfoLabel.setMaximumSize(new java.awt.Dimension(10, 20));
        ClientInfoLabel.setMinimumSize(new java.awt.Dimension(10, 20));
        jScrollPane4.setViewportView(ClientInfoLabel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ServerIPLabel)
                                .addGap(3, 3, 3)
                                .addComponent(IPInputTextFiled, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ConnectButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(RightLabelMarker)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(RightButton))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(UpLapbelMarker)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ForwardButton)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(LeftLabelMarker)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(LeftButton))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(BackwardLabelMarker)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(BackwardButton)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 269, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(UserNameLabelMarker, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(UserNameInputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(UserNameSaveButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(TargetUserNameLabelMarker, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TargetUserNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TargetUserNameSaveButton))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                        .addComponent(jScrollPane3)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ServerIPLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(IPInputTextFiled, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ConnectButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(UpLapbelMarker)
                            .addComponent(BackwardLabelMarker)
                            .addComponent(ForwardButton)
                            .addComponent(BackwardButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(RightLabelMarker)
                            .addComponent(LeftLabelMarker)
                            .addComponent(RightButton)
                            .addComponent(LeftButton)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(UserNameInputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(UserNameLabelMarker)
                            .addComponent(UserNameSaveButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TargetUserNameSaveButton)
                            .addComponent(TargetUserNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TargetUserNameLabelMarker, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(SendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void IPInputTextFiledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IPInputTextFiledActionPerformed
        // TODO add your handling code here:
        
                
    }//GEN-LAST:event_IPInputTextFiledActionPerformed

    private void SendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendButtonActionPerformed
        // TODO add your handling code here:
        MainFrame.Send = true;
    }//GEN-LAST:event_SendButtonActionPerformed

    private void UserNameInputTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserNameInputTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UserNameInputTextFieldActionPerformed

    private void ConnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectButtonActionPerformed
        // TODO add your handling code here:
        if(!Connect){
            MainFrame.Ip = MainFrame.IPInputTextFiled.getText();
            if(MainFrame.UserName.equals("")) {
               clientInfoWriter(false,"Nincs megadva felhasználónév!");
            }
            if(MainFrame.IPInputTextFiled.getText().equals("")) {
                clientInfoWriter(false,"Nincs megadva szerver IP-cím!");
            }
            if(MainFrame.TargetUserName.equals("")) {
                clientInfoWriter(false,"Nincs megadva cél kliens név!");
            }
            if(!MainFrame.Ip.equals("") && !MainFrame.UserName.equals("")){
                MainFrame.IPInputTextFiled.setEditable(false);
                MainFrame.ConnectButton.setText("Lecsatlakozás");
                Connect = !Connect;
                try {
                    MainFrame.SocketSetter();
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                MainFrame.mainFrame.start();
            }
        }
        else 
        {
            MainFrame.IPInputTextFiled.setEditable(true);
            MainFrame.ConnectButton.setText("Csatlakozás");
            Connect = !Connect;
        }
    }//GEN-LAST:event_ConnectButtonActionPerformed

    private void UserNameSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserNameSaveButtonActionPerformed
        // TODO add your handling code here:
        if(!MainFrame.UserNameInputTextField.getText().equals("")){
            MainFrame.UserName = MainFrame.UserNameInputTextField.getText();
            MainFrame.UserNameSaveButton.setEnabled(false);
            MainFrame.UserNameInputTextField.setEditable(false);
            clientInfoWriter(false,"Felhasználónév elmentve!");
        }
        else {
            clientInfoWriter(false,"Nincs megadva felhasználónlév!");
        }
    }//GEN-LAST:event_UserNameSaveButtonActionPerformed

    private void TargetUserNameSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TargetUserNameSaveButtonActionPerformed
        // TODO add your handling code here:
        
        if(!MainFrame.TargetUserNameLabel.getText().equals("")){
            clientInfoWriter(false,"Cél kliens  felhasználónév elmentve!");
            MainFrame.TargetUserName = MainFrame.TargetUserNameLabel.getText();
        }
        else {
            clientInfoWriter(false,"Cél kliens  felhasználónév nem lehet üres!");
        }
        
    }//GEN-LAST:event_TargetUserNameSaveButtonActionPerformed

    private void ForwardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ForwardButtonActionPerformed
        // TODO add your handling code here:
        if(Forward){
            ForwardSend = false;
            ForwardButton.setText("NEM ");
            clientInfoWriter(false,"Előre:"+ForwardSend);
            MainFrame.OutputMessage = "FORWARD:FALSE";
        }
        else {
            Backward = false;
            //ForwardSend = true;
            BackwardSend = false;
            clientInfoWriter(false,"Előre:"+ForwardSend+"<br></br>Hátra:"+BackwardSend);
            ForwardButton.setText("IGEN");
            BackwardButton.setText("NEM ");
            MainFrame.OutputMessage = "FORWARD:TRUE";
        }
        Forward = !Forward;
    }//GEN-LAST:event_ForwardButtonActionPerformed

    private void RightButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RightButtonActionPerformed
        // TODO add your handling code here:
        if(Right){
            //RightSend = false;
            clientInfoWriter(false,"Jobbra:"+RightSend);
            RightButton.setText("NEM ");
            MainFrame.OutputMessage = "RIGHT:FALSE";
        }
        else {
            Left = false;
            LeftSend = false;
            //RightSend = true;
            clientInfoWriter(false,"Jobbra:"+RightSend+"<br></br>Balra:"+LeftSend);
            RightButton.setText("IGEN");
            LeftButton.setText("NEM ");
            MainFrame.OutputMessage = "RIGHT:TRUE";
        }
        Right = !Right;
    }//GEN-LAST:event_RightButtonActionPerformed

    private void BackwardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackwardButtonActionPerformed
        // TODO add your handling code here:
        
        if(Backward){
            //BackwardSend = false;
            clientInfoWriter(false,"Hátra:"+BackwardSend);
            BackwardButton.setText("NEM ");
            MainFrame.OutputMessage = "BACKWARD:FALSE";
        }
        else {
            Forward = false;
            //ForwardSend = false;
            //BackwardSend = true;
            MainFrame.OutputMessage = "BACKWARD:TRUE";
            clientInfoWriter(false,"Előre:"+ForwardSend+"<br></br>Hátra:"+BackwardSend);
            BackwardButton.setText("IGEN");
            ForwardButton.setText("NEM ");
            
        }
        Backward = !Backward;
    }//GEN-LAST:event_BackwardButtonActionPerformed

    private void LeftButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LeftButtonActionPerformed
        // TODO add your handling code here:
        //
        
        if(Left){
            LeftSend = false;
            clientInfoWriter(false,"Balra:"+LeftSend);
            LeftButton.setText("NEM ");
            MainFrame.OutputMessage = "LEFT:FALSE";
        }
        else {
            Right = false;
            LeftSend = true;
            MainFrame.OutputMessage = "LEFT:TRUE";
            clientInfoWriter(false,"Jobbra:"+RightSend+"<br></br>Balra:"+LeftSend);
            LeftButton.setText("IGEN");
            RightButton.setText("NEM ");
        }
        Left = !Left;
    }//GEN-LAST:event_LeftButtonActionPerformed

    private MainFrame start(){
            
            new Writer(MainSocket).start();
            new Reader(MainSocket).start();
            
            return this;
    }
    
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
                //new MainFrame().start().setVisible(true);
                MainFrame.mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                MainFrame.ClientInfoLabel.setText("<html>");
                MainFrame.OutputLabel.setText("<html>");
                MainFrame.TargetUserNameLabel.setText("Sanyi");
                MainFrame.UserNameInputTextField.setText("Sanyi");
                MainFrame.IPInputTextFiled.setText("192.168.56.1");
                //mainFrame.start();
                //new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel AllUserListLabel;
    public static javax.swing.JButton BackwardButton;
    private javax.swing.JLabel BackwardLabelMarker;
    public static javax.swing.JLabel ClientInfoLabel;
    public static javax.swing.JButton ConnectButton;
    public static javax.swing.JButton ForwardButton;
    public static javax.swing.JTextField IPInputTextFiled;
    public static javax.swing.JTextArea InputTextArea;
    public static javax.swing.JButton LeftButton;
    private javax.swing.JLabel LeftLabelMarker;
    public static javax.swing.JLabel OutputLabel;
    public static javax.swing.JButton RightButton;
    private javax.swing.JLabel RightLabelMarker;
    public static javax.swing.JButton SendButton;
    public static javax.swing.JLabel ServerIPLabel;
    public static javax.swing.JTextField TargetUserNameLabel;
    private javax.swing.JLabel TargetUserNameLabelMarker;
    public static javax.swing.JButton TargetUserNameSaveButton;
    private javax.swing.JLabel UpLapbelMarker;
    public static javax.swing.JTextField UserNameInputTextField;
    private javax.swing.JLabel UserNameLabelMarker;
    public static javax.swing.JButton UserNameSaveButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}
