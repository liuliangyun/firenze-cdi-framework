package testCases.injectInterfaceWithMultiImpl;

import annotations.Named;
import testCases.Movie;

import java.util.ArrayList;
import java.util.List;

@Named("database")
public class MovieFinderImpl2 implements MovieFinderInterfaceWithMultiImpl {
    @Override
    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();

        Movie movie1 = new Movie("zhang yi mou", "红高粱");
        Movie movie2 = new Movie("zhang yi mou", "秋菊打官司");
        Movie movie3 = new Movie("zhang yi mou", "大红灯笼高高挂");
        Movie movie4 = new Movie("zhang yi mou", "我的父亲母亲");
        Movie movie5 = new Movie("chen kai ge", "梅兰芳");

        movies.add(movie1);
        movies.add(movie2);
        movies.add(movie3);
        movies.add(movie4);
        movies.add(movie5);

        return movies;
    }
}
