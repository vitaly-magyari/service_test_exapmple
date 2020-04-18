# API testing showcase

Implemented originally as test task.

## Installation

Requires 3rd party token for the API which I do not provide. If you have one, add it into `src/test/resources/config.properties`

Maybe will port the API to flask eventually or something and update this accordingly

## Usage
Check that `baseURI` and `token` in `config.properties` are correct. 

Then from project root:

```bash
./gradlew test
```
Pray.

Report from test runs are in 

```bash
$PROJECT_ROOT/build/reports/tests/test/index.html
```

## License
[MIT](https://choosealicense.com/licenses/mit/)
