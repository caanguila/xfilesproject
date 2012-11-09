/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.web.galua;
import java.util.HashMap;
import java.util.Iterator;
import java.util.*;
import java.util.Set;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author 7
 */
public class Coding {
    
    private final String cipro;
//            ="ЪДЪИМГЬФЬНЯМКЩИМУОВВИЬНХРНЧЮОБИГЪДШММЮДХЫКЮЮОЩЯСЧСВПЛФАМЯНЯЪНБКРЬПВРИДИВШРФ"+
//"ОИМДХУКСЛСЦЛИЭДЩЛИДЗСЯПФЛМАЬЪЯНВРНЖЛЬЦИГМОЖУМШНЖЛИЧКХОИВЖХБЖЪЫИМРСВКЫОСБСОД"+
//"ЫМДТЖЪЪЖОЯАГЪОКФЖФНМХМЗАКМЪНШГРЩОСПЪЮМРЩУРЫОВАГЮДОХЯЕМДБЪЩУКРЖУУАТЫЦЖКИЬНХУ"+
//"ПЖМКЩКХЮСЗНМЮНВЦОВППБОУУКБЮРЩЯСЮЗЬСПЮБРЦДАППЩФГЪМРКФШКЕЫКСЛСЕКРЧНЭЖУБИМУЗБМ"+
//"САИГЪЗКЛИВКЖШЮЩГОБИЕЦГЪИХБЖЯЧНЧВГЮДДШНБВМАЕЦЪЯЩГЕФЮЫАЭЩИМАИЦМОЪОЪФОНАЗЦГЛДК"+
//"ПЪДБЖВДЯСЭЯЧЦЦТВИШЯБЖИЯЛЦЮСЭРЯДЫОУСХРЯВКТОПЮГХЖЬЦФСХКЕДОСЭНБГЪЩОЮЭДГЮУПКХЧЯ"+
//"ЯЩЕФЖМЩЯЬСУЭПНОАЯСНЬЖСЩЯЯЖТБЖМОПАГМДЕМЦЧЖЮДДЕГЬЗЖЮРГЬДЫСХИМШПЫЫЭЭРИЮКПЦПЗЗГ"+
//"ЯДМЪНШЮПЬКХРДДРЮЦЬВЯЯЮЖИВЬНОИЭГМЦКФЪДБЖНБЗЦЪДЕИЦЛЬОЫРСМХЦБУЯЪЧЮХРУМДЗЮМЕВМС"+
//"ЕЛРБРЖЖПЦЛВКГЫПУЧЗГМЪЕДТЫРХКЮЯЕГПКЗЗГЯДТЭЮАМНЕКПАЛЪПХЖЯЗУРЭВИЮЬЖААЪОРФОСЭЧХ"+
//"ПЗБУНЫЗВВРФЕСФНБНСШНХАОЭИНАДПЫЦЪЛЯГКДЧНБГФЩЗИЪЗЯРГЭЭСЦИВЖЧГЬРЯНЧПНЬИССЯАЖЗФ"+
//"ВИЪДЕЗСЮШНЫЖХКВЮНВЦБВАФЩСЗРЗЫГРЬЫШЫИХЕГЮЬФКИХЗГУОСЪДЯМЕЭКФЯЫБГОРГВЮИХЕГЕШРО"+
//"БЪОРБОСДМВИМВМСПТЩЖОБНЯРМХЦИЯЯИЭНЪХЦЦНХРНЯЬДЦДЗОЕЪПСЯИМЯДЯЪРБУХЫАРБСДЛСТЯЖЮ"+
//"НБЯСЭНЩЮХББФЯЫБГХФЕЪЯНЦЩХБЖФЯЪЪМЗАЬНЫЕЭЛИЕКЪЯНЦЩХБИНЦДЕНСДКДЪЪЦЩОЬЕОМАЧЖРБЛ"+
//"УЦБЕГПЕКПХГЪПЯХЧОЫЦЖМХБОГЧНЪПХГЬРЪНЪХХБОСРСХЗСЯМСТДМГЖБКРЮЯАЛИЯКЖЮДЦГСХЦВЮМ"+
//"ЭРЯЩЗЦЬНЮЮЛФЖСЮЫЮЮНДЬПЫМГМХБЗФЫЖБЮЕФЖФНЦЖМЕЩНЯПЯЯПСЦНИЩРЧММЯЯСРНДМПЬФЦЩНАПХ"+
//"ФЖРОМЪПНБЖЯЧНАЖРЖОНОИЦСЗЕКЖТДЖМЕШЬОЦРЮОЮВЕМЦСДСДПИГЭДЬЩЕФЖМСГЪРСЫЬЖЫПХКМЬЮФ"+
//"УОВВИГИЦШНЕЪХЖЗГЪНАНСИККЦЛБЮРЩЭУУЕБМЛФЗГШДЧЮРАКИЬНЯГРФЕГЭСЭЛИЬДЛЛСВБСЯЯОЦРЖ"+
//"МЖБЕСУИХЗРФЭУЫРХЛРБЯСЬНЯЭЕПССТЗЯЖВДИСЦНЮМРЛБРЪНЖМОРЕСЫГБЖХБИНЦДМГУЕЧЦРКЪЗГЕ"+
//"БОКМВЖДЮКРТЗБЗМЩБСРЯЯЪРБЕУАВЯЖЕМБИЮЮЯЖЪЬЕСУДЖМРЩИЯЧЗЭРСАБРКИЭЖФЕЬРЧЯЮММХЧЕО"+
//"ДЖСМАНХЦСЗРНЬЮТУПЧЩИЯБФНХРНСДЖИРЪГСФЭЬИУАЪИСЩЛСДСЭНУБНХЫДГИГЕШМГДЯГЖЭКМШНЧЗ"+
//"СБЭШРЯЖЖЕМБИРНЧПИИЗИЮСХУПБЖСТДБЪНЬБФЯПВЖРПБЪШДБЩНБОСЭЪЪМЛАЬЪОКЭПЯЦЕГЧЗЪРСЛД"+
//"ФЯЪКИМАДВВИХЕГЮКФКНБЮЕДЫТЫФВВМЮЬРОИХЗЦТОСЦВДСЫЭПСЯЦЪРОЬЮСРЪЖМЪЩИРАЭЭЕФЮКРЫБ"+
//"ВЖНБНХЦНБЮХБЖЯЧНВВРФЭИШДЯЮМЦЧШЫГЭИГВМСХПХХРБЪМЮБЪРОБЪМХЛЗРРБДМЪДГОСЫМГДМВЖХ"+
//"БЖТИБЭВРБОГЧТЫЯЮЦЬИЯМХПЕЩОИРЗЩЛСЬУМДЗЮМЕПИГЪДЕЗСЮШНЫЛЭЛЦЕЮКЦЖБЖСХМГЖЯУРФУЮТ"+
//"ЫЬЖМЕАКФШНЧМТБЩХПТЩГХЖВИЮКЭЦНБЗТЫИДЮМАБМЩДДГСАЛСДТЧПХЦКЕОКЕГДУНСРДДЦИАИСДДА"+
//"РСЦМСТДАМОБАССНМГОБЮИЧЯМСХРУЦЯЫБГЖЖНГЭНАСЕЬАИРЧЭАСЫЖИЪЗКНЦДОСЦРЖСОБИХЫСМЮФЩ"+
//"ЯСХЯБЭОГЬЛСНЧМУДИГДЯЯЮРЩЕОУЗЯПВАКТЫРЯГЗЩЖСЬННИСЬКРЪЯМЮОШЬКУОВИЦЛЬХКУВОФАКЛТ"+
//"ДЕЪНЦБОЦЦХЖЫЩЗЦЬПЭПНБМДЦЭБЮЗБЭРЫЖХКИЕДХКЦЖМОТАМЮСЪНИАИЮУЗЬЮРЬЗГМШЭГЕФВРИДЩМ"+
//"ОЪИСЮСЭЗГЭОСЪДАЛСЧКХНЕЪИСЦЬХИБДЮЛЧКЕЫПХУФШЬПОЛЭЛГСОСЩЯЕРИГЬЖЫРГМЗФЛСЭТМЖНЬД"+
//"РЦИХЗРЩАГШДЪЗГВДХОМЕЗМИУМЪНЧЗГЭКРЦГЪИГТОДЫВЭУЕЩАГУСЮЮКЩОФНЗБГСЛБРКЛЗВУЩИЮУБ"+
//"ЪЧМЧКЕЫПФРГШБЕЦХХРСЬАИШНЮЮЪФБХЮЮБЮФЕПОУНЖППЩСГЮСХРФЭДМФДЕМЕЩОРЦИЦМЖЫИГУСМРС"+
//"ГЬФЮИХДИЕДОЦОВАИШБХЭДМЪСЕКПДСВОСДНМННМГРРЛУЫРЖОГАИСУВВПЦШЬУЮСЧММЮДСЯОЗПХЬОН"+
//"ЫЛГИМЯБРЯИВРСГЧМЧНБГЪАКЕИГЗКГАИИПДЬМФЕМСАЛЭЭРБКХЪДШМЦЪЬФЪНГЮШАБХЧМЭБСТБФШЗЫ"+
//"ГФЭЬКУСМРСАДДАГСППЩФРЫДЖМФФЗРУРДЮЕАБРЪНЦМОРФИЮЛЪГХДЫЪУЛЖЮНБОСЭЯФГЖБНОАЧХГХЫ"+
//"АИЮЫТРСЫЬПУЦЪЛСШЖВЯНШМЪЕКДИЦЭРГЕБОЦБЭВИЮДТЫЦЪКЦХЖСЪГЭЛНФНХОКХЕИЦЬХКБВАУЩЗВЭ"+
//"ЯЕПНФГСРМХЦИЧКЖУПВЭЖЩМСЦНЩЛГЭККУРВАФЩЗАЯНШМРЩГГЩДМЮОГЬФЮИХЕЮЦЬВЩМВДИДОЕЫОДЖ"+
//"ВЕИЮВБЪЧИЬЕСЯНДЩИЖВИЮКЗХГЮКФКДАСТГКМХМВПМЕШЕЬНЩМДАЧШЮКЗХГУСЕЭЯЬЛЮИЗИЮСХУМЯБ"+
//"РЪНЧПМЯЭМЭРЮММЧПДУПБЖМЖНСБПВЛГЬЮГЪНЧЖЪФЭИЮОЪХРБЯССГЪЯЮЮДХЫВЩЮЗБУЯУВВЮЗЩЖГЦГ"+
//"ХПСЗМСЪНЧЛГДОУУЛФЕСЮКЕЧЯАЖПФМЯУЗШЮЕГДОЫББММФЖИЧРХЛЗГКМСЯЧОМЮКЕЪНЭЖГШБОКВЪЖЗ"+
//"БДЖОБДЖОБЮРЫЗЗТИШКУОУЪВСГКЕЦЦХНИГБНЭНЪАГЦМВХЯБПНБДЖААЪОРЬДЦБПВИГЦЬФЦКСГЕЬУГ"+
//"ЬНЦГЗБИСЮМВБСЦЛИЪЖЪЛФЭКМСТЦГУАДМЦТЦОГЕЬИСНГГХГЬЕОРЭИЯЩЮМДЯШВИХЧОЦРЧМВЛБРЦХХ"+
//"ГЖБЕГЯДДЖРФЗМВЯЭИСЦИГЦББСЪФОРИДЕГФЕМЮУДДМЛФРИТНДМЕАЬМЛЛЭИМУРИТНДМЕАЬЕРЮЖПНБ"+
//"ДЖААЪОРЬДЦЬДЖОГЦЬУЮНБМЧРБЕЦЦХБЗЩЭЮШЯЕГФЕМГЪДЧГФЕЕМУВВНИЮЬЖУЮЪБСГКЕЪЯЕНОЩЗВЪ"+
//"МЭФИЬНСБЫЪЖУБНХЦРЯЮЕАКМЦГЧСПУНЕЫГБЩПЬНИЮСДЮПЬНСБЗЪЖГЮБНЮЯБВУБЮРЫЗЭКГЭЖГЯТДМ"+
//"МФЖИЧРХЛЗГКЕЪНЭАФЩЗЗОЛХКФБЮИЭЧЪЛРБИИЬНБОГЦДОЫРСРГЭКИЫАКМКШБРЦДМЖЪЬЕСРЯВВРФД"+
//"ЛЪЗКЛГГКЪЪНГОСМЖГЩЗАМИЧКЪЯНЦЩЗФОЯУЛЗЫХБГГЩДЖЖХРДЗОЕЪЕГШБООАЯМРШДРЧТЩМЕБЖЯЪН"+
//"БГДГБКЪНЖМОДОЮЩПЗИСДЮСУВВНОФОЯНЯНЮУЗКПЧНЖМУПДТЫПКЮОЦКНЭТШНОЩУИУПХПТБМВТЗЯЮФ"+
//"РОГЧЦЖМСАЗГВМЗИНБИЩЫЛЕАСЬЗИУОВПГЯКПАКЭФЦЦОСФДЕЮПББЕЭДАЭТБГГТЗЪБСЬГСТМЭУЗФЗФ"+
//"ЧЗКСФЕДЛЪДЕИСДШЕЩДЕРИДГГЬЯКМПЗДГШНЮВСЦКОКМВ";

