package pl.itger.dualsimcard;

/**
 * Created by piotrz on 7/23/15.
 * Copyright 2015 Piotr Zerynger ITger
 */

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static pl.itger.dualsimcard.MainActivity.getMActContext;

public final class TelInfo {

    private static TelInfo telInf;
    protected Sci[] scitems;
    ArrayList<Sci> scitemsArr;
    //private String imsiSIM1;
    private String imsiSIM2;
    private boolean isSIM1Ready;
    private boolean isSIM2Ready;
    private String sim1_STATE;
    private String sim2_STATE;
    // Integrated circuit card identifier (ICCID)
    private String sim1_ICCID;
    private String sim2_ICCID;
    // International mobile subscriber identity (IMSI)
    private String sim1_IMSI;
    private String sim2_IMSI;
    // Service provider name (SPN)
    private String sim1_SPN;
    private String sim2_SPN;
    // Mobile country code (MCC)
    private String sim1_MCC;
    private String sim2_MCC;
    // Mobile network code (MNC)
    private String sim1_MNC;
    private String sim2_MNC;

    //private NeighboringCellInfo nci;

    //private  List<NeighboringCellInfo> sim1_NC;
    //private  List<NeighboringCellInfo> sim2_NC;
    private String sim1_MCC_MNC;
    private String sim2_MCC_MNC;
    // Mobile subscriber identification number (MSIN)
    // Mobile station international subscriber directory number (MSISDN)
    private String sim1_MSISDN;
    private String sim2_MSISDN;


    // Abbreviated dialing numbers (ADN)
    // Last dialed numbers (LDN)
    // Short message service (SMS)
    // Language preference (LP)
    // Card holder verification (CHV1 and CHV2)
    // Ciphering key (Kc)
    // Ciphering key sequence number
    // Emergency call code
    // Fixed dialing numbers (FDN)
    //getDeviceId() Returns the unique device ID, for example, the IMEI for GSM and the MEID or ESN for CDMA phones.
    private String sim1_IMEI;
    private String sim2_IMEI;
    // Local area identity (LAI)
    private String sim1_LAI;
    private String sim2_LAI;
    // Location Area Code (LAC).
    private String sim1_LAC;
    private String sim2_LAC;


    // Own dialing number
    // Temporary mobile subscriber identity (TMSI)
    // Routing area identifier (RIA) network code
    // Service dialing numbers (SDNs)
    // CellID
    private String sim1_CellID;
    private String sim2_CellID;
    private List<CellInfo> all;


    private TelInfo() {
    }

