package org.apache.camel.learn;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ",")
public class Persona {
    @DataField(pos = 1)
    private int ID;
    @DataField(pos = 2)
    private int LIMIT_BAL;
    @DataField(pos = 3)
    private int SEX;
    @DataField(pos = 4)
    private int EDUCATION;
    @DataField(pos = 5)
    private int MARRIAGE;
    @DataField(pos = 6)
    private int AGE;
    @DataField(pos = 7)
    private int PAY_0;
    @DataField(pos = 8)
    private int PAY_2;
    @DataField(pos = 9)
    private int PAY_3;
    @DataField(pos = 10)
    private int PAY_4;
    @DataField(pos = 11)
    private int PAY_5;
    @DataField(pos = 12)
    private int PAY_6;
    @DataField(pos = 13)
    private int BILL_1;
    @DataField(pos = 14)
    private int BILL_2;
    @DataField(pos = 15)
    private int BILL_3;
    @DataField(pos = 16)
    private int BILL_4;
    @DataField(pos = 17)
    private int BILL_5;
    @DataField(pos = 18)
    private int BILL_6;
    @DataField(pos = 19)
    private int PAY_1;
    @DataField(pos = 20)
    private int PAY_22;
    @DataField(pos = 21)
    private int PAY_23;
    @DataField(pos = 22)
    private int PAY_24;
    @DataField(pos = 23)
    private int PAY_25;
    @DataField(pos = 24)
    private int PAY_26;
    @DataField(pos = 25)
    private int default_payment_next_month;

    @Override
    public String toString() {
        return "Persona{" +
                "ID=" + ID +
                ", LIMIT_BAL=" + LIMIT_BAL +
                ", SEX=" + SEX +
                ", EDUCATION=" + EDUCATION +
                ", MARRIAGE=" + MARRIAGE +
                ", AGE=" + AGE +
                ", PAY_0=" + PAY_0 +
                ", PAY_2=" + PAY_2 +
                ", PAY_3=" + PAY_3 +
                ", PAY_4=" + PAY_4 +
                ", PAY_5=" + PAY_5 +
                ", PAY_6=" + PAY_6 +
                ", BILL_1=" + BILL_1 +
                ", BILL_2=" + BILL_2 +
                ", BILL_3=" + BILL_3 +
                ", BILL_4=" + BILL_4 +
                ", BILL_5=" + BILL_5 +
                ", BILL_6=" + BILL_6 +
                ", PAY_1=" + PAY_1 +
                ", PAY_22=" + PAY_22 +
                ", PAY_23=" + PAY_23 +
                ", PAY_24=" + PAY_24 +
                ", PAY_25=" + PAY_25 +
                ", PAY_26=" + PAY_26 +
                ", default_payment_next_month=" + default_payment_next_month +
                '}';
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public int getLIMIT_BAL() {
        return LIMIT_BAL;
    }

    public void setLIMIT_BAL(int lIMIT_BAL) {
        LIMIT_BAL = lIMIT_BAL;
    }

    public int getSEX() {
        return SEX;
    }

    public void setSEX(int sEX) {
        SEX = sEX;
    }

    public int getEDUCATION() {
        return EDUCATION;
    }

    public void setEDUCATION(int eDUCATION) {
        EDUCATION = eDUCATION;
    }

    public int getMARRIAGE() {
        return MARRIAGE;
    }

    public void setMARRIAGE(int mARRIAGE) {
        MARRIAGE = mARRIAGE;
    }

    public int getAGE() {
        return AGE;
    }

    public void setAGE(int aGE) {
        AGE = aGE;
    }

    public int getPAY_0() {
        return PAY_0;
    }

    public void setPAY_0(int pAY_0) {
        PAY_0 = pAY_0;
    }

    public int getPAY_2() {
        return PAY_2;
    }

    public void setPAY_2(int pAY_2) {
        PAY_2 = pAY_2;
    }

    public int getPAY_3() {
        return PAY_3;
    }

    public void setPAY_3(int pAY_3) {
        PAY_3 = pAY_3;
    }

    public int getPAY_4() {
        return PAY_4;
    }

    public void setPAY_4(int pAY_4) {
        PAY_4 = pAY_4;
    }

    public int getPAY_5() {
        return PAY_5;
    }

    public void setPAY_5(int pAY_5) {
        PAY_5 = pAY_5;
    }

    public int getPAY_6() {
        return PAY_6;
    }

    public void setPAY_6(int pAY_6) {
        PAY_6 = pAY_6;
    }

    public int getBILL_1() {
        return BILL_1;
    }

    public void setBILL_1(int bILL_1) {
        BILL_1 = bILL_1;
    }

    public int getBILL_2() {
        return BILL_2;
    }

    public void setBILL_2(int bILL_2) {
        BILL_2 = bILL_2;
    }

    public int getBILL_3() {
        return BILL_3;
    }

    public void setBILL_3(int bILL_3) {
        BILL_3 = bILL_3;
    }

    public int getBILL_4() {
        return BILL_4;
    }

    public void setBILL_4(int bILL_4) {
        BILL_4 = bILL_4;
    }

    public int getBILL_5() {
        return BILL_5;
    }

    public void setBILL_5(int bILL_5) {
        BILL_5 = bILL_5;
    }

    public int getBILL_6() {
        return BILL_6;
    }

    public void setBILL_6(int bILL_6) {
        BILL_6 = bILL_6;
    }

    public int getPAY_1() {
        return PAY_1;
    }

    public void setPAY_1(int pAY_1) {
        PAY_1 = pAY_1;
    }

    public int getPAY_22() {
        return PAY_22;
    }

    public void setPAY_22(int pAY_22) {
        PAY_22 = pAY_22;
    }

    public int getPAY_23() {
        return PAY_23;
    }

    public void setPAY_23(int pAY_23) {
        PAY_23 = pAY_23;
    }

    public int getPAY_24() {
        return PAY_24;
    }

    public void setPAY_24(int pAY_24) {
        PAY_24 = pAY_24;
    }

    public int getPAY_25() {
        return PAY_25;
    }

    public void setPAY_25(int pAY_25) {
        PAY_25 = pAY_25;
    }

    public int getPAY_26() {
        return PAY_26;
    }

    public void setPAY_26(int pAY_26) {
        PAY_26 = pAY_26;
    }

    public int getDefault_payment_next_month() {
        return default_payment_next_month;
    }

    public void setDefault_payment_next_month(int default_payment_next_month) {
        this.default_payment_next_month = default_payment_next_month;
    }
}
