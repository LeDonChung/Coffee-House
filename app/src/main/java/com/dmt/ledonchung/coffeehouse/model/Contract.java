package com.dmt.ledonchung.coffeehouse.model;

import java.io.Serializable;

public class Contract implements Serializable {
    private String method;
    private String infor;
    private int icon;

    public Contract(String method, String infor, int icon) {
        this.method = method;
        this.infor = infor;
        this.icon = icon;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getInfor() {
        return infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
