package testCases.injectClass;

import testCases.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieFinder {
    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();

        Movie movie1 = new Movie("zhang yi mou", "红高粱");
        Movie movie2 = new Movie("zhang yi mou", "秋菊打官司");
        Movie movie3 = new Movie("chen kai ge", "梅兰芳");

        movies.add(movie1);
        movies.add(movie2);
        movies.add(movie3);

        return movies;
    }
}
