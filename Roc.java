/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roc;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Michal Kubica
 */
public class Roc {

    /**
     * @param args the command line arguments
     */
    
    
    public static Map sortByValue(Map unsortMap) {	 
        List list = new LinkedList(unsortMap.entrySet());

        Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                        return ((Comparable) ((Map.Entry) (o2)).getValue())
                                                .compareTo(((Map.Entry) (o1)).getValue());
                }
        });

        Map sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    
    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException {

        Scanner in = new Scanner(new FileReader("C:\\roc\\views.csv"));
        
        HashMap<String,users_movies> main_map=new HashMap<>();
        
        String[] line_;
        //fill hashamp
        while(in.hasNext()){
            line_=in.nextLine().replace("\"","").split(",");
            if(main_map.containsKey(line_[0])){
                main_map.get(line_[0]).add_movie(Integer.parseInt(line_[1]));
            }else{         
                main_map.put(line_[0],new users_movies(Integer.parseInt(line_[1])) );
            }
        }///fill hashmap 
        in.close();
        
        //treemap podobnosti uzivatela
        TreeMap<Double,String> tm = new TreeMap<>(Collections.reverseOrder());
        Iterator it = main_map.entrySet().iterator();
        
        //
        users_movies    cur_user_movies=null;
        //id uzivatela, re ktoreho budeme odporucat filmy
        String          cur_user_id;
        
        boolean i=true;
        for (Map.Entry<String, users_movies> entry : main_map.entrySet()) {
            //Pre prveho uzivatela budeme odporucat filmy.
            if(i){
                cur_user_movies = entry.getValue();
                cur_user_id = entry.getKey();
                i=false;
                
                //Dysplay only
                System.out.println("---------------");
                for(int id_:cur_user_movies.id_movies){
                    System.out.print(id_+",");
                }
                System.out.println();
                System.out.println("---------------");
                ///Dysplay only
                
                continue;
                
            }
            //vypocet podobnsoti prveho uzivatela z ostatnimi.
            tm.put(cur_user_movies.get_sim(entry.getValue()),entry.getKey());
            
        }
        
        //vysledna hashmapa filmov, ktore budeme odporucat
        HashMap<Integer,Integer> result_tm= new HashMap<>();
        
        Set set = tm.entrySet();
        it = set.iterator();
        int ii=0;
        
        users_movies cur_res_movies =null;
        
        
        for (Map.Entry<Double, String> entry : tm.entrySet()) {
            System.out.println(entry.getKey()+entry.getValue());
            //len pre 40 najopdobnejsich uzivatelov
            //je to treemap zotriedeny reersne, takze prvych 40 je najpodobnejsich 
            if(ii==40) break;
            ii++;
            
            cur_res_movies = main_map.get(entry.getValue());
            
            
            //vysledne scitanie odporucani pre filmy 
            for(int movie_id:cur_res_movies.id_movies){
                if(result_tm.containsKey(movie_id)){
                    result_tm.put(movie_id,result_tm.get(movie_id)+1);
                }else{
                    result_tm.put(movie_id, 1);
                }
            }
            
        }
        
        result_tm=(HashMap<Integer, Integer>) sortByValue(result_tm);
        
        //Vypis odporucencyh filmov
        for (Map.Entry<Integer, Integer> entry : result_tm.entrySet()) {
            System.out.println("key: "+entry.getKey()+": value:"+entry.getValue());
            System.out.print(entry.getKey()+",");
            
        }
        
    }
}
