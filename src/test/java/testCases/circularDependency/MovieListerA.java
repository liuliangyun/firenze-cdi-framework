package testCases.circularDependency;

import annotations.Inject;

public class MovieListerA {
    @Inject
    public MovieListerB listerB;

}