    /*@
   @requires context != null;
   @ensures TelInfo != null;
      @*/
    public static TelInfo getInstance() {//Context context) {
        //if (TelInfo.telInf != null) {
        TelInfo.telInf = null;
        TelInfo.telInf = new TelInfo();
        //}
        Context context = getMActContext();
        //printTelephonyManagerMethodNamesForThisDevice(context);
        //System.out.println("1 " + context);

        TelephonyManager telMngr = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        //System.out.println("2 " + telMngr);

        //telInf.nci = new NeighboringCellInfo(NeighboringCellInfo.UNKNOWN_RSSI, new GsmCellLocation().toString(), TelephonyManager.NETWORK_TYPE_HSPA);
        //System.out.println(telInf.nci.toString());


        telInf.sim1_ICCID = telMngr.getSimSerialNumber();
        telInf.sim1_IMSI = telMngr.getSubscriberId();
        telInf.sim1_SPN = telMngr.getSimOperatorName();
        telInf.sim1_MCC = telMngr.getNetworkCountryIso();
        telInf.sim1_MNC = telMngr.getNetworkOperatorName();
        telInf.sim1_MCC_MNC = telMngr.getSimOperator();
        telInf.sim1_MSISDN = telMngr.getLine1Number();
        telInf.sim1_IMEI = telMngr.getDeviceId();
        //telInf.sim1_NC = telMngr.getNeighboringCellInfo();

        if (telMngr.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
            final GsmCellLocation location = (GsmCellLocation) telMngr.getCellLocation();
            if (location != null) {
                telInf.sim1_LAC = Integer.toString(location.getLac());
                telInf.sim1_CellID = Integer.toString(location.getCid());
            }
        }

        telInf.imsiSIM2 = null;

        telInf.isSIM1Ready = telMngr.getSimState() == TelephonyManager.SIM_STATE_READY;
        telInf.isSIM2Ready = false;

        telInf.sim1_STATE = simState(telMngr.getSimState());
        try {
            //telInf.isSIM1Ready = getSIMStateBySlot(context, "getSimState", 0);
            telInf.isSIM2Ready = getSIMStateBySlot(context, "getSimState", 1);
            //System.out.println("!!");
        } catch (ITgerMethodNotFoundException e1) {
            //Call here for next manufacturer's predicted method name if you wish
            e1.printStackTrace();
        }

        if (telInf.isSIM2Ready) {
            try {
                // telInf.sim1_IMSI = getDeviceIdBySlot(context, "getDeviceId", 0);
                telInf.imsiSIM2 = getDeviceIdBySlot(context, "getDeviceId", 1);
                telInf.sim2_ICCID = getDeviceIdBySlot(context, "getSimSerialNumber", 1);
                telInf.sim2_IMSI = getDeviceIdBySlot(context, "getSubscriberId", 1);
                telInf.sim2_SPN = getDeviceIdBySlot(context, "getSimOperatorName", 1);
                telInf.sim2_MCC = getDeviceIdBySlot(context, "getNetworkCountryIso", 1);
                telInf.sim2_MNC = getDeviceIdBySlot(context, "getNetworkOperatorName", 1);
                telInf.sim2_MCC_MNC = getDeviceIdBySlot(context, "getSimOperator", 1);
                telInf.sim2_MSISDN = getDeviceIdBySlot(context, "getLine1Number", 1);
                telInf.sim2_IMEI = getDeviceIdBySlot(context, "getDeviceId", 1);

                if (telMngr.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
                    final GsmCellLocation location = getCellLocBySlot(context, "getCellLocation", 1);// telMngr.getCellLocation();
                    if (location != null) {
                        telInf.sim2_LAC = Integer.toString(location.getLac());
                        telInf.sim2_CellID = Integer.toString(location.getCid());
                    }
                }

                System.out.println("!");
            } catch (ITgerMethodNotFoundException e1) {
                //Call here for next manufacturer's predicted method name if you wish
                e1.printStackTrace();
            }
        }

        telInf.scitemsArr = new ArrayList<Sci>();

        telInf.scitemsArr.add(new Sci("SIM 1 state", telInf.sim1_STATE));
        telInf.scitemsArr.add(new Sci("Integrated circuit card identifier (ICCID)", telInf.sim1_ICCID));
        telInf.scitemsArr.add(new Sci("Unique device ID (IMEI or MEID/ESN for CDMA)", telInf.sim1_IMEI));
        telInf.scitemsArr.add(new Sci("International mobile subscriber identity (IMSI)", telInf.sim1_IMSI));
        telInf.scitemsArr.add(new Sci("Service provider name (SPN)", telInf.sim1_SPN));
        telInf.scitemsArr.add(new Sci("Mobile country code (MCC)", telInf.sim1_MCC));
        telInf.scitemsArr.add(new Sci("Mobile operator name", telInf.sim1_MNC));
        telInf.scitemsArr.add(new Sci("Mobile country code + mobile network code (MCC+MNC)", telInf.sim1_MCC_MNC));
        telInf.scitemsArr.add(new Sci("Mobile station international subscriber directory number (MSISDN)", telInf.sim1_MSISDN));
        telInf.scitemsArr.add(new Sci("Location Area Code (LAC)", telInf.sim1_LAC));
        telInf.scitemsArr.add(new Sci("Cell Tower ID (CID)", telInf.sim1_CellID));

        if (telInf.isSIM2Ready) {
            telInf.scitemsArr.add(new Sci(" ", " "));
            telInf.scitemsArr.add(new Sci("SIM 2 state", telInf.sim2_STATE));
            telInf.scitemsArr.add(new Sci("Integrated circuit card identifier (ICCID)", telInf.sim2_ICCID));
            telInf.scitemsArr.add(new Sci("Unique device ID (IMEI or MEID/ESN for CDMA)", telInf.sim2_IMEI));
            telInf.scitemsArr.add(new Sci("International mobile subscriber identity (IMSI)", telInf.sim2_IMSI));
            telInf.scitemsArr.add(new Sci("Service provider name (SPN)", telInf.sim2_SPN));
            telInf.scitemsArr.add(new Sci("Mobile country code (MCC)", telInf.sim2_MCC));
            telInf.scitemsArr.add(new Sci("Mobile operator name", telInf.sim2_MNC));
            telInf.scitemsArr.add(new Sci("Mobile country code + mobile network code (MCC+MNC)", telInf.sim2_MCC_MNC));
            telInf.scitemsArr.add(new Sci("Mobile station international subscriber directory number (MSISDN)", telInf.sim2_MSISDN));
            telInf.scitemsArr.add(new Sci("Location Area Code (LAC)", telInf.sim2_LAC));
            telInf.scitemsArr.add(new Sci("Cell Tower ID (CID)", telInf.sim2_CellID));


            //telInf.scitemsArr.add(new Sci("NC (Neighboring Cell ", telInf.sim1_NC.toString() ));

        }

        return telInf;
    }