    public Coding() {
        cipro = getStringFile("D:/cry.txt");
        System.out.println(cipro);
    }
    
    
    
    private Map<Integer, Integer> statistic = new HashMap(); 
    private Map<Integer, Integer> realStatistic = new HashMap(); 
    private Map<Integer, Double> statisticProb = new HashMap(); 
    private Map<Integer, Double> realstatisticProb = new HashMap();
    
    public String cesarcode(String text, int param){
        
        text = text.toUpperCase();
        String buf = "";
        int a = (int) ' ';
        char s = (char) a;
        
      // System.out.println("a(int)= "+a+" s(char)= "+s);
        int i;
        for(i = 0;i < text.length(); i++) {
            s = text.charAt(i);
            a = (int) s;
            System.out.println("index: "+i+"  a(int)= "+a+" s(char)= "+s);
             
            
             if((a+param)>=1040 & (a+param)<=1071 ) a = a + param;
             else{
                 int d = 1071-a;
                 param -=d;
                 a = 1040+param;
             }
            s=(char)a;
            //System.out.println("a(int)= "+a+" s(char)= "+s);
            buf = buf+(char)(a);
        }
        /*for(i=1;i<200;i++){
            a=i;
            s=(char)a;
            System.out.println("a(int)= "+a+" s(char)= "+s);
        }
         * 
         */
       System.out.println(buf);
        return buf;
        
    }
    
