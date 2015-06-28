/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roc;

import java.util.ArrayList;

/**
 *
 * @author Michal
 */
public class users_movies{
        ArrayList<Integer> id_movies;
        int length;

        public users_movies(int new_movie_) {
             id_movies = new ArrayList<>();
            id_movies.add(new_movie_);
            length=1;
        }

        void add_movie(int new_movie){
            id_movies.add(new_movie);
            length++;
        }
        
        //cosine similarity
        double get_sim(users_movies um_){
            
            int concurrent=0;
            for(int id_movie:id_movies){
                if(um_.id_movies.contains(id_movie)){
                    concurrent++;
                }
            }
           
            return concurrent/(Math.sqrt(this.length)*Math.sqrt(um_.length));
                    
        }
        
    }
