/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.helpers;

/**
 *
 * @author 7
 */

import com.tiemens.secretshare.engine.SecretShare;
import com.tiemens.secretshare.engine.SecretShare.PublicInfo;
import com.tiemens.secretshare.engine.SecretShare.ShareInfo;
import com.tiemens.secretshare.engine.SecretShare.SplitSecretOutput;
import com.tiemens.secretshare.exceptions.SecretShareException;
import com.tiemens.secretshare.math.BigIntUtilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ShamirSchema {

    public static HashMap<Integer, String> splitShare(String secret, int n, int k) {
        return MainSplit.splitShare(secret, n, k);
    }

    public static String combineSecret(HashMap<Integer, String> parts) {
        return MainCombine.combineSecret(parts);
    }

    private static class MainSplit {

        /**
         * @param args
         */
        public static HashMap<Integer, String> splitShare(String secret, int n, int k) {
            String param[] = new String[7];
            param[0] = null;
            param[1] = "-k";
            param[2] = "" + k;
            param[3] = "-n";
            param[4] = "" + n;
            param[5] = "-sS";
            param[6] = secret;

            try {
                SplitInput input = SplitInput.parse(param);
                SplitOutput output = input.output();
                List list = output.getShares();
                Iterator iter = list.iterator();

                HashMap<Integer, String> parts = new HashMap<Integer, String>();
                while (iter.hasNext()) {
                    SecretShare.ShareInfo share = (SecretShare.ShareInfo) iter.next();
                    parts.put(share.getIndex(), share.getShare().toString());
                }
                return parts;
//            Iterator it = list.iterator();
//            while(it.hasNext()){
//                SecretShare.ShareInfo share = (SecretShare.ShareInfo) it.next();
//                System.out.println(""+share.getShare());
//            }


                //  output.print(System.out);
            } catch (SecretShareException e) {
                System.out.println(e.getMessage());
                usage();
                optionallyPrintStackTrace(param, e);
            }

            return null;
        }

        public static void usage() {
            System.out.println("Usage:");
            System.out.println(" split -k <k> -n <n> -sN|-sS secret " + // required
                    "  [-prime384|-prime192|-primeN] [-d <desc>] [-paranoid <p>] "); // optional
            System.out.println("  -k <k>        the threshold");
            System.out.println("  -n <k>        the number of shares to generate");
            System.out.println("  -sN           the secret as a number, e.g. '124332' or 'bigintcs:01e5ac-787852'");
            System.out.println("  -sS           the secret as a string, e.g. 'My Secret'");
            System.out.println("  -d <desc>     description of the secret");
            System.out.println("  -prime4096    for modulus, use built-in 4096-bit prime");
            System.out.println("  -prime384     for modulus, use built-in 384-bit prime [default]");
            System.out.println("  -prime192     for modulus, use built-in 192-bit prime");
            System.out.println("  -primeN       for modulus, use a random prime (that is bigger than secret)");
            System.out.println("  -m <modulus>  for modulus, use <modulus>, e.g. '11753999' or 'bigintcs:b35a0f-F89BEC'");
            System.out.println("  -primeNone    no modulus, do NOT use any modulus");
            System.out.println("  -paranoid <p> test combine combinations, maximum of <p> tests");

        }

        public static BigInteger parseBigInteger(String argname,
                String[] args,
                int index) {
            checkIndex(argname, args, index);

            String value = args[index];
            BigInteger ret = null;
            if (BigIntUtilities.Checksum.couldCreateFromStringMd5CheckSum(value)) {
                try {
                    ret = BigIntUtilities.Checksum.createBigInteger(value);
                } catch (SecretShareException e) {
                    String m = "Failed to parse 'bigintcs:' because: " + e.getMessage();
                    throw new SecretShareException(m, e);
                }
            } else {
                try {
                    ret = new BigInteger(value);
                } catch (NumberFormatException e) {
                    String m = "Failed to parse integer because: " + e.getMessage();
                    throw new SecretShareException(m, e);
                }
            }

            return ret;
        }

        public static Integer parseInt(String argname,
                String[] args,
                int index) {
            checkIndex(argname, args, index);
            String value = args[index];

            Integer ret = null;
            try {
                ret = Integer.valueOf(value);
            } catch (NumberFormatException e) {
                String m = "The argument of '" + value + "' "
                        + "is not a number.";
                throw new SecretShareException(m);
            }
            return ret;
        }

        public static void checkIndex(String argname,
                String[] args,
                int index) {
            if (index >= args.length) {
                throw new SecretShareException("The argument '-" + argname + "' requires an "
                        + "additional argument");
            }
        }

        public static void optionallyPrintStackTrace(String[] args,
                Exception e) {
            boolean print = false;
            for (String s : args) {
                if (s != null) {
                    print = true;
                }
            }
            if (print) {
                e.printStackTrace();
            }
        }

        public static class SplitInput {
            // ==================================================
            // instance data
            // ==================================================

            // required arguments:
            private Integer k = null;
            private Integer n = null;
            private BigInteger secret = null;
            // optional: if 'secret' was given as a human-string, this is non-null
            // else this is null
            private String secretArgument = null;
            // optional:  if null, then do not use modulus 
            // default to 384-bit
            private BigInteger modulus = SecretShare.getPrimeUsedFor384bitSecretPayload();
            // optional: 
            //    paranoid: null = do nothing, paranoid < 0 = do all, otherwise paranoid = # of tests 
            private Integer paranoid;
            // optional description
            private String description = null;
            // optional: the random can be seeded
            private Random random;

            // ==================================================
            // constructors
            // ==================================================
            public static SplitInput parse(String[] args) {
                SplitInput ret = new SplitInput();

                boolean calculateModulus = false;
                for (int i = 0, n = args.length; i < n; i++) {
                    if (args[i] == null) {
                        continue;
                    }

                    if ("-k".equals(args[i])) {
                        i++;
                        ret.k = parseInt("k", args, i);
                    } else if ("-n".equals(args[i])) {
                        i++;
                        ret.n = parseInt("n", args, i);
                    } else if ("-d".equals(args[i])) {
                        i++;
                        checkIndex("d", args, i);
                        ret.description = args[i];
                    } else if ("-sN".equals(args[i])) {
                        i++;
                        ret.secret = parseBigInteger("sN", args, i);
                    } else if ("-sS".equals(args[i])) {
                        i++;
                        ret.secretArgument = args[i];
                        ret.secret = BigIntUtilities.Human.createBigInteger(args[i]);
                    } else if ("-r".equals(args[i])) {
                        i++;
                        int seed = parseInt("r", args, i);
                        ret.random = new Random(seed);
                    } else if ("-prime4096".equals(args[i])) {
                        ret.modulus = SecretShare.getPrimeUsedFor4096bigSecretPayload();
                    } else if ("-prime384".equals(args[i])) {
                        ret.modulus = SecretShare.getPrimeUsedFor384bitSecretPayload();
                    } else if ("-prime192".equals(args[i])) {
                        ret.modulus = SecretShare.getPrimeUsedFor192bitSecretPayload();
                    } else if ("-primeN".equals(args[i])) {
                        calculateModulus = true;
                    } else if ("-primeNone".equals(args[i])) {
                        calculateModulus = false;
                        ret.modulus = null;
                    } else if ("-m".equals(args[i])) {
                        calculateModulus = false;
                        i++;
                        final String thearg = args[i];
                        if (BigIntUtilities.Checksum.couldCreateFromStringMd5CheckSum(thearg)) {
                            ret.modulus = BigIntUtilities.Checksum.createBiscs(thearg).asBigInteger();
                        } else {
                            ret.modulus = new BigInteger(thearg);
                        }
                    } else if ("-paranoid".equals(args[i])) {
                        i++;
                        if ("all".equals(args[i])) {
                            ret.paranoid = -1;
                        } else {
                            ret.paranoid = parseInt("paranoid", args, i);
                        }
                    } else if (args[i].startsWith("-")) {
                        String m = "Argument '" + args[i] + "' not understood";
                        throw new SecretShareException(m);
                    } else {
                        String m = "Extra argument '" + args[i] + "' not valid";
                        throw new SecretShareException(m);
                    }
                }

                checkRequired("-k", ret.k);
                checkRequired("-n", ret.n);
                checkRequired("-sN or -sS", ret.secret);

                if (calculateModulus) {
                    ret.modulus = SecretShare.createAppropriateModulusForSecret(ret.secret);
                }

                if (ret.modulus != null) {
                    if (!SecretShare.isTheModulusAppropriateForSecret(ret.modulus, ret.secret)) {
                        String m = "The secret is too big.  Please adjust the prime modulus or "
                                + "use -primeNone";
                        throw new SecretShareException(m);

                    }
                }

                if (ret.random == null) {
                    ret.random = new SecureRandom();
                }
                return ret;
            }

            private static void checkRequired(String argname,
                    Object obj) {
                if (obj == null) {
                    throw new SecretShareException("Argument '" + argname + "' is required.");
                }
            }

            // ==================================================
            // public methods
            // ==================================================
            public SplitOutput output() {
                SplitOutput ret = new SplitOutput();
                ret.splitInput = this;

                SecretShare.PublicInfo publicInfo =
                        new SecretShare.PublicInfo(this.n,
                        this.k,
                        this.modulus,
                        this.description);

                SecretShare secretShare = new SecretShare(publicInfo);
                Random random = this.random;

                SecretShare.SplitSecretOutput generate = secretShare.split(secret, random);

                ret.splitSecretOutput = generate;

                if (paranoid != null) {
                    Integer parg = paranoid;
                    if (parg < 0) {
                        parg = null;
                    }

                    secretShare.combineParanoid(generate.getShareInfos(),
                            parg,
                            this.getParanoidOutput());
                }
                return ret;
            }

            private PrintStream getParanoidOutput() {
                return System.out;
            }
            // ==================================================
            // non public methods
            // ==================================================
        }

        public static class SplitOutput {

            private static String SPACES = "                                              ";
            public SplitInput splitInput;
            private SplitSecretOutput splitSecretOutput;

            public void print(PrintStream out) {
                final SecretShare.PublicInfo publicInfo = splitSecretOutput.getPublicInfo();

                //   field(out, "Secret Share version " + Main.getVersionString(), "");
                field(out, "Date", publicInfo.getDate());
                field(out, "UUID", publicInfo.getUuid());
                field(out, "Description", publicInfo.getDescription());

                markedValue(out, "n", publicInfo.getN());
                markedValue(out, "k", publicInfo.getK());
                markedValue(out, "modulus", publicInfo.getPrimeModulus(), false);
                markedValue(out, "modulus", publicInfo.getPrimeModulus(), true);

                List<SecretShare.ShareInfo> shares = splitSecretOutput.getShareInfos();
                out.println("");
                for (SecretShare.ShareInfo share : shares) {
                    printShare(out, share, false);
                }
                for (SecretShare.ShareInfo share : shares) {
                    printShare(out, share, true);
                }
            }

            public List getShares() {
                List<SecretShare.ShareInfo> shares = splitSecretOutput.getShareInfos();
                return shares;
            }

            private void markedValue(PrintStream out,
                    String fieldname,
                    BigInteger number,
                    boolean printAsBigIntCs) {
                String s;
                if (printAsBigIntCs) {
                    s = BigIntUtilities.Checksum.createMd5CheckSumString(number);
                } else {
                    s = number.toString();
                }
                out.println(fieldname + " = " + s);
            }

            private void markedValue(PrintStream out,
                    String fieldname,
                    int n) {
                out.println(fieldname + " = " + n);
            }

            // ==================================================
            // instance data
            // ==================================================
            private void field(PrintStream out,
                    String label,
                    BigInteger number) {
                if (number != null) {
                    String spaces;
                    if (label.trim().equals("")) {
                        spaces = SPACES.substring(0, label.length());
                    } else {
                        spaces = "." + SPACES.substring(0, label.length() - 1);
                    }

                    field(out, label, number.toString());
                    field(out, spaces, BigIntUtilities.Checksum.createMd5CheckSumString(number));
                } else {
                    // no output
                }
            }

            private void field(PrintStream out,
                    String label,
                    String value) {
                if (value != null) {
                    String sep;
                    String pad;
                    if ((label.length() > 0)
                            && (!label.trim().equals(""))) {
                        pad = label + SPACES;
                        pad = pad.substring(0, 30);
                        if (value.equals("")) {
                            sep = "  ";
                        } else {
                            sep = ": ";
                        }
                    } else {
                        pad = label;
                        sep = "";
                    }

                    out.println(pad + sep + value);
                }
            }

            private void printShare(PrintStream out,
                    ShareInfo share,
                    boolean printAsBigIntCs) {
                markedValue(out, "Share (x:" + share.getIndex() + ")", share.getShare(), printAsBigIntCs);
            }
            // ==================================================
            // constructors
            // ==================================================
            // ==================================================
            // public methods
            // ==================================================
            // ==================================================
            // non public methods
            // ==================================================
        }
    }

    private static class MainCombine {

        /**
         * @param args
         */
        public static String combineSecret(HashMap<Integer, String> parts) {
            Set<Integer> keys = parts.keySet();
            int len = keys.size();

            String param[] = new String[3 + len * 2];
            param[0] = null;
            param[1] = "-k";
            param[2] = "" + len;
            int i = 3;
            Iterator<Integer> iter = keys.iterator();

            while (iter.hasNext()) {
                Integer key = iter.next();
                param[i] = "-s" + key;
                param[i + 1] = parts.get(key);
                i += 2;
            }

            try {
                CombineInput input = CombineInput.parse(param);
                CombineOutput output = input.output();
                // output.print(System.out);
                //  System.out.println("Real secret: "+output.getCombinedSecret());
                return output.getCombinedSecret();
            } catch (SecretShareException e) {
                System.out.println(e.getMessage());
                usage();
                MainSplit.optionallyPrintStackTrace(param, e);
            }

            return null;
        }

        public static void usage() {
            System.out.println("Usage:");
            System.out.println(" combine -k <k>  -s<n> <secret-N> ..." + // required
                    "  [-prime384|-prime192|-primeN <m>|-primeNone] -stdin"); // optional
            System.out.println("  -k <k>        the threshold");
            System.out.println("  -s<n> <s>     secret#n as a number e.g. '124332' or 'bigintcs:123456-DC0AE1'");
            System.out.println("     ...           repeat this argument <k> times");
            System.out.println("  -prime384     for modulus, use built-in 384-bit prime [default]");
            System.out.println("  -prime192     for modulus, use built-in 192-bit prime");
            System.out.println("  -primeN <m>   for modulus use m, e.g. '59561' or 'bigintcs:12345-DC0AE1'");
            System.out.println("  -primeNone    modulus, do NOT use any modulus");
            System.out.println("  -stdin        read values from standard input, as written by 'split'");
        }

        private static BigInteger parseBigInteger(String argname,
                String[] args,
                int index) {
            return MainSplit.parseBigInteger(argname, args, index);
        }

        private static Integer parseInt(String argname,
                String[] args,
                int index) {
            return MainSplit.parseInt(argname, args, index);
        }

        public static class CombineInput {
            // ==================================================
            // instance data
            // ==================================================

            // required arguments:
            private Integer k = null;
            private List<SecretShare.ShareInfo> shares = new ArrayList<SecretShare.ShareInfo>();
            // optional:  if null, then do not use modulus 
            // default to 384-bit
            private BigInteger modulus = SecretShare.getPrimeUsedFor384bitSecretPayload();
            // optional: for combine, we don't need n, but you can provide it
            private Integer n = null;
            private PublicInfo publicInfo;
            // ==================================================
            // constructors
            // ==================================================

            public static CombineInput parse(String[] args) {
                CombineInput ret = new CombineInput();

                for (int i = 0, n = args.length; i < n; i++) {
                    if (args[i] == null) {
                        continue;
                    }

                    if ("-k".equals(args[i])) {
                        i++;
                        ret.k = parseInt("k", args, i);
                        if (ret.n == null) {
                            ret.n = ret.k;
                        }
                    } else if ("-n".equals(args[i])) {
                        i++;
                        ret.n = parseInt("n", args, i);
                    } else if ("-stdin".equals(args[i])) {
                        ret.processStdin();
                    } else if ("-m".equals(args[i])) {
                        i++;
                        ret.modulus = parseBigInteger("m", args, i);
                    } else if ("-prime384".equals(args[i])) {
                        ret.modulus = SecretShare.getPrimeUsedFor384bitSecretPayload();
                    } else if ("-prime192".equals(args[i])) {
                        ret.modulus = SecretShare.getPrimeUsedFor192bitSecretPayload();
                    } else if ("-primeN".equals(args[i])) {
                        i++;
                        ret.modulus = parseBigInteger("primeN", args, i);
                    } else if ("-primeNone".equals(args[i])) {
                        ret.modulus = null;
                    } else if (args[i].startsWith("-s")) {
                        String number = args[i].substring(2);
                        i++;
                        MainSplit.checkIndex("s", args, i);
                        String line = "Share (x:" + number + ") = " + args[i];
                        SecretShare.ShareInfo share = ret.parseEqualShare("-s", line);
                        // TODO; better checking for duplicates
                        ret.addIfNotDuplicate(share);
                    } else if (args[i].startsWith("-")) {
                        String m = "Argument '" + args[i] + "' not understood";
                        throw new SecretShareException(m);
                    } else {
                        String m = "Extra argument '" + args[i] + "' not valid";
                        throw new SecretShareException(m);
                    }
                }
                checkRequired("-k", ret.k);
                if (ret.shares.size() < ret.k) {
                    throw new SecretShareException("k set to " + ret.k + " but only "
                            + ret.shares.size() + " shares provided");
                }

                return ret;
            }

            private void processStdin() {
                try {
                    processStdinThrow();
                } catch (IOException e) {
                    throw new SecretShareException("IOException reading stdin", e);
                }
            }

            // examples of the kinds of lines we look for:
            //  n = 6
            //  k = 3
            //  modulus = 830856716641269307206384693584652377753448639527
            //  modulus = bigintcs:000002-dba253-6f54b0-ec6c27-3198DB
            //  Share (x:1) = 481883688219928417596627230876804843822861100800
            //  Share (x:2) = 481883688232565050752267350226995441999530323860
            //  Share (x:1) = bigintcs:005468-697323-cc48a7-8f1f87-996040-4d07d2-3da700-9C4722
            //  Share (x:2) = bigintcs:005468-69732d-4e02c5-7b11d2-9d4426-e26c88-8a6f94-9809A9
            private void processStdinThrow()
                    throws IOException {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("n =")) {
                        this.n = parseEqualInt("n", line);
                    } else if (line.startsWith("k =")) {
                        this.k = parseEqualInt("k", line);
                    } else if (line.startsWith("modulus =")) {
                        this.modulus = parseEqualBigInt("modulus", line);

                    } else if (line.startsWith("Share (")) {
                        SecretShare.ShareInfo share = parseEqualShare("share", line);
                        addIfNotDuplicate(share);
                    }
                }

            }

            private void addIfNotDuplicate(ShareInfo add) {
                boolean shouldadd = true;
                for (ShareInfo share : this.shares) {
                    if (share.getX() == add.getX()) {
                        // dupe
                        if (!share.getShare().equals(add.getShare())) {
                            throw new SecretShareException("share x:" + share.getX()
                                    + " was entered with two different values "
                                    + "(" + share.getShare() + ") and ("
                                    + add.getShare() + ")");
                        } else {
                            shouldadd = false;
                        }
                    } else if (share.getShare().equals(add.getShare())) {
                        throw new SecretShareException("duplicate share values at x:"
                                + share.getX() + " and x:"
                                + add.getX());
                    }
                }
                if (shouldadd) {
                    this.shares.add(add);
                }
            }

            private ShareInfo parseEqualShare(String fieldname,
                    String line) {
                if (this.publicInfo == null) {
                    this.publicInfo = new SecretShare.PublicInfo(n, k, this.modulus, "");
                }

                BigInteger s = parseEqualBigInt(fieldname, line);
                int x = parseXcolon(line);
                return new ShareInfo(x, s, this.publicInfo);
            }

            //  Share (x:2) = bigintcs:005468-69732d-4e02c5-7b11d2-9d4426-e26c88-8a6f94-9809A9
            private int parseXcolon(String line) {
                String i = after(line, ":");
                int end = i.indexOf(")");
                i = i.substring(0, end);

                return Integer.valueOf(i);
            }

            private BigInteger parseEqualBigInt(String fieldname,
                    String line) {
                String s = after(line, "=");
                if (BigIntUtilities.Checksum.couldCreateFromStringMd5CheckSum(s)) {
                    return BigIntUtilities.Checksum.createBigInteger(s);
                } else if (BigIntUtilities.Hex.couldCreateFromStringHex(s)) {
                    return BigIntUtilities.Hex.createBigInteger(s);
                } else {
                    return new BigInteger(s);
                }
            }

            private String after(String line,
                    String lookfor) {
                return line.substring(line.indexOf(lookfor) + 1).trim();
            }

            private Integer parseEqualInt(String fieldname,
                    String line) {
                String s = after(line, "=");
                return Integer.valueOf(s);
            }

            private static void checkRequired(String argname,
                    Object obj) {
                if (obj == null) {
                    throw new SecretShareException("Argument '" + argname + "' is required.");
                }
            }

            // ==================================================
            // public methods
            // ==================================================
            public CombineOutput output() {
                CombineOutput ret = new CombineOutput();
                ret.combineInput = this;

                SecretShare.PublicInfo publicInfo =
                        new SecretShare.PublicInfo(this.n,
                        this.k,
                        this.modulus,
                        "recombine combine command line");

                SecretShare secretShare = new SecretShare(publicInfo);

                SecretShare.CombineOutput combine = secretShare.combine(shares);

                ret.secret = combine.getSecret();

                return ret;
            }
            // ==================================================
            // non public methods
            // ==================================================
        }

        public static class CombineOutput {

            private BigInteger secret;
            @SuppressWarnings("unused")
            private SecretShare.CombineOutput combineOutput;
            @SuppressWarnings("unused")
            private CombineInput combineInput;

            public void print(PrintStream out) {
                //final SecretShare.PublicInfo publicInfo = combineOutput.getPublicInfo();

                //  out.println("Secret Share version " + Main.getVersionString());
                //field(out, "Date", publicInfo.getDate());
                //field(out, "UUID", publicInfo.getUuid());
                //field(out, "Description", publicInfo.getDescription());

                out.println("secret.number = '" + secret + "'");
                String s = BigIntUtilities.Human.createHumanString(secret);
                out.println("secret.string = '" + s + "'");

            }

            public String getCombinedSecret() {
                return BigIntUtilities.Human.createHumanString(secret);
            }
            // ==================================================
            // instance data
            // ==================================================
            // ==================================================
            // constructors
            // ==================================================
            // ==================================================
            // public methods
            // ==================================================
            // ==================================================
            // non public methods
            // ==================================================
        }
    }
}