## Development

### Code Lint

We use [checkstyle](https://github.com/checkstyle/checkstyle)
with [Google java Style](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml) to
lint our code. Code lint is not compulsory before your commit or pull request, but it is highly recommended. You could
run `./gradlew checkstyle` to check the code style.

### New commit
We add hook [pre-commit](./config/pre-commit) to run `./graldew build` before each commit to make sure code won't be broken. It takes a bit time and please be patient.

## Release

We follow these steps as release flow
1. Label each PR according to branch name by GitHub Actions. If it does not work as expected, please create an issue for it.  
2. Add and push tag to main branch (`develop`). Once a new tag is detected, GitHub Actions will generate a release draft (thanks to `release-drafter` action`, and upload artifacts to Maven Central Repository.
3. Manually update CHANGELOG.md, publish release to GitHub and publish artifacts in Maven Central Repository.

