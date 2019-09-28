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

    public static String getBody(IExtensionHelpers helpers, IHttpRequestResponse messageInfo, boolean isRequest) {
        try {
            int BodyOffset;
            int body_length;
            String body;
            if(isRequest){
                byte[] request = messageInfo.getRequest();
                IRequestInfo analyzeRequest = helpers.analyzeRequest(request);
                BodyOffset = analyzeRequest.getBodyOffset();
                body_length = request.length - BodyOffset;
                body = new String(request, BodyOffset, body_length, "UTF-8");
            } else{
                if (messageInfo.getResponse() != null) {
                    byte[] response = messageInfo.getResponse();
                    IResponseInfo analyzeResponse = helpers.analyzeResponse(response);
                    BodyOffset = analyzeResponse.getBodyOffset();
                    body_length = response.length - BodyOffset;
                    body = new String(response, BodyOffset, body_length, "UTF-8");
                }else {
                    body = "";
                }

            }
            return body;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getHeader(IExtensionHelpers helpers, IHttpRequestResponse messageInfo, boolean isRequest){
        try {
            List<String> headers;
            if(isRequest){
                byte[] request = messageInfo.getRequest();
                IRequestInfo analyzeRequest = helpers.analyzeRequest(request);
                headers = analyzeRequest.getHeaders();
            } else{
                byte[] response = messageInfo.getResponse();
                IResponseInfo analyzeResponse = helpers.analyzeResponse(response);
                headers = analyzeResponse.getHeaders();
            }
            return String.join("\n",headers);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean findString(String messageString,String keyword){
        boolean caseDifference = KeywordHunter.getInstance().Hunter_CaseSensitive_CheckBox.isSelected();
        if(!caseDifference){
            messageString = messageString.toLowerCase();
            keyword = keyword.toLowerCase();
        }
        boolean isExist = messageString.contains(keyword);
        return isExist;
    }

    public static String keyFoundPlace(IExtensionHelpers helpers, IHttpRequestResponse messageInfo,String keyword){
        String foundPlace = "";
        boolean keyInResponse = false;
        boolean keyInRequest = false;

        boolean requestHeaders = KeywordHunter.getInstance().Request_Headers_CheckBox.isSelected();
        boolean requestBody = KeywordHunter.getInstance().Request_Body_CheckBox.isSelected();
        boolean responseHeaders = KeywordHunter.getInstance().Response_Headers_CheckBox.isSelected();
        boolean responseBody = KeywordHunter.getInstance().Response_Body_CheckBox.isSelected();

        if(requestHeaders&&requestBody){
            keyInRequest = findString(new String(messageInfo.getRequest()),keyword);
        }else if(requestHeaders){
            keyInRequest = findString(getHeader(helpers,messageInfo,true),keyword);
        }else if(requestBody){
            keyInRequest = findString(getBody(helpers,messageInfo,true),keyword);
        }

        if(responseHeaders && responseBody){
            if(messageInfo.getResponse() != null){ keyInResponse = findString(new String(messageInfo.getResponse()),keyword);}
        }else if(responseHeaders){
            keyInResponse = findString(getHeader(helpers,messageInfo,false),keyword);
        }else if(responseBody){
            keyInResponse = findString(getBody(helpers,messageInfo,false),keyword);
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

    public static String getFilename(IExtensionHelpers helpers, IHttpRequestResponse messageInfo){
        byte[] request = messageInfo.getRequest();
        IRequestInfo analyzeRequest = helpers.analyzeRequest(request);
        String url = analyzeRequest.getHeaders().get(0).split(" ")[1];
        String filename =url.substring(url.lastIndexOf('/')+1);
        if(filename.contains("?")){
            filename = filename.split("\\?")[0];
        }
        if(filename.contains(".")){
            filename = filename.substring(filename.lastIndexOf('.')+1);
        }
        return filename;
    }
}