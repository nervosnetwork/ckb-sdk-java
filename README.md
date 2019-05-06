# CKB SDK Java

[![Build Status](https://travis-ci.com/nervosnetwork/ckb-sdk-java.svg?branch=develop)](https://travis-ci.com/nervosnetwork/ckb-sdk-java)

Java SDK for Nervos [CKB](https://github.com/nervosnetwork/ckb).

You can generate the jar and import manually.
```
git clone https://github.com/nervosnetwork/ckb-sdk-java.git
gradle shadowJar  // ./gradlew shadowJar 
```
You will get `console-{version}-all.jar` from console module.

### How to Develop

We use [Google Java Code Format](https://google.github.io/styleguide/javaguide.html#s4.5-line-wrapping) and [google checkstyle](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml) to develop.

If `verifyGoogleJavaFormat FAILED` happens when you build this project, please format your code with [Google Java Code Format](https://google.github.io/styleguide/javaguide.html#s4.5-line-wrapping)
or execute `./gradlew goJF` on macOS and Linux,  or `gradlew goJF` on Windows.

If you use IntelliJ IDEA to develop, you can install `google-java-format` plugin to format code automatically.

## License

The SDK is available as open source under the terms of the [MIT License](https://opensource.org/licenses/MIT).

## Changelog

See [CHANGELOG](CHANGELOG.md) for more information.

