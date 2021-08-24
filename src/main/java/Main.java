import java.util.List;

public class Main {
    public static void main(String[] args){
        Container container = configureContainer();
        MovieLister lister = (MovieLister)container.getComponent(MovieLister.class);
        List<Movie> movies = lister.moviesDirectedBy("zhang yi mou");

        for(Movie movie:movies) {
            System.out.println(movie.getTitle());
        }
    }

    private static Container configureContainer() {
        Container container = new FirenzeContainer();
        container.registerComponent(MovieFinder.class, ColonDelimitedMovieFinder.class, null);
        container.registerComponent(MovieLister.class);
        return container;
    }
}
