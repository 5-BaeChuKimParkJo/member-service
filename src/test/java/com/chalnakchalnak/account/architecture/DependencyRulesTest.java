package com.chalnakchalnak.account.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class DependencyRulesTest {
  @Test
  void innerLayersDoNotDependOnTransportOrPersistence() {
    var classes =
        new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("com.chalnakchalnak.account");
    noClasses()
        .that()
        .resideInAnyPackage("..application..", "..domain..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(
            "..adapter..",
            "..infrastructure..",
            "..presentation..",
            "..vo..",
            "..entity..",
            "com.amazonaws..",
            "org.springframework.data..",
            "com.querydsl..")
        .check(classes);
    noClasses()
        .that()
        .resideInAPackage("..domain..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..application..")
        .check(classes);
    noClasses()
        .that()
        .resideInAPackage("..adapter.in..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("..adapter.out..", "com.amazonaws..", "org.springframework.data..")
        .check(classes);
  }

  @Test
  void ownedTypesUseRoleNamesInsteadOfLegacySuffixes() {
    var classes =
        new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("com.chalnakchalnak.account");
    for (String suffix : new String[] {"Dto", "Vo", "Impl", "Domain"}) {
      noClasses().should().haveSimpleNameEndingWith(suffix).check(classes);
    }
    noClasses().should().resideInAPackage("..mysql..").check(classes);
  }
}
