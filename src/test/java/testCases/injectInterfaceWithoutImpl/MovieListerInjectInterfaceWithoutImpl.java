package testCases.injectInterfaceWithoutImpl;

import cdi.annotations.Inject;
import testCases.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieListerInjectInterfaceWithoutImpl {
    @Inject
    private MovieFinderInterfaceWithoutImpl finder;

    public List<Movie> moviesDirectedBy(String director){
        List movies = new ArrayList<Movie>();
        List<Movie> allMovies = finder.findAll();
        for(Movie movie:allMovies)
            if(movie.getDirector().equalsIgnoreCase(director))
                movies.add(movie);

        return movies;
    }

    public MovieFinderInterfaceWithoutImpl getFinder() {
        return finder;
    }
}
