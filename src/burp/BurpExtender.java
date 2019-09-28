package burp;

import GUI.KeywordHunter;
import GUI.rightMenu;

import java.io.PrintWriter;
import javax.swing.*;
import java.awt.Component;

public class BurpExtender implements IBurpExtender,ITab,IExtensionStateListener {
    public static IBurpExtenderCallbacks callbacks;
    public IExtensionHelpers helpers;
    public String extensionName = "Keyword Hunter";
    public String version ="v0.2.1";
    public PrintWriter stdout;
    public PrintWriter stderr;
    private KeywordHunter mainPane;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        this.callbacks = callbacks;
        this.helpers = callbacks.getHelpers();
        callbacks.setExtensionName(extensionName);
        callbacks.registerContextMenuFactory(new rightMenu(callbacks));
        callbacks.registerHttpListener(new httpListener(callbacks));

        stdout = new PrintWriter(callbacks.getStdout(),true);
        stderr = new PrintWriter(callbacks.getStderr(),true);


        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    mainPane = KeywordHunter.getInstance();
                    mainPane.initTab(callbacks);
                    callbacks.addSuiteTab(BurpExtender.this);

                    stdout.println(String.format("- Successfully Loaded %s %s",extensionName,version));
                    stdout.println("- For bugs please on the official github: https://github.com/wooyin/KeywordHunter/");
                }
            });
            this.helpers = callbacks.getHelpers();
        } catch(Exception e) {
            stderr.println(e);
        }
    }

    @Override
    public void extensionUnloaded() { callbacks.printOutput(String.format("- %s extension was unloaded",extensionName)); }

    @Override
    public String getTabCaption()
    {
        return "Keyword Hunter";
    }

    @Override
    public Component getUiComponent()
    {
        return mainPane;
    }

}