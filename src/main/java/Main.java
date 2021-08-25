import java.util.List;

public class Main {
    public static void main(String[] args){
        Container container = configureContainer();
        MovieLister lister = (MovieLister)container.getComponent(MovieLister.class);
        MovieLister lister2 = (MovieLister)container.getComponent(MovieLister.class);
        List<Movie> movies = lister.moviesDirectedBy("zhang yi mou");
        List<Movie> movies2 = lister2.moviesDirectedBy("zhang yi mou");

        for(Movie movie:movies) {
            System.out.println(movie.getTitle());
        }

        for(Movie movie:movies2) {
            System.out.println(movie.getTitle());
        }

        System.out.println(lister.finder.hashCode());
        System.out.println(lister2.finder.hashCode());
    }

    private static Container configureContainer() {
        Container container = new FirenzeContainer();
        container.registerImplementation(MovieFinder.class, ColonDelimitedMovieFinder.class);
        container.registerImplementation(MovieFinder.class, DatabaseMovieFinder.class);
        return container;
    }
}
