package testCases.circularDependency;

import annotations.Inject;

public class MovieListerB {
    @Inject
    private MovieListerA listerA;

}
