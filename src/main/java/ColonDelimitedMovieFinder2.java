import java.util.ArrayList;
import java.util.List;

public class ColonDelimitedMovieFinder2 {
    private String movieFile = "movies.txt";

    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();

        Movie movie1 = new Movie("zhang yi mou", "hong gao liang");
        Movie movie2 = new Movie("zhang yi mou", "qiu ju da guan si");
        Movie movie3 = new Movie("chen kai ge", "mei lan fang");

        movies.add(movie1);
        movies.add(movie2);
        movies.add(movie3);

        return movies;
    }
}