package dev.educosta.application;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.simpleNameEndingWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * Ensure Architecture constraints
 */
@AnalyzeClasses(
        packages = "dev.educosta.application",
        importOptions = {ImportOption.DoNotIncludeTests.class}
)
public class HexagonalArchtectureApplicationTest {

    @ArchTest
    static final ArchRule input_ports_should_have_specific_format =
            classes().that().resideInAPackage("..input..").should().notBeInterfaces()
                    .andShould().bePackagePrivate()
                    .andShould().implement(simpleNameEndingWith("UseCase"));

    @ArchTest
    static final ArchRule output_ports_should_have_specific_format =
            classes().that().resideInAPackage("..output..").should().beInterfaces()
                    .andShould().bePublic()
                    .andShould().haveSimpleNameEndingWith("OutputPort");
    @ArchTest
    static final ArchRule use_cases_ports_should_have_specific_format =
            classes().that().resideInAPackage("..usecases..").should().beInterfaces()
                    .andShould().bePublic()
                    .andShould().haveSimpleNameEndingWith("UseCase");

}
