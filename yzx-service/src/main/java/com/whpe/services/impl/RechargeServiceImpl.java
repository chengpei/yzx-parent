package com.whpe.services.impl;

import com.whpe.bean.InitializeForLoadBean;
import com.whpe.bean.NfcCardRecharge;
import com.whpe.services.CommonService;
import com.whpe.services.RechargeService;
import com.whpe.utils.DateUtils;
import com.whpe.utils.SocketClient;
import com.whpe.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RechargeServiceImpl extends CommonService implements RechargeService{

    @Value("${jmj_ip1}")
    private String jmjIp1;

    @Value("${jmj_port1}")
    private String jmjPort1;

    @Value("${jmj_ip2}")
    private String jmjIp2;

    @Value("${jmj_port1}")
    private String jmjPort2;

    @Override
    public boolean checkMac1(NfcCardRecharge nfcCardRecharge, InitializeForLoadBean data8050Bean) {
        long rechargeMoney = Long.parseLong(nfcCardRecharge.getOrdermount()); // 单位为 分，十进制
        String strMessage = "87654321"+"52"+"0"+"00101"+"1"
                + nfcCardRecharge.getCardno()
                + data8050Bean.getRandom()
                + data8050Bean.getTradeSeq()
                + "8000"
                + data8050Bean.getMac1()
                + "023" + "0000000000000000"
                + data8050Bean.getOldBalance() + String.format("%08X", rechargeMoney) + "02"
                + nfcCardRecharge.getImei();

        strMessage = String.format("%04X", strMessage.length()) + StringUtils.strToHex(strMessage, false);
        byte[] result = sendByteMessageToJMJ(StringUtils.hexString2Bytes(strMessage, false));
        if(result[10]!='5'||result[11]!='3'||result[12]!='0'||result[13]!='0'){
            throw new RuntimeException("mac1验证失败！"
                    + StringUtils.hexToStr(StringUtils.bytes2HexString(result, false)).substring(10));
        }
        return true;
    }

    @Override
    public String calculateMac2(NfcCardRecharge nfcCardRecharge, InitializeForLoadBean data8050Bean) {
        long rechargeMoney = Long.parseLong(nfcCardRecharge.getOrdermount()); // 单位为 分，十进制
        String tradeTime = DateUtils.getFormatDate(new Date(), "yyyyMMddHHmmss");
        String strMessage = "87654321"+"50"+"0"+"00101"+"1"
                + nfcCardRecharge.getCardno()
                + data8050Bean.getRandom()
                + data8050Bean.getTradeSeq()
                + "8000"
                + "026"+"0000000000000000"
                + String.format("%08X", rechargeMoney) + "02"
                + nfcCardRecharge.getImei()
                + tradeTime;

        strMessage = String.format("%04X", strMessage.length())+ StringUtils.strToHex(strMessage, false);

        byte[] result = sendByteMessageToJMJ(StringUtils.hexString2Bytes(strMessage, false));
        if(result[2+8]!='5' && result[3+8]!='1' && result[4+8]!='0'&& result[5+8]!='0'){
            throw new RuntimeException("mac2生成失败！"
                    + StringUtils.hexToStr(StringUtils.bytes2HexString(result, false)).substring(10));
        }
        String mac2 = StringUtils.hexToStr(StringUtils.bytes2HexString(result, false).substring(12 + 8 + 8));
        return mac2;
    }

    /**
     * 构造圈存指令，内容参考FMCOS技术手册 85页 8.7.2.2
     * @param mac2
     * @return
     */
    @Override
    public String buildCreditForLoad(String mac2) {
        String yyyyMMddHHmmss = DateUtils.getFormatDate(new Date(), "yyyyMMddHHmmss");
        return "805200000B" + yyyyMMddHHmmss + mac2 + "04";
    }

    /**
     * 计算修改15文件的Mac
     *
     * @param cardNo
     * @param newEffectiveDate
     * @param random
     * @return
     */
    @Override
    public String calculateYearCardRenewMac(String cardNo, String newEffectiveDate, String random) {
        String macDate = random + "0000000004D6951808" + newEffectiveDate; // 计算mac的数据
        String strMessage = "87654321" + "50" + "0" + "0" + "0210" + "0"
                + cardNo
                + String.format("%03d", macDate.length() / 2)
                + macDate;
        strMessage = String.format("%04X", strMessage.length()) + StringUtils.strToHex(strMessage, false);
        byte[] result = sendByteMessageToJMJ(StringUtils.hexString2Bytes(strMessage, false));
        if(result[2+8]!='5' && result[3+8]!='1' && result[4+8]!='0'&& result[5+8]!='0'){
            throw new RuntimeException("mac计算失败！"
                    + StringUtils.hexToStr(StringUtils.bytes2HexString(result, false)).substring(10));
        }
        String mac = StringUtils.hexToStr(StringUtils.bytes2HexString(result, false).substring(12 + 8 + 8));
        return mac;
    }

    /**
     * 发送二进制数据给加密机
     * @param bytes
     * @return
     */
    private byte[] sendByteMessageToJMJ(byte[] bytes) {
        SocketClient socketClient = null;
        try {
            socketClient = new SocketClient(jmjIp1, Integer.parseInt(jmjPort1));
            if(!socketClient.isConnected()){
                socketClient = new SocketClient(jmjIp2, Integer.parseInt(jmjPort2));
            }
            if(!socketClient.isConnected()){
                throw new RuntimeException("加密机连接失败！！！");
            }
            logger.info("发送给加密机的数据 ==== " + StringUtils.bytes2HexString(bytes, false));
            byte[] resultBytes = socketClient.sendMessage(bytes);
            logger.info("加密机返回的数据 ==== " + StringUtils.bytes2HexString(resultBytes, false));
            return resultBytes;
        }catch (Exception e){
            throw e;
        }finally {
            if(socketClient != null){
                socketClient.close();
            }
        }
    }
}
