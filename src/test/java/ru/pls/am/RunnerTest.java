package ru.pls.am;import cucumber.api.CucumberOptions;import cucumber.api.SnippetType;import cucumber.api.junit.Cucumber;import org.junit.runner.RunWith;@RunWith(Cucumber.class)@CucumberOptions(        features = "src/test/resources/ru/pls/am/features",        glue = "ru.pls.am.stepdefs",        tags = "@all",//        dryRun = true,        strict = true,        snippets = SnippetType.CAMELCASE//        name = "^Успешное|Успешная.*")public class RunnerTest {}