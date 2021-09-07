package testCases.injectInterfaceDependency;

import annotations.Inject;
import testCases.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieListerInjectInterfaceDependency {
    @Inject
    private MovieFinderInterface finder;

    public List<Movie> moviesDirectedBy(String director){
        List movies = new ArrayList<Movie>();
        List<Movie> allMovies = finder.findAll();
        for(Movie movie:allMovies)
            if(movie.getDirector().equalsIgnoreCase(director))
                movies.add(movie);

        return movies;
    }

    public MovieFinderInterface getFinder() {
        return finder;
    }
}
