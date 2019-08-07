package GUI;

import burp.IBurpExtenderCallbacks;
import burp.IHttpRequestResponse;
import common.Config;
import static common.Methods.getListString;
import static common.Methods.insertString;
import static common.Methods.getClipboardContents;
import static common.Methods.removeString;
import static common.Methods.sendRequestResponseToRepeater;
import static common.Methods.unionString;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class KeywordHunter extends javax.swing.JPanel {
    private IBurpExtenderCallbacks callbacks;
    private static KeywordHunter mainPane;
        
    public void initTab(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        initComponents();
        initValue(callbacks);
    }

    public static KeywordHunter getInstance() {
        if(mainPane == null)
            mainPane = new KeywordHunter();
        return mainPane;
    }

    public IHttpRequestResponse getRequestObjectByRow(JTable table, int row) {
            return getRequestObjectByModelIndex( (DefaultTableModel)table.getModel(), table.convertRowIndexToModel(row) );
    }
    
    public IHttpRequestResponse getRequestObjectByModelIndex(DefaultTableModel model, int index){
            return (IHttpRequestResponse)model.getValueAt(index, model.findColumn("messageInfo"));
    }
    
    private JPopupMenu createPopupMenu(IBurpExtenderCallbacks callbacks) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem Hunter_SendtoRepeater_MenItem = new JMenuItem();
        Hunter_SendtoRepeater_MenItem.setText("Send to Repeater");
        Hunter_SendtoRepeater_MenItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    int rowLenght = Hunter_Table.getSelectedRows().length;
                    if(rowLenght==1){
                        int rowNum = Hunter_Table.getSelectedRow(); 
                        IHttpRequestResponse messageInfo = getRequestObjectByRow(Hunter_Table,rowNum);
                        sendRequestResponseToRepeater(callbacks,messageInfo);
                    }
                }
        });
        popupMenu.add(Hunter_SendtoRepeater_MenItem);
        return popupMenu;
    }
    
    private void addRightClickActions(java.awt.event.MouseEvent evt){
        JPopupMenu popupMenu = createPopupMenu(callbacks);
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            int focusedRowIndex = Hunter_Table.rowAtPoint(evt.getPoint());
            if (focusedRowIndex == -1) {
                return;
            }
            
            Hunter_Table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
            popupMenu.show(Hunter_Table, evt.getX(), evt.getY());
        }
    }
    
    public void initValue(IBurpExtenderCallbacks callbacks){
        if (callbacks.loadExtensionSetting("Hunter_Request_RadioButton") != null){
            Hunter_Request_RadioButton.setSelected(Boolean.parseBoolean(callbacks.loadExtensionSetting("Hunter_Request_RadioButton")));
        }
        else{ Hunter_Request_RadioButton.setSelected(Hunter_Request_RadioButton.isSelected());}
        
        if (callbacks.loadExtensionSetting("Hunter_Response_RadioButton") != null){
            Hunter_Response_RadioButton.setSelected(Boolean.parseBoolean(callbacks.loadExtensionSetting("Hunter_Response_RadioButton")));
        }
        else{ Hunter_Response_RadioButton.setSelected(Hunter_Response_RadioButton.isSelected());}
                
        if (callbacks.loadExtensionSetting("Hunter_Both_RadioButton") != null){
            Hunter_Both_RadioButton.setSelected(Boolean.parseBoolean(callbacks.loadExtensionSetting("Hunter_Both_RadioButton")));
        }
        else{ Hunter_Both_RadioButton.setSelected(Hunter_Both_RadioButton.isSelected());}
        
        if (callbacks.loadExtensionSetting("Hunter_CaseDifference_CheckBox") != null) {
            Hunter_CaseDifference_CheckBox.setSelected(Boolean.parseBoolean(callbacks.loadExtensionSetting("Hunter_CaseDifference_CheckBox")));
        }
        else{ Hunter_CaseDifference_CheckBox.setSelected(Config.HUNTER_CASE); }
        
        if (callbacks.loadExtensionSetting("Hunter_Keyword_List") != null) {
            Hunter_Keyword_List.setListData(callbacks.loadExtensionSetting("Hunter_Keyword_List").split(","));
        }
        else{ Hunter_Keyword_List.setListData(Config.HUNTER_KEYWORDS); }
    }
    
    private void hunterSwitchOn(boolean isEnable){
        Hunter_Add_Button.setEnabled(isEnable);
        Hunter_Add_TextField.setEnabled(isEnable);
        Hunter_Both_RadioButton.setEnabled(isEnable);
        Hunter_CaseDifference_CheckBox.setEnabled(isEnable);
        Hunter_ClearTable_Button.setEnabled(isEnable);
        Hunter_Clear_Button.setEnabled(isEnable);
        Hunter_Keyword_List.setEnabled(isEnable);
        Hunter_Paste_Button.setEnabled(isEnable);
        Hunter_Remove_Button.setEnabled(isEnable);
        Hunter_Request_RadioButton.setEnabled(isEnable);
        Hunter_Response_RadioButton.setEnabled(isEnable);
        Hunter_SaveSettings_Button.setEnabled(isEnable);
        Hunter_Table.setEnabled(isEnable);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Hunter_CaseDifference_CheckBox = new javax.swing.JCheckBox();
        Hunter_Paste_Button = new javax.swing.JButton();
        Hunter_Remove_Button = new javax.swing.JButton();
        Hunter_Clear_Button = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        Hunter_Keyword_List = new javax.swing.JList<>();
        Hunter_Add_Button = new javax.swing.JButton();
        Hunter_Add_TextField = new javax.swing.JTextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        Hunter_Table = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        Hunter_ClearTable_Button = new javax.swing.JButton();
        Hunter_SaveSettings_Button = new javax.swing.JButton();
        Hunter_Request_RadioButton = new javax.swing.JRadioButton();
        Hunter_Response_RadioButton = new javax.swing.JRadioButton();
        Hunter_Both_RadioButton = new javax.swing.JRadioButton();
        Hunter_Switch_CheckBox = new javax.swing.JCheckBox();

        Hunter_CaseDifference_CheckBox.setSelected(false);
        Hunter_CaseDifference_CheckBox.setText("Case difference");
        Hunter_CaseDifference_CheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Hunter_CaseDifference_CheckBoxActionPerformed(evt);
            }
        });

        Hunter_Paste_Button.setText("Paste");
        Hunter_Paste_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Hunter_Paste_ButtonActionPerformed(evt);
            }
        });

        Hunter_Remove_Button.setText("Remove");
        Hunter_Remove_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Hunter_Remove_ButtonActionPerformed(evt);
            }
        });

        Hunter_Clear_Button.setText("Clear");
        Hunter_Clear_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Hunter_Clear_ButtonActionPerformed(evt);
            }
        });

        jScrollPane8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane8.setPreferredSize(new java.awt.Dimension(567, 150));

        Hunter_Keyword_List.setPreferredSize(new java.awt.Dimension(567, 140));
        jScrollPane8.setViewportView(Hunter_Keyword_List);

        Hunter_Add_Button.setText("Add");
        Hunter_Add_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Hunter_Add_ButtonActionPerformed(evt);
            }
        });

        Hunter_Table.setAutoCreateRowSorter(true);
        Hunter_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Host", "Method", "URL", "Keyword", "Found place", "Status", "Length", "messageInfo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Hunter_Table.getTableHeader().setReorderingAllowed(false);
        Hunter_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Hunter_TableMouseClicked(evt);
            }
        });
        Hunter_Table.removeColumn(Hunter_Table.getColumn("messageInfo"));
        jScrollPane10.setViewportView(Hunter_Table);
        if (Hunter_Table.getColumnModel().getColumnCount() > 0) {
            Hunter_Table.getColumnModel().getColumn(0).setPreferredWidth(30);
            Hunter_Table.getColumnModel().getColumn(1).setPreferredWidth(130);
            Hunter_Table.getColumnModel().getColumn(2).setPreferredWidth(65);
            Hunter_Table.getColumnModel().getColumn(3).setPreferredWidth(350);
            Hunter_Table.getColumnModel().getColumn(4).setPreferredWidth(100);
            Hunter_Table.getColumnModel().getColumn(5).setPreferredWidth(100);
            Hunter_Table.getColumnModel().getColumn(6).setPreferredWidth(40);
            Hunter_Table.getColumnModel().getColumn(7).setPreferredWidth(50);
        }

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 30, 5));

        Hunter_ClearTable_Button.setText("Clear Table");
        Hunter_ClearTable_Button.setMaximumSize(new java.awt.Dimension(137, 30));
        Hunter_ClearTable_Button.setMinimumSize(new java.awt.Dimension(137, 30));
        Hunter_ClearTable_Button.setPreferredSize(new java.awt.Dimension(137, 30));
        Hunter_ClearTable_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Hunter_ClearTable_ButtonActionPerformed(evt);
            }
        });
        jPanel12.add(Hunter_ClearTable_Button);

        Hunter_SaveSettings_Button.setText("Save Settings");
        Hunter_SaveSettings_Button.setMaximumSize(new java.awt.Dimension(137, 30));
        Hunter_SaveSettings_Button.setMinimumSize(new java.awt.Dimension(137, 30));
        Hunter_SaveSettings_Button.setPreferredSize(new java.awt.Dimension(137, 30));
        Hunter_SaveSettings_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Hunter_SaveSettings_ButtonActionPerformed(evt);
            }
        });
        jPanel12.add(Hunter_SaveSettings_Button);

        Hunter_Request_RadioButton.setText("Request Only");

        Hunter_Response_RadioButton.setText("Response Only");

        Hunter_Both_RadioButton.setSelected(true);
        Hunter_Both_RadioButton.setText("Hunt Both");

        Hunter_Switch_CheckBox.setSelected(true);
        Hunter_Switch_CheckBox.setText("Hunter Switch");
        Hunter_Switch_CheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Hunter_Switch_CheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Hunter_Switch_CheckBox)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Hunter_Both_RadioButton)
                        .addGap(6, 6, 6)
                        .addComponent(Hunter_Request_RadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Hunter_Response_RadioButton))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 1056, Short.MAX_VALUE)
                        .addComponent(Hunter_CaseDifference_CheckBox)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(Hunter_Add_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Hunter_Add_TextField))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Hunter_Clear_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Hunter_Remove_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Hunter_Paste_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(Hunter_Switch_CheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Hunter_CaseDifference_CheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Hunter_Paste_Button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Hunter_Remove_Button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Hunter_Clear_Button))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Hunter_Add_Button)
                    .addComponent(Hunter_Add_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Hunter_Request_RadioButton)
                    .addComponent(Hunter_Response_RadioButton)
                    .addComponent(Hunter_Both_RadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void Hunter_Paste_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Hunter_Paste_ButtonActionPerformed
        String[] orginStrings = getListString(Hunter_Keyword_List);
        String[] strs = getClipboardContents().split("\n");
        Hunter_Keyword_List.setListData(unionString(orginStrings,strs));
    }//GEN-LAST:event_Hunter_Paste_ButtonActionPerformed

    private void Hunter_Remove_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Hunter_Remove_ButtonActionPerformed
        String[] strs = removeString(getListString(Hunter_Keyword_List),Hunter_Keyword_List.getSelectedIndex());
        Hunter_Keyword_List.setListData(strs);
    }//GEN-LAST:event_Hunter_Remove_ButtonActionPerformed

    private void Hunter_Clear_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Hunter_Clear_ButtonActionPerformed
        String[] strs = {};
        Hunter_Keyword_List.setListData(strs);
    }//GEN-LAST:event_Hunter_Clear_ButtonActionPerformed

    private void Hunter_Add_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Hunter_Add_ButtonActionPerformed
        String[] strs = insertString(getListString(Hunter_Keyword_List),Hunter_Add_TextField.getText());
        Hunter_Keyword_List.setListData(strs);
        Hunter_Add_TextField.setText("");
    }//GEN-LAST:event_Hunter_Add_ButtonActionPerformed

    private void Hunter_TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Hunter_TableMouseClicked
        addRightClickActions(evt);
    }//GEN-LAST:event_Hunter_TableMouseClicked

    private void Hunter_ClearTable_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Hunter_ClearTable_ButtonActionPerformed
        DefaultTableModel tableModel = (DefaultTableModel)Hunter_Table.getModel();
        tableModel.setRowCount( 0 );
    }//GEN-LAST:event_Hunter_ClearTable_ButtonActionPerformed

    private void Hunter_SaveSettings_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Hunter_SaveSettings_ButtonActionPerformed
        callbacks.saveExtensionSetting("Hunter_Both_RadioButton", Boolean.toString(Hunter_Both_RadioButton.isSelected()));
        callbacks.saveExtensionSetting("Hunter_Request_RadioButton", Boolean.toString(Hunter_Request_RadioButton.isSelected()));
        callbacks.saveExtensionSetting("Hunter_Response_RadioButton", Boolean.toString(Hunter_Response_RadioButton.isSelected()));
        callbacks.saveExtensionSetting("Hunter_CaseDifference_CheckBox", Boolean.toString(Hunter_CaseDifference_CheckBox.isSelected()));
        callbacks.saveExtensionSetting("Hunter_Keyword_List", String.join(",", getListString(Hunter_Keyword_List)));
    }//GEN-LAST:event_Hunter_SaveSettings_ButtonActionPerformed

    private void Hunter_CaseDifference_CheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Hunter_CaseDifference_CheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Hunter_CaseDifference_CheckBoxActionPerformed

    private void Hunter_Switch_CheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Hunter_Switch_CheckBoxActionPerformed
        if(Hunter_Switch_CheckBox.isSelected()){
            Hunter_Switch_CheckBox.setText("Hunter Enable");
            hunterSwitchOn(true);
        }
        else{
            Hunter_Switch_CheckBox.setText("Hunter Disnable");
            hunterSwitchOn(false);
        }
    }//GEN-LAST:event_Hunter_Switch_CheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Hunter_Add_Button;
    private javax.swing.JTextField Hunter_Add_TextField;
    public javax.swing.JRadioButton Hunter_Both_RadioButton;
    public javax.swing.JCheckBox Hunter_CaseDifference_CheckBox;
    private javax.swing.JButton Hunter_ClearTable_Button;
    private javax.swing.JButton Hunter_Clear_Button;
    public javax.swing.JList<String> Hunter_Keyword_List;
    private javax.swing.JButton Hunter_Paste_Button;
    private javax.swing.JButton Hunter_Remove_Button;
    public javax.swing.JRadioButton Hunter_Request_RadioButton;
    public javax.swing.JRadioButton Hunter_Response_RadioButton;
    private javax.swing.JButton Hunter_SaveSettings_Button;
    public javax.swing.JCheckBox Hunter_Switch_CheckBox;
    public javax.swing.JTable Hunter_Table;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane8;
    // End of variables declaration//GEN-END:variables
}
