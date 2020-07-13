package com.guoliang.module.main.storage.table;

public class DeviceElectricInfo {

    private String devId;

    private String devName;

    private float elecA;

    private float elecB;

    private float elecC;

    private float elecE;

    private float elecN;

    public DeviceElectricInfo(String devId, String devName, float elecA, float elecB, float elecC, float elecE, float elecN) {
        this.devId = devId;
        this.devName = devName;
        this.elecA = elecA;
        this.elecB = elecB;
        this.elecC = elecC;
        this.elecE = elecE;
        this.elecN = elecN;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public float getElecA() {
        return elecA;
    }

    public void setElecA(float elecA) {
        this.elecA = elecA;
    }

    public float getElecB() {
        return elecB;
    }

    public void setElecB(float elecB) {
        this.elecB = elecB;
    }

    public float getElecC() {
        return elecC;
    }

    public void setElecC(float elecC) {
        this.elecC = elecC;
    }

    public float getElecE() {
        return elecE;
    }

    public void setElecE(float elecE) {
        this.elecE = elecE;
    }

    public float getElecN() {
        return elecN;
    }

    public void setElecN(float elecN) {
        this.elecN = elecN;
    }
}
