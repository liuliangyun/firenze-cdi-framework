package testCases.injectInterfaceDependency;

import annotations.Inject;
import annotations.Named;
import testCases.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieListerInjectNamedInterfaceDependency {
    @Inject
    @Named("database")
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
