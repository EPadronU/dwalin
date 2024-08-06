# Dwalin

[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

Dwalin is a web-automation library for Java. It's a [Selenide](https://selenide.org) /
[Selenium-WebDriver](https://www.selenium.dev/documentation/webdriver) wrapper inspired by
[Geb](https://www.gebish.org) and its elder brother
[Balin](https://github.com/EPadronU/balin).

Contrary to a framework, Dwalin is **lightweight** and **non-opinionated**. Additionally, it
supports the usage of the [Page Component Object Model](https://gorillalogic.com/blog/test-automation-frameworks-page-object-model-vs-page-component-object-model)
design pattern, an extension of the well-known [<abbr title="Page Object Model">POM</abbr>](https://www.guru99.com/page-object-model-pom-page-factory-in-selenium-ultimate-guide.html)
design pattern.

## Rationale

Geb, written in Groovy, is a fantastic library that lets users harness the power of the
Selenium-WebDriver API with ease. On the Java side, Selenide offers many of the same benefits as
Geb. As a software developer focused on web-based test automation, I've had the privilege of
working with both tools. This inspired me to create a smoother experience, leading to the
development of Balin in Kotlin a few years ago. Now, with Dwalin, I'm excited to bring what I've
learned from Balin into the Java world.

## Usage

TODO


### Templates

TODO


## Documentation

- [Java API](https://epadronu.github.io/dwalin/apidocs/index.html)


## Build system & framework integrations

### Maven

```xml
TODO
```

### Gradle

```groovy
TODO
```


## Note

This project has been conceived as a hobby and for personal use. Nonetheless, I don't dismiss the
possibility of making it a production-ready library if it gets to that point.

## License

Like Balin, _Dwalin_ is released under version 2.0 of the [Apache License](LICENSE).