    private static String simState(int simState) {
        switch (simState) {
            case 0:
                return "UNKNOWN";
            case 1:
                return "ABSENT";
            case 2:
                return "REQUIRED";
            case 3:
                return "PUK_REQUIRED";
            case 4:
                return "NETWORK_LOCKED";
            case 5:
                return "READY";
            case 6:
                return "NOT_READY";
            case 7:
                return "PERM_DISABLED";
            case 8:
                return "CARD_IO_ERROR";
        }
        return "??? " + simState;
    }

    private static String getDeviceIdBySlot(Context context, String predictedMethodName, int slotID) throws ITgerMethodNotFoundException {

        String imsi = null;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);

            if (ob_phone != null) {
                imsi = ob_phone.toString();

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ITgerMethodNotFoundException(predictedMethodName);
        }

        return imsi;
    }

    private static GsmCellLocation getCellLocBySlot(Context context, String predictedMethodName, int slotID) throws ITgerMethodNotFoundException {

        GsmCellLocation cloc = null;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);

            if (ob_phone != null) {
                cloc = (GsmCellLocation) ob_phone;

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ITgerMethodNotFoundException(predictedMethodName);
        }

        return cloc;
    }

    private static boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) throws ITgerMethodNotFoundException {
        boolean isReady = false;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimState = telephonyClass.getMethod(predictedMethodName, parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimState.invoke(telephony, obParameter);

            if (ob_phone != null) {
                int simState = Integer.parseInt(ob_phone.toString());
                telInf.sim2_STATE = simState(simState);
                if ((simState != TelephonyManager.SIM_STATE_ABSENT) && (simState != TelephonyManager.SIM_STATE_UNKNOWN)) {
                    isReady = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ITgerMethodNotFoundException(predictedMethodName);
        }

        return isReady;
    }

    public static void printTelephonyManagerMethodNamesForThisDevice(Context context) {

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class<?> telephonyClass;
        try {
            System.out.println("-------------------------------");
            telephonyClass = Class.forName(telephony.getClass().getName());
            Method[] methods = telephonyClass.getMethods();

            for (Method method : methods) {
                // if (methods[idx].getName().startsWith("get")) {
                System.out.println("\n" + method.getName() + " declared by " + method.getDeclaringClass());//+ "  superclass " + methods[idx].getDeclaringClass().getSuperclass().getName());
                //}
            }
            System.out.println("-------------------------------");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isSIM1Ready() {
        return isSIM1Ready;
    }

    public boolean isSIM2Ready() {
        return isSIM2Ready;
    }

    private boolean isDualSIM() {
        return imsiSIM2 != null;
    }

    @Override
    public String toString() {
        return "XXXXXXXXXXXXXXXX TelInfo{" +
                "imsiSIM1='" + sim1_IMSI + '\'' +
                ", imsiSIM2='" + imsiSIM2 + '\'' +
                ", isSIM1Ready=" + isSIM1Ready +
                ", isSIM2Ready=" + isSIM2Ready +
                '}';
    }

    private static class ITgerMethodNotFoundException extends Exception {
        private static final long serialVersionUID = -996812356902545308L;

        public ITgerMethodNotFoundException(String info) {
            super(info);
        }

    }
}
