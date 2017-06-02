package com.whpe.services;

import com.whpe.bean.InitializeForLoadBean;
import com.whpe.bean.NfcCardRecharge;

public interface RechargeService {
    boolean checkMac1(NfcCardRecharge nfcCardRecharge, InitializeForLoadBean data8050Bean);

    String calculateMac2(NfcCardRecharge nfcCardRecharge, InitializeForLoadBean data8050Bean);

    String buildCreditForLoad(String mac2);

    String calculateMac(String cardNo, String random, String apdu);
}
