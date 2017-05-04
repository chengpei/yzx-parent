package com.whpe.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient {
    protected Log logger = LogFactory.getLog(this.getClass());

    private String ip;

    private int port;

    private Socket socket;

    public SocketClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            socket = new Socket();
            socket.setSoTimeout(3000);
            socket.connect(new InetSocketAddress(InetAddress.getByName(ip), port), 3000);
        }catch (Exception e){
            logger.error(ip+":"+port+" 连接失败！", e);
        }
    }

    /**
     * 发送数据
     * @param message
     * @return
     */
    public byte[] sendMessage(byte[] message) {
        if(!socket.isConnected()){
            logger.error("连接尚未成功！！！");
        }
        try {
            DataOutputStream doc = new DataOutputStream(socket.getOutputStream());
            doc.write(message);
            doc.flush();

            DataInputStream in = new DataInputStream(socket.getInputStream());
            int itime = 0;
            byte[] byteMac = null;
            do {
                Thread.sleep(10);
                itime++;
                byteMac = new byte[in.available()];

            } while (byteMac.length == 0 && itime < 500);

            in.read(byteMac, 0, byteMac.length);
            return byteMac;
        }catch (Exception e){
            logger.error("数据发送失败！", e);
            return null;
        }
    }

    public void close(){
        if(socket.isConnected()){
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("socket连接关闭失败！", e);
            }
        }
    }

    public boolean isConnected(){
        return socket != null && socket.isConnected();
    }

}
