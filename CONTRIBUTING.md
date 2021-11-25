
## Development

### Code Lint
We
use [Google Java Code Format](https://google.github.io/styleguide/javaguide.html#s4.5-line-wrapping)
and
follow [Google Checkstyle](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml)
for development.

Before building task `./gradlew build`, task `verifyGoogleJavaFormat` will be executed ahead to check code style. If you run into `verifyGoogleJavaFormat FAILED` please run `./gradlew goJF` to lint code.

### New commit
We add hook [pre-commit](./config/pre-commit) to run `./graldew build` before each commit to make sure code won't be broken. It takes a bit time and please be patient.

## Release

We follow these steps as release flow
1. Label each PR according to branch name by GitHub Actions. If it does not work as expected, please create an issue for it.  
2. Add and push tag to main branch (`develop`). Once a new tag is detected, GitHub Actions will generate a release draft (thanks to `release-drafter` action`, and upload artifacts to Maven Central Repository.
3. Manually update CHANGELOG.md, publish release to GitHub and publish artifacts in Maven Central Repository.

