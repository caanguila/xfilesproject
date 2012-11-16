/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.web.galua;

/**
 *
 * @author 7
 */
public class ElepticCurve {
    
    public static long[] summ(long[] a, long[] b, long n, long paramA, long paramB){
    long[] c = new long[2];
    long s=0;
    if(a[0] == b[0] && a[1]!=b[1]){
        c[0] = Long.MAX_VALUE;
        c[1] = Long.MAX_VALUE;
        return c;
    }
    if(a[0] == b[0] && a[1] ==b[1]){
         s = ((3*a[0]*a[0] + paramA) * (UsefulAlgo.ReverseElement(2*a[1], n)))%n;
         
        while(s<0) s +=n;
        c[0] = (s*s - a[0] - b[0])%n;
        c[1] = (s*(a[0] - c[0]) -a[1])%n;
        if(c[0]<0) c[0]+=n;
        if(c[1]<0) c[1]+=n;
        System.out.println("s:"+s);
        System.out.println("x3:"+c[0]);
        System.out.println("y3:"+c[1]);
        return c;
    }
    else if(a[0] != b[0] && a[1]!=b[1]){
        long k = b[0]-a[0];
        while(k<0) k+=n;
        s = ((b[1] - a[1]) * (UsefulAlgo.ReverseElement(k, n)))%n;
        while(s<0) s +=n;
        c[0] = (s*s - a[0] - b[0])%n;
        c[1] = (s*(a[0] - c[0]) -a[1])%n;
        if(c[0]<0) c[0]+=n;
        if(c[1]<0) c[1]+=n;
        
        System.out.println("s:"+s);
        System.out.println("x3:"+c[0]);
        System.out.println("y3:"+c[1]);
        return c;
    }
    return c;
    }
}
