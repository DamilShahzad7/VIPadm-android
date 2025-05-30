package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "ClientDetails",strict=false)
public class ClientDetails {

    @Element(name = "Column1",required=false)
    private String Column1;

    @Element(name = "COMPANY",required=false)
    private String COMPANY;
    @Element(name = "TCPIP",required=false)
    private String TCPIP;
    @Element(name = "CLIENTNAME",required=false)
    private String CLIENTNAME;
    @Element(name = "MOBILE",required=false)
    private String MOBILE;
    @Element(name = "EXTERNALIP",required=false)
    private boolean EXTERNALIP;
    @Element(name = "P_CLIENTID",required=false)
    private String P_CLIENTID;
    @Element(name = "URL",required=false)
    private String URL;
    @Element(name = "HTTPS",required=false)
    private boolean HTTPS;

    public String getColumn1() {
        return Column1;
    }

    public void setColumn1(String column1) {
        Column1 = column1;
    }

    public boolean isEXTERNALIP() {
        return EXTERNALIP;
    }

    public boolean isHTTPS() {
        return HTTPS;
    }

    public String getCOMPANY() {
        return COMPANY;
    }

    public void setCOMPANY(String COMPANY) {
        this.COMPANY = COMPANY;
    }

    public String getTCPIP() {
        return TCPIP;
    }

    public void setTCPIP(String TCPIP) {
        this.TCPIP = TCPIP;
    }

    public String getCLIENTNAME() {
        return CLIENTNAME;
    }

    public void setCLIENTNAME(String CLIENTNAME) {
        this.CLIENTNAME = CLIENTNAME;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }



    public void setEXTERNALIP(boolean EXTERNALIP) {
        this.EXTERNALIP = EXTERNALIP;
    }

    public String getP_CLIENTID() {
        return P_CLIENTID;
    }

    public void setP_CLIENTID(String P_CLIENTID) {
        this.P_CLIENTID = P_CLIENTID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }


    public void setHTTPS(boolean HTTPS) {
        this.HTTPS = HTTPS;
    }

    @Override
    public String toString() {
        return "ClassPojo [COMPANY = " + COMPANY + ", TCPIP = " + TCPIP + ", CLIENTNAME = " + CLIENTNAME + ", MOBILE = " + MOBILE + ", EXTERNALIP = " + EXTERNALIP + ", P_CLIENTID = " + P_CLIENTID + ", URL = " + URL + ", HTTPS = " + HTTPS + "]";
    }
}