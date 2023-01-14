package dev.educosta.domain.specification;

import dev.educosta.domain.exception.GenericSpecificationException;
import dev.educosta.domain.specification.shared.AbstractSpecification;

public class EmptyCityNameSpec extends AbstractSpecification<String> {

    @Override
    public boolean isSatisfiedBy(String s) {
        return s == null || s.trim().isEmpty();
    }

    @Override
    public void check(String s) throws GenericSpecificationException {
        if (this.isSatisfiedBy(s)) {
            throw new GenericSpecificationException("Invalid city name: "+s);
        }
    }
}
