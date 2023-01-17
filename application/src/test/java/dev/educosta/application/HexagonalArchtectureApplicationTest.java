package dev.educosta.application;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Ensure Architecture contraints
 */
@AnalyzeClasses(packages = "dev.educosta.application")
public class HexagonalArchtectureApplicationTest {

    @ArchTest
    static final ArchRule interfaces_must_not_be_placed_in_implementation_packages =
            noClasses().that().resideInAPackage("..input..").should().beInterfaces();
    @ArchTest
    static final ArchRule package_output_should_have_only_interface =
            classes().that().resideInAPackage("..output..").should().beInterfaces()
                    .andShould().haveSimpleNameEndingWith("OutputPort");
    @ArchTest
    static final ArchRule package_usecases_should_have_only_interface =
            classes().that().resideInAPackage("..usecases..").should().beInterfaces()
                    .andShould().haveSimpleNameEndingWith("UseCase");

}
