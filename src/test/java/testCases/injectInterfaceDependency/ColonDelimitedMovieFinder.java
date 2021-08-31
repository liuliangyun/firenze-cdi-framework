package testCases.injectInterfaceDependency;

import testCases.Movie;

import java.util.ArrayList;
import java.util.List;


public class ColonDelimitedMovieFinder implements MovieFinderInterface {

    @Override
    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();

        Movie movie1 = new Movie("zhang yi mou", "红高粱");
        Movie movie2 = new Movie("zhang yi mou", "秋菊打官司");
        Movie movie3 = new Movie("zhang yi mou", "大红灯笼高高挂");
        Movie movie4 = new Movie("chen kai ge", "mei lan fang");

        movies.add(movie1);
        movies.add(movie2);
        movies.add(movie3);
        movies.add(movie4);

        return movies;
    }
}
