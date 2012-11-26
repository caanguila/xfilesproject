
import in.xfiles.web.galua.UsefulAlgo;


/**
 *
 * @author danon
 */
public class Shit {
    public static void main(String[] args) {
        long N = 23;
        for(long x = 1; x<N; x++)
            System.out.println(x+" => "+UsefulAlgo.ReverseElement(x, N));
    }
}
