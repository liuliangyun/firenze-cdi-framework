package testCases.injectClass;

import annotations.Inject;
import testCases.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieListerInjectClass {
    @Inject
    private MovieFinder finder;

    public List<Movie> moviesDirectedBy(String director){
        List<Movie> movies = new ArrayList<>();
        List<Movie> allMovies = finder.findAll();
        for(Movie movie:allMovies)
            if(movie.getDirector().equalsIgnoreCase(director))
                movies.add(movie);

        return movies;
    }

    public MovieFinder getFinder() {
        return finder;
    }
}
