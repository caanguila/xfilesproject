/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.web.galua;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author 7
 */
@ManagedBean
@RequestScoped
public class GaluaHelper {

    public long elementToReverse;
    public long n;
    
    public long elementToPower;
    public long n1;
    public long power;
    
    public long x1;
    public long x2;
    public long y1;
    public long y2;
    public long curveN;
    public long paramA;
    public long paramB;
    /**
     * Creates a new instance of GaluaHelper
     */
    public GaluaHelper() {
    }

    public long getElementToReverse() {
        return elementToReverse;
    }

    public void setElementToReverse(long elementToReverse) {
        this.elementToReverse = elementToReverse;
    }

    public long getElementToPower() {
        return elementToPower;
    }

    public void setElementToPower(long elementToPower) {
        this.elementToPower = elementToPower;
    }

    public long getN1() {
        return n1;
    }

    public void setN1(long n1) {
        this.n1 = n1;
    }

    public long getPower() {
        return power;
    }

    public void setPower(long power) {
        this.power = power;
    }

    public long getX1() {
        return x1;
    }

    public void setX1(long x1) {
        this.x1 = x1;
    }

    public long getX2() {
        return x2;
    }

    public void setX2(long x2) {
        this.x2 = x2;
    }

    public long getY1() {
        return y1;
    }

    public void setY1(long y1) {
        this.y1 = y1;
    }

    public long getY2() {
        return y2;
    }

    public void setY2(long y2) {
        this.y2 = y2;
    }

    public long getCurveN() {
        return curveN;
    }

    public void setCurveN(long curveN) {
        this.curveN = curveN;
    }

    public long getParamA() {
        return paramA;
    }

    public void setParamA(long paramA) {
        this.paramA = paramA;
    }

    public long getParamB() {
        return paramB;
    }

    public void setParamB(long paramB) {
        this.paramB = paramB;
    }

    
    
    
    
    public long getN() {
        return n;
    }

    public void setN(long n) {
        this.n = n;
    }
    
    public long getReverseElement(){
        
        return UsefulAlgo.ReverseElement(elementToReverse, n);
    }
    public long getPowerResult(){
        return UsefulAlgo.qe2(elementToPower, power, n1);
    }
    
    public String getSummOfCurve(){
        long[] point1 = new long[2];
        long[] point2 = new long[2];
        point1[0] = x1;
        point1[1] = y1;
        
        point2[0] = x2;
        point2[1] = y2;
        if(curveN==0) return "";
        
        long[] point3 = ElepticCurve.summ(point1, point2, curveN, paramA, paramB);
        return "( "+point3[0]+", "+point3[1]+" )";
    }
    
}
