package com.sorum.sorum;

import java.io.Serializable;

public class Post implements Serializable {
    private String soruId;
    private String soru;
    private String cevapA;
    private String cevapB;
    private String cevapC;
    private String cevapD;
    private String cevapE;
    private String dogruCevap;
    private String desp;

    public Post(String soruId,String desp, String soru, String cevapA, String cevapB, String cevapC, String cevapD, String cevapE, String dogruCevap) {
        this.soruId = soruId;
        this.soru = soru;
        this.cevapA = cevapA;
        this.cevapB = cevapB;
        this.cevapC = cevapC;
        this.cevapD = cevapD;
        this.cevapE = cevapE;
        this.dogruCevap = dogruCevap;
        this.desp = desp;
    }

    public String getSoruId() {
        return soruId;
    }

    public void setSoruId(String soruId) {
        this.soruId = soruId;
    }

    public String getSoru() {
        return soru;
    }

    public void setSoru(String soru) {
        this.soru = soru;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getCevapA() {
        return cevapA;
    }

    public void setCevapA(String cevapA) {
        this.cevapA = cevapA;
    }

    public String getCevapB() {
        return cevapB;
    }

    public void setCevapB(String cevapB) {
        this.cevapB = cevapB;
    }

    public String getCevapC() {
        return cevapC;
    }

    public void setCevapC(String cevapC) {
        this.cevapC = cevapC;
    }

    public String getCevapD() {
        return cevapD;
    }

    public void setCevapD(String cevapD) {
        this.cevapD = cevapD;
    }

    public String getCevapE() {
        return cevapE;
    }

    public void setCevapE(String cevapE) {
        this.cevapE = cevapE;
    }

    public String getDogruCevap() {
        return dogruCevap;
    }

    public void setDogruCevap(String dogruCevap) {
        this.dogruCevap = dogruCevap;
    }
}