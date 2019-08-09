/*
Great Thanks: https://github.com/portswigger/authz
 */


package common;

import burp.*;
import GUI.KeywordHunter;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Methods {
    public static String findHeader(List<String> headers, String name){
        ListIterator<String> iter = headers.listIterator();
        String value = "";
        boolean inHeader = false;

        while (iter.hasNext()) {
            String iterText = ((String) iter.next());
            if (iterText.contains(name)) {
                value = iterText.replace(name+":","").replace(" ","");
                inHeader = true;
                break;
            }
        }
        if (!inHeader) {
            value = "N/A";
        }
        return value;
    }

    public static boolean findStringFromMessageByte(byte[] messageByte,String keyword){
        String reqString = new String(messageByte);
        boolean caseDifference = KeywordHunter.getInstance().Hunter_CaseSensitive_CheckBox.isSelected();
        if(!caseDifference){
            reqString = reqString.toLowerCase();
            keyword = keyword.toLowerCase();
        }
        boolean isExist = reqString.contains(keyword);
        return isExist;
    }

    public static String keyFoundPlace(IHttpRequestResponse messageInfo,String keyword){
        String foundPlace = "";
        boolean keyInResponse = false;
        boolean keyInRequest = false;
        boolean requestOnly = KeywordHunter.getInstance().Hunter_Request_RadioButton.isSelected();;
        boolean responseOnly = KeywordHunter.getInstance().Hunter_Response_RadioButton.isSelected();
        boolean huntBoth = KeywordHunter.getInstance().Hunter_Both_RadioButton.isSelected();

        if(huntBoth){
            if(messageInfo.getResponse() != null){ keyInResponse = findStringFromMessageByte(messageInfo.getResponse(),keyword); }
            keyInRequest = findStringFromMessageByte(messageInfo.getRequest(),keyword);
        }else if(requestOnly){
            keyInRequest = findStringFromMessageByte(messageInfo.getRequest(),keyword);
        }else if(responseOnly){
            if(messageInfo.getResponse() != null){ keyInResponse = findStringFromMessageByte(messageInfo.getResponse(),keyword); }
        }

        if(keyInRequest){ foundPlace = "Request"; }
        else if(keyInResponse){ foundPlace = "Response"; }
        if(keyInRequest&&keyInResponse){ foundPlace = "Both"; }
        return foundPlace;
    }

    public static int getResponseBodyLength(IResponseInfo analyzeResponse, byte[] response) {
        for (String header: analyzeResponse.getHeaders()) {
            if (header.toLowerCase().startsWith("content-length:")) {
                return Integer.parseInt(header.substring(header.indexOf(":") + 1).trim());
            }
        }
        // if no content-length header returned, let's calculate it manually
        String resp = new String(response);
        String body = resp.substring(analyzeResponse.getBodyOffset());
        return body.length();
    }

    public static void sendRequestResponseToRepeater(IBurpExtenderCallbacks callback, IHttpRequestResponse req){
        callback.sendToRepeater(req.getHttpService().getHost(), req.getHttpService().getPort(), req.getHttpService().getProtocol().equalsIgnoreCase("https"), req.getRequest(), null);
    }

    public static String[] getListString(javax.swing.JList list){
        int listLength = list.getModel().getSize();
        String[] strings = new String[listLength];
        for(int i = 0; i< listLength;i++){
            strings[i] = list.getModel().getElementAt(i).toString();
        }
        return strings;
    }

    public static String[] insertString(String[] arr, String str) {
        int size = arr.length;
        String[] tmp = new String[size + 1];
        for (int i = 0; i < size; i++){
            tmp[i] = arr[i];
        }
        tmp[size] = str;
        return tmp;
    }

    public static String[] removeString(String[] arr,int index) {
        List<String> list1=Arrays.asList(arr);
        List<String> arrList = new ArrayList<String>(list1); //
        arrList.remove(index);
        arr = arrList.toArray(new String[arrList.size()]);
        return arr;
    }

    public static String getClipboardContents() {
        //Get clipboard contents for implement grep and match paste button
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);

        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static  String[] unionString(String[] arr1,String[] arr2){
        int a = arr1.length;
        int b = arr2.length;
        arr1 = Arrays.copyOf(arr1,a+b);
        System.arraycopy(arr2,0,arr1,a,b);
        return arr1;
    }
}