    public void printASCCI(){
       char c;
       
        for(int i=1040; i<1072; i++){
          c = (char) i;  
          System.out.println("i: "+i+" char: "+c);  
        
        }
        
    }
    
    public void getInfo(){
        System.out.println("Length: " + cipro.length());
        initMap();
        scanInputText();
        
        
        getRealStat();
        normMaps();
        printExpectedText();
       // printSortMap();
    }

    private void initMap() {
        
         for(int i=1040; i<1072; i++){
        statistic.put(i, 0);
        }
         printMap();
    }
    private void initRMap() {
        
         for(int i=1040; i<1072; i++){
        realStatistic.put(i, 0);
        statisticProb.put(i, 0D);
        realstatisticProb.put(i, 0D);
        }
       
    }
    private void printMap(){
      Set set = statistic.keySet();
      
      Iterator iter = set.iterator();
       System.out.println();
      while(iter.hasNext()){
            Integer key = ((Integer) iter.next());
            int keyVal = key.intValue();
          System.out.println("Map key: "+keyVal+"   word element: "+((char)keyVal)+"  count: "+statistic.get(key));
      }
    }
    
    private void getRealStat(){
        initRMap();
        ByteArrayOutputStream baos = null;
        FileInputStream fis = null;
        try {
            File file = new File("D:/abc.txt");
            System.out.println("file: "+file.exists());
            fis = new FileInputStream(file);
            byte[] mass = new byte[fis.available()];
            fis.read(mass);
            baos = new ByteArrayOutputStream();
            baos.write(mass);
            String realText = baos.toString("Windows-1251").toUpperCase();
        //    System.out.println("Text: "+realText);
          scanRInputText(realText);
          printSortRMap();         
            
        } catch (IOException ex) {
            Logger.getLogger(Coding.class.getName()).log(Level.SEVERE, null, ex);
        }  finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(Coding.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String getStringFile(String path){
            ByteArrayOutputStream baos = null;
        FileInputStream fis = null;
        try {
            File file = new File(path);
            System.out.println("file: "+file.exists());
            fis = new FileInputStream(file);
            byte[] mass = new byte[fis.available()];
            fis.read(mass);
            baos = new ByteArrayOutputStream();
            baos.write(mass);
            String realText = baos.toString("Windows-1251").toUpperCase();
        //    System.out.println("Text: "+realText);
      //    scanRInputText(realText);
      //    printSortRMap();         
            return realText;
            
        } catch (IOException ex) {
            Logger.getLogger(Coding.class.getName()).log(Level.SEVERE, null, ex);
        }  finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(Coding.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    return null;
    }
    
   public void normMaps(){
        int summarySizeReal = 0;
        int summarySize = 0;
        for(int i=1040; i<1072; i++){
            summarySizeReal+=realStatistic.get(i);
            summarySize+= statistic.get(i);
        }
        System.out.println("Imput Text Summary: "+summarySize);
        System.out.println("Real Text Summary: "+summarySizeReal);
        
        double prop = 0;
        double prop1 = 0;
        
        for(int i=1040; i<1072; i++){
            prop =((double) realStatistic.get(i)) /((double) summarySizeReal);
            prop1 =((double) statistic.get(i)) /((double) summarySize);
          
           realstatisticProb.put(i, prop);
           statisticProb.put(i, prop1);
        }
        for(int i=1040; i<1072; i++){
      // System.out.println("Map key: "+i+"   word element: "+((char)i)+"  count: "+statistic.get(i));
       System.out.println("Map key: "+i+"   word element: "+((char)i)+"  count: "+realstatisticProb.get(i));
        }
        System.out.println();
        for(int i=1040; i<1072; i++){
      // System.out.println("Map key: "+i+"   word element: "+((char)i)+"  count: "+statistic.get(i));
       System.out.println("Map key: "+i+"   word element: "+((char)i)+"  count: "+statisticProb.get(i));
        }
        
   }
    
    private void printSortMap(){
        System.out.println();
         for(int i=1040; i<1072; i++){
      // System.out.println("Map key: "+i+"   word element: "+((char)i)+"  count: "+statistic.get(i));
       System.out.println("Map key: "+i+"   word element: "+((char)i)+"  count: "+realstatisticProb.get(i));
        }
    }
    private void printSortRMap(){
        System.out.println();
         for(int i=1040; i<1072; i++){
       System.out.println("Map key: "+i+"   word element: "+((char)i)+"  count: "+realStatistic.get(i));
        }
    }

    private void scanInputText() {
        int leng = cipro.length();
        char symbol;
        int coun;
        for(int i = 0; i< leng; i++){
            symbol = cipro.charAt(i);
            if(statistic.containsKey((int)symbol)){
             Integer count =  statistic.get((int)symbol);
             coun = count.intValue();
             coun++;
             statistic.put((int)symbol, coun);
            }
        }
    }
    
    private void scanRInputText(String s) {
        int leng = s.length();
        char symbol;
        int coun;
        for(int i = 0; i< leng; i++){
            symbol = s.charAt(i);
            if(realStatistic.containsKey((int)symbol)){
             Integer count =  realStatistic.get((int)symbol);
             coun = count.intValue();
             coun++;
             realStatistic.put((int)symbol, coun);
            }
        }
        
    }

    private void printExpectedText() {
        StringBuffer buff = new StringBuffer();
       int ind1;
       int ind2;
       HashMap map = new HashMap();
       for(int i=1040; i<1072; i++){
            
       ind1 =  getMaxElemInd(realstatisticProb);
       ind2 = getMaxElemInd(statisticProb);
       map.put(ind2, ind1);
       
       
       realstatisticProb.put(ind1, 0D);
       statisticProb.put(ind2, 0D);
        
        }
       
       char start;
       char end;
       
       for(int i=0; i< cipro.length(); i++){
           start = cipro.charAt(i);
           
           if(map.containsKey((int)start)){
           int charVal =  ((Integer) map.get((int)start)).intValue();
           end = (char)charVal;
       //   System.out.println("Start char: "+start+" End Char: "+end);
           buff.append(end);
           }else{
           buff.append(start);
           }
       }
       String expected =  buff.toString();
//        for(int i=1040; i<1072; i++){
//      // System.out.println("Map key: "+i+"   word element: "+((char)i)+"  count: "+statistic.get(i));
//       System.out.println("Map key: "+i+"   word element: "+((char)i)+"  count: "+realstatisticProb.get(i));
//        }
//        System.out.println();
//        for(int i=1040; i<1072; i++){
//      // System.out.println("Map key: "+i+"   word element: "+((char)i)+"  count: "+statistic.get(i));
//       System.out.println("Map key: "+i+"   word element: "+((char)i)+"  count: "+statisticProb.get(i));
//        }
        System.out.println("Expected text: ");
        System.out.println(expected);
    }
    
    private int getMaxElemInd(Map<Integer, Double> map){
        double max = 0;
        int index=0;
       Iterator iter = map.keySet().iterator();
       
       while(iter.hasNext()){
          Integer key = (Integer) iter.next();
          Double value = map.get(key);
          
          if(value >= max){
              max = value;
              index = key.intValue();
          }
       }
       
       
       System.out.println("Max ind: "+index+"  value: "+max);
       return index;
    }
}
