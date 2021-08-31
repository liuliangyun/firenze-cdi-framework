package testCases.circularDependency;

import annotations.Inject;

public class MovieListerAInjectB {
    @Inject
    public MovieListerBInjectA listerB;

}
