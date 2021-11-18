package testCases.circularDependency;

import cdi.annotations.Inject;

public class MovieListerA {
    @Inject
    private MovieListerB listerB;

}
