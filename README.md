# Dwalin

[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)


## About the project

Dwalin is a web-automation library for Java. It's a [Selenide](https://selenide.org) /
[Selenium-WebDriver](https://www.selenium.dev/documentation/webdriver) wrapper inspired by
[Geb](https://www.gebish.org) and its elder brother
[Balin](https://github.com/EPadronU/balin).

Contrary to a framework, Dwalin is **lightweight** and **non-opinionated**. Additionally, its
supports the usage of the
[Page Component Object Model](https://gorillalogic.com/blog/test-automation-frameworks-page-object-model-vs-page-component-object-model)
design pattern, an extension of the well-known
[<abbr title="Page Object Model">POM</abbr>](https://www.guru99.com/page-object-model-pom-page-factory-in-selenium-ultimate-guide.html)
design pattern.


## Rationale

Geb, written in Groovy, is a fantastic library that lets users harness the power of the
Selenium-WebDriver API with ease. On the Java side, Selenide offers many of the same benefits as
Geb. As a software developer focused on web-based test automation, I've had the privilege of
working with both tools. This inspired me to create a smoother experience, leading to the
development of Balin in Kotlin a few years ago. Now, with Dwalin, I'm excited to bring what I've
learned from Balin into the Java world.


## Usage

[Sample tests](./src/test/java/com/github/epadronu/dwalin/SampleTests.java)


### Templates

TODO


## Documentation

- [Java API](https://epadronu.github.io/dwalin/apidocs/index.html)


## Using the library in your project

### Maven

```xml
<dependency>
    <groupId>io.github.epadronu</groupId>
    <artifactId>dwalin</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'io.github.epadronu:dwalin:0.1.0'
```


## Notes

This project has been conceived as a hobby and for personal use. Nonetheless, I don't dismiss the
possibility of making it a production-ready library if it gets to that point.

## License

Like Balin, Dwalin is released under version 2.0 of the [Apache License](LICENSE).
