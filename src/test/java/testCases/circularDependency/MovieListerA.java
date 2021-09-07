package testCases.circularDependency;

import annotations.Inject;

public class MovieListerA {
    @Inject
    private MovieListerB listerB;

}
