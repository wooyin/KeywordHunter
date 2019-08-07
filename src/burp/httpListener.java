package burp;

import GUI.KeywordHunter;
import javax.swing.table.DefaultTableModel;
import java.io.PrintWriter;

import static common.Methods.*;

public class httpListener implements IHttpListener {
    private IBurpExtenderCallbacks callbacks;
    private final IExtensionHelpers helpers;
    private PrintWriter stdout;
    private PrintWriter stderr;

    public httpListener(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        this.helpers = callbacks.getHelpers();
        this.stdout = new PrintWriter(callbacks.getStdout(),true);
        this.stderr = new PrintWriter(callbacks.getStderr(),true);
    }

    @Override
    public void processHttpMessage (int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo)
    {
        if((KeywordHunter.getInstance().Hunter_Switch_CheckBox.isSelected())&&(toolFlag==4)){
            DefaultTableModel tableModel = (DefaultTableModel)(KeywordHunter.getInstance().Hunter_Table).getModel();
            addHunterTable(tableModel,messageInfo);
        }
    }

    private int reqIdx = 0;
    public void addHunterTable(DefaultTableModel requestTableModel,IHttpRequestResponse messageInfo){
        IRequestInfo analyzeRequest = helpers.analyzeRequest(messageInfo);
        String[] keywordStrings =  getListString(KeywordHunter.getInstance().Hunter_Keyword_List);
        for(String keyword: keywordStrings) {
            String foundPlace = keyFoundPlace(messageInfo, keyword);
            if (foundPlace != "") {
                String statusCode;
                String responseLength;
                if (messageInfo.getResponse() != null) {
                    statusCode = Short.toString(helpers.analyzeResponse(messageInfo.getResponse()).getStatusCode());
                    responseLength = Integer.toString(getResponseBodyLength(helpers.analyzeResponse(messageInfo.getResponse()), messageInfo.getResponse()));
                } else {
                    statusCode = "N/A";
                    responseLength = "N/A";
                }
                requestTableModel.addRow(new Object[]{
                        ++reqIdx,
                        findHeader(analyzeRequest.getHeaders(), "Host"),
                        analyzeRequest.getMethod(),
                        analyzeRequest.getUrl(),
                        keyword,
                        foundPlace,
                        statusCode,
                        responseLength,
                        messageInfo
                });
            }
        }
    }
}
