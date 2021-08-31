package testCases.circularDependency;

import annotations.Inject;

public class MovieListerBInjectA {
    @Inject
    public MovieListerAInjectB listerA;

}
