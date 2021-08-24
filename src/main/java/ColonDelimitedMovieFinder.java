import annotations.Inject;

import java.util.List;

public class ColonDelimitedMovieFinder implements MovieFinder {

    @Inject
    ColonDelimitedMovieFinder2 finder;

    @Override
    public List<Movie> findAll() {
        return finder.findAll();
    }
}
