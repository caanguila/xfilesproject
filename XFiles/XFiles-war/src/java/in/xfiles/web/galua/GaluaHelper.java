/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.web.galua;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.*;
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
    
    public long curve1N;
    public long param1A;
    public long param1B;
    
    public long elemX;
    public long elemY;
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

    public long getCurve1N() {
        return curve1N;
    }

    public void setCurve1N(long curve1N) {
        this.curve1N = curve1N;
    }

    public long getParam1A() {
        return param1A;
    }

    public void setParam1A(long param1A) {
        this.param1A = param1A;
    }

    public long getParam1B() {
        return param1B;
    }

    public void setParam1B(long param1B) {
        this.param1B = param1B;
    }

    
    
    
    
    public long getN() {
        return n;
    }

    public void setN(long n) {
        this.n = n;
    }
    
    public long getReverseElement(){
        
       if(n!=0) elementToReverse = ((elementToReverse % n)+n)%n;
        return UsefulAlgo.ReverseElement(elementToReverse, n);
    }
    
    public long getPowerResult(){
        if(n1!=0) elementToPower = ((elementToPower % n1)+n1)%n1;
        
        if(power > 0){
          return UsefulAlgo.qe2(elementToPower, power, n1);
        }
        else if(power == 0){
          return 1;
        }else if(power < 0){
          return UsefulAlgo.qe2(UsefulAlgo.ReverseElement(elementToPower, n1), -power, n1);
        }
        
        return 0;
    }
    
    public String getSummOfCurve(){
        long[] point1 = new long[2];
        long[] point2 = new long[2];
        
        if(curveN!=0){
            x1 = ((x1 % curveN)+curveN)%curveN;
            y1 = ((y1 % curveN)+curveN)%curveN;
            x2 = ((x2 % curveN)+curveN)%curveN;
            y2 = ((y2 % curveN)+curveN)%curveN;
             
        }
        point1[0] = x1;
        point1[1] = y1;
        
        point2[0] = x2;
        point2[1] = y2;
        if(curveN==0) return "";
        
        long[] point3 = ElepticCurve.summ(point1, point2, curveN, paramA, paramB);
        return " ( "+point3[0]+", "+point3[1]+" ) ";
    }
    
    public List yval; 
    
    public String getCurveValues(){
        HashMap sqmap = new HashMap();
        HashMap sqymap = new HashMap();
        for(long i = 0; i< curve1N; i++){
          long sqy = ((i*i*i)%curve1N + param1A * i +param1B)%curve1N;  
          sqmap.put(i, (i*i)%curve1N);
          sqymap.put(i, sqy);
        }
        System.out.println("Table:");
        
        printMap(sqymap);
        System.out.println("Table:");
        printMap(sqmap);
      //  System.out.println(getCurveMapElements(sqmap));
       // System.out.println(getCurveMapElements(sqymap));
        
        yval = curveSqrt(sqmap, sqymap);//(x,y)
    //    getAllSubGroups();
//        if(!yval.isEmpty())
//        getSubGroup((Long)yval.get(0), (Long)yval.get(1));
        return getCurveMapElements(yval);
    }
    private void printMap(Map m){
        for(Object o: m.keySet()){
            System.out.println("key: "+o+" value: "+m.get(o));
        }
    }
    
    public String getSubGroups(){
        String result ="";
        if(elemX !=0 || elemY!=0){
            List subGroup = getSubGroup(elemX, elemY);
            result+=" ( "+elemX+", "+elemY+" ): {"+getCurveMapElements(subGroup)+"} \n";
            System.out.println("All Subgroups: "+result);
        }
        return result;
    }

    public long getElemX() {
        return elemX;
    }

    public void setElemX(long elemX) {
        this.elemX = elemX;
    }

    public long getElemY() {
        return elemY;
    }

    public void setElemY(long elemY) {
        this.elemY = elemY;
    }
    
    
    
    private List getSubGroup(long x, long y){
       
        long a[] = new long[2];
        long b[] = new long[2];
        a[0] = x;
        a[1] = y;
        b[0] = a[0];
        b[1] = a[1];        
        long c[] = new long[2];
        List l = new ArrayList();
        
        long start[] = new long[2];
        
        start[0] = Long.MIN_VALUE;
        start[1] = Long.MIN_VALUE;
        c = ElepticCurve.summ(b, a, curve1N, param1A, param1B);
        while( start[0]!=c[0] || start[1]!=c[1] ){ 
            l.add(c[0]);
            l.add(c[1]);
            
            if(c[0] ==Long.MAX_VALUE && c[1] ==Long.MAX_VALUE) break;
            if(start[0] == Long.MIN_VALUE && start[1] == Long.MIN_VALUE){
                start[0] = c[0];
                start[1] = c[1];
            }
         //   System.out.println("( "+c[0]+", "+c[1]+" )");
            b[0] = c[0];
            b[1] = c[1];
   
           c = ElepticCurve.summ(b, a, curve1N, param1A, param1B);
           
        
        }
        
        
        
        
        
        
        return l;
    }
    private List curveSqrt(HashMap sq, HashMap sqy){
        ArrayList ymap = new ArrayList();
        
        Iterator sqyiter = sqy.keySet().iterator();
//        Iterator sqiter= sq.keySet().iterator();
      
        while(sqyiter.hasNext()){
            Long sqykey =  (Long) sqyiter.next();//x
            Long sqyValue = (Long) sqy.get(sqykey.longValue()); //y^2
            
            Iterator sqiter = sq.keySet().iterator();
            while(sqiter.hasNext()){
                 Long sqkey =  (Long) sqiter.next(); //x
                 Long sqValue = (Long) sq.get(sqkey.longValue()); //x^2
                 
                 if(sqyValue.equals(sqValue)){
                   //  System.out.println("y^2: "+sqyValue+"  y: "+sqkey);
                   //  ymap.put(sqykey.longValue(), sqkey.longValue());
                     ymap.add(sqykey.longValue());
                     ymap.add(sqkey.longValue());
                 }
                 
            }
            
        }
      
        
        
        return  ymap;
    }
    
    private String getCurveMapElements(List m){
        String result = "";
        for(int i=0; i<m.size(); i+=2){
            if((Long)m.get(i) != Long.MAX_VALUE)
            result+=" ( "+m.get(i) +", "+m.get(i+1) + " ) ";
            else result+=" ( "+"INF" +", "+"INF" + " ) ";
        }
        
        
        return result;
    }
}
