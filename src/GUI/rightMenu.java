package GUI;

import burp.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class rightMenu implements IContextMenuFactory {
    private IBurpExtenderCallbacks callbacks;
    private final IExtensionHelpers helpers;
    private PrintWriter stdout;
    private PrintWriter stderr;

    public rightMenu(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        this.helpers = callbacks.getHelpers();
        this.stdout = new PrintWriter(callbacks.getStdout(),true);
        this.stderr = new PrintWriter(callbacks.getStderr(),true);
    }

    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation)
    {
        List<JMenuItem> menus = new ArrayList();

        JMenu toolMenu = new JMenu("Keyword Hunter");
        JMenuItem switchOn = new JMenuItem("Switch On");
        JMenuItem switchOff = new JMenuItem("Switch Off");

        toolMenu.add(switchOn);
        toolMenu.add(switchOff);

        switchOn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent argv) {
                KeywordHunter.getInstance().Hunter_Switch_CheckBox.setSelected(true);
                KeywordHunter.getInstance().Hunter_Switch_CheckBox.setText("Hunter Enable");
            }
        });

        switchOff.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent argv) {
                KeywordHunter.getInstance().Hunter_Switch_CheckBox.setSelected(false);
                KeywordHunter.getInstance().Hunter_Switch_CheckBox.setText("Hunter Disable");
            }
        });

        menus.add(toolMenu);
        return menus;
    }
}