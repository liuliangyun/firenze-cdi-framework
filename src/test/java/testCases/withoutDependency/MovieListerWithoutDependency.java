package testCases.withoutDependency;

import annotations.Singleton;
import testCases.Movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class MovieListerWithoutDependency {
    public List<Movie> moviesDirectedBy(String director){
        List movies = new ArrayList<Movie>();
        List<Movie> allMovies = Arrays.asList(
                new Movie("zhang yi mou", "红高粱"),
                new Movie("chen kai ge", "梅兰芳")
        );
        for(Movie movie:allMovies)
            if(movie.getDirector().equalsIgnoreCase(director))
                movies.add(movie);

        return movies;
    }
}
