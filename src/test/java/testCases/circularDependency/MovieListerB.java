package testCases.circularDependency;

import annotations.Inject;

public class MovieListerB {
    @Inject
    public MovieListerA listerA;

}
