package testCases.circularDependency;

import cdi.annotations.Inject;

public class MovieListerB {
    @Inject
    private MovieListerA listerA;

}
