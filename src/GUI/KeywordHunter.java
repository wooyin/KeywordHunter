package GUI;

import burp.IBurpExtenderCallbacks;
import burp.IHttpRequestResponse;
import burp.ITextEditor;
import static common.Methods.getListString;
import static common.Methods.insertString;
import static common.Methods.getClipboardContents;
import static common.Methods.removeString;
import static common.Methods.sendRequestResponseToRepeater;
import static common.Methods.unionString;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private IHttpRequestResponse textMessageInfo;
    ITextEditor request_textEditor;
    ITextEditor response_textEditor;
    private final String[] HUNTER_KEYWORDS = { "password", "sql", "pwd" ,"ftp","ssh"};
    private final String FILE_FILTER_STRING = "js;css;swf;gif;png";
       
    private void addComponentListener(Component component) {
        component.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                textAddRightClickActions(evt);
            }
        });
    }
            
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
    
    private void setData(IHttpRequestResponse messageInfo){
        this.textMessageInfo = messageInfo;
        request_textEditor.setText(messageInfo.getRequest());
        response_textEditor.setText(messageInfo.getResponse());
    }
    
    private JPopupMenu tableCreatePopupMenu(IBurpExtenderCallbacks callbacks) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem Hunter_Table_SendtoRepeater_MenItem = new JMenuItem();
        Hunter_Table_SendtoRepeater_MenItem.setText("Send to Repeater");
        Hunter_Table_SendtoRepeater_MenItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    int rowLenght = Hunter_Table.getSelectedRows().length;
                    if(rowLenght==1){
                        int rowNum = Hunter_Table.getSelectedRow(); 
                        IHttpRequestResponse messageInfo = getRequestObjectByRow(Hunter_Table,rowNum);
                        sendRequestResponseToRepeater(callbacks,messageInfo);
                    }
                }
        });
        popupMenu.add(Hunter_Table_SendtoRepeater_MenItem);
        return popupMenu;
    }
    
    private void tableAddRightClickActions(java.awt.event.MouseEvent evt){
        JPopupMenu popupMenu = tableCreatePopupMenu(callbacks);
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            int focusedRowIndex = Hunter_Table.rowAtPoint(evt.getPoint());
            if (focusedRowIndex == -1) {
                return;
            }
            
            Hunter_Table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
            popupMenu.show(Hunter_Table, evt.getX(), evt.getY());
        }
    }
    
    private JPopupMenu textCreatePopupMenu(IBurpExtenderCallbacks callbacks) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem Hunter_Text_SendtoRepeater_MenItem = new JMenuItem();
        Hunter_Text_SendtoRepeater_MenItem.setText("Send to Repeater");
        Hunter_Text_SendtoRepeater_MenItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    IHttpRequestResponse messageInfo = textMessageInfo;
                    if(messageInfo != null){
                        sendRequestResponseToRepeater(callbacks,messageInfo);
                    }	
                }
        });
        popupMenu.add(Hunter_Text_SendtoRepeater_MenItem);
        return popupMenu;
    }
    
    private void textAddRightClickActions(java.awt.event.MouseEvent evt){
        JPopupMenu popupMenu = textCreatePopupMenu(callbacks);
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }
       
    public void initValue(IBurpExtenderCallbacks callbacks){
        if (callbacks.loadExtensionSetting("Hunter_CaseSensitive_CheckBox") != null) {
            Hunter_CaseSensitive_CheckBox.setSelected(Boolean.parseBoolean(callbacks.loadExtensionSetting("Hunter_CaseSensitive_CheckBox")));
        }
        
        if (callbacks.loadExtensionSetting("Hunter_Keyword_List") != null) {
            Hunter_Keyword_List.setListData(callbacks.loadExtensionSetting("Hunter_Keyword_List").split(","));
        }
        else{ Hunter_Keyword_List.setListData(HUNTER_KEYWORDS); }
        
        if (callbacks.loadExtensionSetting("File_Filter_TextArea") != null) {
            File_Filter_TextArea.setText(callbacks.loadExtensionSetting("File_Filter_TextArea"));
        }
        else{ File_Filter_TextArea.setText(FILE_FILTER_STRING); }
        
        if (callbacks.loadExtensionSetting("Domain_Filter_TextArea") != null) {
            Domain_Filter_TextArea.setText(callbacks.loadExtensionSetting("Domain_Filter_TextArea"));
        }
        
        if (callbacks.loadExtensionSetting("Request_Body_CheckBox") != null) {
            Request_Body_CheckBox.setSelected(Boolean.parseBoolean(callbacks.loadExtensionSetting("Request_Body_CheckBox")));
        }
        
        if (callbacks.loadExtensionSetting("Request_Headers_CheckBox") != null) {
            Request_Headers_CheckBox.setSelected(Boolean.parseBoolean(callbacks.loadExtensionSetting("Request_Headers_CheckBox")));
        }
        
        if (callbacks.loadExtensionSetting("Response_Body_CheckBox") != null) {
            Response_Body_CheckBox.setSelected(Boolean.parseBoolean(callbacks.loadExtensionSetting("Response_Body_CheckBox")));
        }
        
        if (callbacks.loadExtensionSetting("Response_Headers_CheckBox") != null) {
            Response_Headers_CheckBox.setSelected(Boolean.parseBoolean(callbacks.loadExtensionSetting("Response_Headers_CheckBox")));
        }
        
        if (callbacks.loadExtensionSetting("File_Filter_CheckBox") != null) {
            File_Filter_CheckBox.setSelected(Boolean.parseBoolean(callbacks.loadExtensionSetting("File_Filter_CheckBox")));
        }
                
        if (callbacks.loadExtensionSetting("Domain_Filter_CheckBox") != null) {
            Domain_Filter_CheckBox.setSelected(Boolean.parseBoolean(callbacks.loadExtensionSetting("Domain_Filter_CheckBox")));
        }
        
        File_Filter_TextArea.setEnabled(File_Filter_CheckBox.isSelected());
        Domain_Filter_TextArea.setEnabled(Domain_Filter_CheckBox.isSelected());
       
    }
    
    private void hunterSwitchOn(boolean isEnable){
        All_Hunt_Button.setEnabled(isEnable);
        Default_Hunt_Button.setEnabled(isEnable);
        Domain_Filter_CheckBox.setEnabled(isEnable);
        Domain_Filter_TextArea.setEnabled(isEnable);
        File_Filter_CheckBox.setEnabled(isEnable);
        File_Filter_TextArea.setEnabled(isEnable);
        Hunter_Add_Button.setEnabled(isEnable);
        Hunter_Add_TextField.setEnabled(isEnable);
        Hunter_CaseSensitive_CheckBox.setEnabled(isEnable);
        Hunter_ClearTable_Button.setEnabled(isEnable);
        Hunter_Clear_Button.setEnabled(isEnable);
        Hunter_Keyword_List.setEnabled(isEnable);
        Hunter_Paste_Button.setEnabled(isEnable);
        Hunter_Remove_Button.setEnabled(isEnable);
        Hunter_SaveSettings_Button.setEnabled(isEnable);
//        Hunter_Switch_CheckBox.setEnabled(isEnable);
        Hunter_Table.setEnabled(isEnable);
        Request_Body_CheckBox.setEnabled(isEnable);
        Request_Headers_CheckBox.setEnabled(isEnable);
        Response_Body_CheckBox.setEnabled(isEnable);
        Response_Headers_CheckBox.setEnabled(isEnable);
        jLabel1.setEnabled(isEnable);
        jLabel2.setEnabled(isEnable);
        jPanel1.setEnabled(isEnable);
        jPanel12.setEnabled(isEnable);
        jScrollPane1.setEnabled(isEnable);
        jScrollPane10.setEnabled(isEnable);
        jScrollPane2.setEnabled(isEnable);
        jScrollPane3.setEnabled(isEnable);
        jScrollPane4.setEnabled(isEnable);
        jScrollPane5.setEnabled(isEnable);
        jScrollPane8.setEnabled(isEnable);
        jTabbedPane1.setEnabled(isEnable);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        Hunter_CaseSensitive_CheckBox = new javax.swing.JCheckBox();
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
        Hunter_Switch_CheckBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        request_textEditor = callbacks.createTextEditor();
        addComponentListener(request_textEditor.getComponent());
        jScrollPane2 = new javax.swing.JScrollPane(request_textEditor.getComponent());
        response_textEditor = callbacks.createTextEditor();
        addComponentListener(response_textEditor.getComponent());
        jScrollPane3 = new javax.swing.JScrollPane(response_textEditor.getComponent());
        Request_Headers_CheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        Request_Body_CheckBox = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        Response_Headers_CheckBox = new javax.swing.JCheckBox();
        Response_Body_CheckBox = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        All_Hunt_Button = new javax.swing.JButton();
        Default_Hunt_Button = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        File_Filter_TextArea = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        Domain_Filter_TextArea = new javax.swing.JTextArea();
        File_Filter_CheckBox = new javax.swing.JCheckBox();
        Domain_Filter_CheckBox = new javax.swing.JCheckBox();

        jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        Hunter_CaseSensitive_CheckBox.setSelected(false);
        Hunter_CaseSensitive_CheckBox.setText("Case sensitive");
        Hunter_CaseSensitive_CheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Hunter_CaseSensitive_CheckBoxActionPerformed(evt);
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

        Hunter_Switch_CheckBox.setSelected(true);
        Hunter_Switch_CheckBox.setText("Hunter Switch");
        Hunter_Switch_CheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Hunter_Switch_CheckBoxActionPerformed(evt);
            }
        });

        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });
        jTabbedPane1.addTab("Request", jScrollPane2);
        jTabbedPane1.addTab("Response", jScrollPane3);

        jScrollPane1.setViewportView(jTabbedPane1);

        Request_Headers_CheckBox.setText("Headers");

        jLabel1.setText("Hunt Request:");

        Request_Body_CheckBox.setSelected(true);
        Request_Body_CheckBox.setText("Body");

        jLabel2.setText("Hunt Response:");

        Response_Headers_CheckBox.setText("Headers");

        Response_Body_CheckBox.setSelected(true);
        Response_Body_CheckBox.setText("Body");

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        All_Hunt_Button.setText("All Hunt");
        All_Hunt_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                All_Hunt_ButtonActionPerformed(evt);
            }
        });
        jPanel2.add(All_Hunt_Button);

        Default_Hunt_Button.setText("Default");
        Default_Hunt_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Default_Hunt_ButtonActionPerformed(evt);
            }
        });
        jPanel2.add(Default_Hunt_Button);

        File_Filter_TextArea.setColumns(20);
        File_Filter_TextArea.setRows(5);
        File_Filter_TextArea.setToolTipText("use (;) to split filename");
        jScrollPane4.setViewportView(File_Filter_TextArea);

        Domain_Filter_TextArea.setColumns(20);
        Domain_Filter_TextArea.setRows(5);
        Domain_Filter_TextArea.setToolTipText("use (;) to split domain");
        jScrollPane5.setViewportView(Domain_Filter_TextArea);

        File_Filter_CheckBox.setSelected(true);
        File_Filter_CheckBox.setText("File Filter");
        File_Filter_CheckBox.setMaximumSize(new java.awt.Dimension(140, 27));
        File_Filter_CheckBox.setMinimumSize(new java.awt.Dimension(140, 27));
        File_Filter_CheckBox.setPreferredSize(new java.awt.Dimension(140, 27));
        File_Filter_CheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                File_Filter_CheckBoxActionPerformed(evt);
            }
        });

        Domain_Filter_CheckBox.setText("Domain Filter");
        Domain_Filter_CheckBox.setMaximumSize(new java.awt.Dimension(140, 27));
        Domain_Filter_CheckBox.setMinimumSize(new java.awt.Dimension(140, 27));
        Domain_Filter_CheckBox.setPreferredSize(new java.awt.Dimension(140, 27));
        Domain_Filter_CheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Domain_Filter_CheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(Hunter_Add_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Hunter_Add_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(Hunter_Switch_CheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Hunter_CaseSensitive_CheckBox))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Hunter_Clear_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Hunter_Remove_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Hunter_Paste_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(Response_Headers_CheckBox))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(Request_Headers_CheckBox)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Request_Body_CheckBox, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(Response_Body_CheckBox, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addComponent(File_Filter_CheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Domain_Filter_CheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 966, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Hunter_Switch_CheckBox)
                            .addComponent(Hunter_CaseSensitive_CheckBox))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(Hunter_Paste_Button)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Hunter_Remove_Button)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Hunter_Clear_Button))
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Hunter_Add_Button)
                            .addComponent(Hunter_Add_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 6, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Request_Body_CheckBox)
                            .addComponent(Request_Headers_CheckBox)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(Response_Body_CheckBox)
                            .addComponent(Response_Headers_CheckBox))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(File_Filter_CheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Domain_Filter_CheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        jScrollPane6.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1354, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void Hunter_CaseSensitive_CheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Hunter_CaseSensitive_CheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Hunter_CaseSensitive_CheckBoxActionPerformed

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
        setData(getRequestObjectByRow(Hunter_Table,Hunter_Table.getSelectedRow()));
        tableAddRightClickActions(evt);
    }//GEN-LAST:event_Hunter_TableMouseClicked

    private void Hunter_ClearTable_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Hunter_ClearTable_ButtonActionPerformed
        DefaultTableModel tableModel = (DefaultTableModel)Hunter_Table.getModel();
        tableModel.setRowCount( 0 );
    }//GEN-LAST:event_Hunter_ClearTable_ButtonActionPerformed

    private void Hunter_SaveSettings_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Hunter_SaveSettings_ButtonActionPerformed
        callbacks.saveExtensionSetting("Hunter_CaseSensitive_CheckBox", Boolean.toString(Hunter_CaseSensitive_CheckBox.isSelected()));
        callbacks.saveExtensionSetting("Hunter_Keyword_List", String.join(",", getListString(Hunter_Keyword_List)));
        callbacks.saveExtensionSetting("Request_Body_CheckBox", Boolean.toString(Request_Body_CheckBox.isSelected()));
        callbacks.saveExtensionSetting("Request_Headers_CheckBox", Boolean.toString(Request_Headers_CheckBox.isSelected()));
        callbacks.saveExtensionSetting("Response_Body_CheckBox", Boolean.toString(Response_Body_CheckBox.isSelected()));
        callbacks.saveExtensionSetting("Response_Headers_CheckBox", Boolean.toString(Response_Headers_CheckBox.isSelected()));
        callbacks.saveExtensionSetting("File_Filter_CheckBox", Boolean.toString(File_Filter_CheckBox.isSelected()));
        callbacks.saveExtensionSetting("Domain_Filter_CheckBox", Boolean.toString(Domain_Filter_CheckBox.isSelected()));
        callbacks.saveExtensionSetting("File_Filter_TextArea", File_Filter_TextArea.getText());
        callbacks.saveExtensionSetting("Domain_Filter_TextArea", Domain_Filter_TextArea.getText());
    }//GEN-LAST:event_Hunter_SaveSettings_ButtonActionPerformed

    private void Hunter_Switch_CheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Hunter_Switch_CheckBoxActionPerformed
        if(Hunter_Switch_CheckBox.isSelected()){
            Hunter_Switch_CheckBox.setText("Hunter Enable");
            hunterSwitchOn(true);
        }
        else{
            Hunter_Switch_CheckBox.setText("Hunter Disable");
            hunterSwitchOn(false);
        }
    }//GEN-LAST:event_Hunter_Switch_CheckBoxActionPerformed

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked
        //        textAddRightClickActions(evt);
    }//GEN-LAST:event_jScrollPane2MouseClicked

    private void All_Hunt_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_All_Hunt_ButtonActionPerformed
        Request_Body_CheckBox.setSelected(true);
        Request_Headers_CheckBox.setSelected(true);
        Response_Body_CheckBox.setSelected(true);
        Response_Headers_CheckBox.setSelected(true);
    }//GEN-LAST:event_All_Hunt_ButtonActionPerformed

    private void Default_Hunt_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Default_Hunt_ButtonActionPerformed
        Request_Body_CheckBox.setSelected(true);
        Request_Headers_CheckBox.setSelected(false);
        Response_Body_CheckBox.setSelected(true);
        Response_Headers_CheckBox.setSelected(false);
    }//GEN-LAST:event_Default_Hunt_ButtonActionPerformed

    private void File_Filter_CheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_File_Filter_CheckBoxActionPerformed
        File_Filter_TextArea.setEnabled(File_Filter_CheckBox.isSelected());
    }//GEN-LAST:event_File_Filter_CheckBoxActionPerformed

    private void Domain_Filter_CheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Domain_Filter_CheckBoxActionPerformed
        Domain_Filter_TextArea.setEnabled(Domain_Filter_CheckBox.isSelected());
    }//GEN-LAST:event_Domain_Filter_CheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton All_Hunt_Button;
    private javax.swing.JButton Default_Hunt_Button;
    public javax.swing.JCheckBox Domain_Filter_CheckBox;
    public javax.swing.JTextArea Domain_Filter_TextArea;
    public javax.swing.JCheckBox File_Filter_CheckBox;
    public javax.swing.JTextArea File_Filter_TextArea;
    private javax.swing.JButton Hunter_Add_Button;
    private javax.swing.JTextField Hunter_Add_TextField;
    public javax.swing.JCheckBox Hunter_CaseSensitive_CheckBox;
    private javax.swing.JButton Hunter_ClearTable_Button;
    private javax.swing.JButton Hunter_Clear_Button;
    public javax.swing.JList<String> Hunter_Keyword_List;
    private javax.swing.JButton Hunter_Paste_Button;
    private javax.swing.JButton Hunter_Remove_Button;
    private javax.swing.JButton Hunter_SaveSettings_Button;
    public javax.swing.JCheckBox Hunter_Switch_CheckBox;
    public javax.swing.JTable Hunter_Table;
    public javax.swing.JCheckBox Request_Body_CheckBox;
    public javax.swing.JCheckBox Request_Headers_CheckBox;
    public javax.swing.JCheckBox Response_Body_CheckBox;
    public javax.swing.JCheckBox Response_Headers_CheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
