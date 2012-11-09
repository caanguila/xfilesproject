/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.web.galua;
import java.util.*;
/**
 *
 * @author 7
 */

public class UsefulAlgo {
    
    public static long qe2(long a, long x, long n){
        long s,t,u;
        int i;
        s=1;
        t=a;
        u=x;
       
        if(u<0) u = n-1+u;
        
        while((u)!= 0){
           if((u&1) == 1) s = (s*t)%n;
           u = u>>1;
           t =(t*t)%n; 
       }
        
        return s;
        
    }
    /**
     *        1. Если b=0 положить d:=a, x:=1, y:=0 и возвратить (d,x,y)
     *        2. Положить x2:=1, x1:=0, y2:=0, y1:=1
     *        3. Пока b>0
     *        3.1 q:=[a/b], r:=a-qb, x:=x2-qx1, y:=y2-qy1
     *        3.2 a:=b, b:=r, x2:=x1, x1:=x, y2:=y1, y1:=y
     *        4. Положить d:=a, x:=x2, y:=y2 и возвратить (d,x,y)
     * @param a
     * @param b
     * @return ArrayList list (d, x, y)
     */
    public static ArrayList extendedEuclide(long a, long b){
        
        ArrayList list = new ArrayList();
        
        if(b == 0) {
            list.add(a);
            list.add(1);
            list.add(0);
            return list;
        }
        long x2 = 1, x1 = 0, y2 = 0, y1 = 1;        
        long q,r,x,y;
        
        while(b>0){
          q = a / b;
          r = a - q * b;
          x = x2 - q * x1;
          y = y2 - q * y1;
         //System.out.println("iter"); 
          a = b; b = r;
          x2 = x1;
          x1 = x;
          y2 = y1;
          y1 = y;
        }
        
        list.add(a);
        list.add(x2);
        list.add(y2);
        
        
        return list;
    }
    
    public static long ReverseElement(long a, long N) {
  long x, y, d;
  long result;
  List list = extendedEuclide(a, N);
   d = ((Long) list.get(0)).longValue();
   
  if (d != 1) {
    return 1;
  } else {
    result = ((Long) list.get(1)).longValue();
    if(result<0) result = N +result;
    
    return result;
  }
}
    
    public static long getDiscretLogarifm(long g, long y, long n){
        long result = 0;
        long i = 0;
        
        for(i = 0; i< n; i++){
            
        if(y == qe2(g,result,n)){
            return result;
        }   
        result++;
        }
        
        return 0;
    }
   
}
