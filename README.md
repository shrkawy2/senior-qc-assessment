# Senior Software QC Automation Assessment

A small Java automation project covering the requested two browser scenarios and three Books API scenarios. It uses Selenium WebDriver, REST Assured, TestNG, and Maven. Page Objects encapsulate browser interactions; a Books service encapsulates API requests. Test assertions remain in the test layer.

## Prerequisites

- JDK 17 or newer, with `java` and `javac` on PATH.
- Maven 3.9 or newer on PATH.
- Chrome installed for the default UI run. Edge is also implemented as an optional browser.
- Internet access to Maven Central, browser-driver downloads, and both public test applications.

Dependency versions are pinned in `pom.xml`. Selenium Manager resolves the browser driver automatically; the first run may take longer while dependencies and drivers are downloaded. No credentials are required for the test applications.

## Run

Open a terminal **inside the directory containing `pom.xml`**:

```sh
mvn -B -ntp clean test
```

Useful alternatives (quote `-D` arguments in PowerShell):

```sh
mvn -B -ntp test "-Dgroups=api"
mvn -B -ntp test "-Dgroups=ui"
mvn -B -ntp test "-Dgroups=ui" "-Dheadless=false"
mvn -B -ntp test "-Dgroups=ui" "-Dbrowser=edge"
mvn -B -ntp test "-Dtest=UiTests#uploadsSmallImage"
mvn -B -ntp test "-Dwait.seconds=30"
```

Defaults live in `src/test/resources/config.properties`. Java system properties override them, for example `"-Dapi.baseUrl=https://fakerestapi.azurewebsites.net"`. A nonstandard browser executable can be selected with `"-Dbrowser.binary=C:/path/to/chrome.exe"`.

The default run is sequential and headless. Each UI method gets a new browser and guaranteed teardown. Do not enable TestNG method/data-provider parallelism without replacing the instance driver/service state with per-test state. Headless Chrome is the required validation target; optional Edge support must be verified separately before relying on it.

## Coverage and assertions

| ID | Test | Verification |
|---|---|---|
| UI-01 | `uploadsSmallImage` | Navigate from home, upload the bundled PNG, submit; verify `File Uploaded!` and the exact filename. |
| UI-02 | `displaysDynamicallyLoadedText` | Navigate from home to Dynamic Loading / Example 2, click Start; wait for the newly inserted result and the loader to disappear; assert `Hello World!`. |
| API-01 | `getsExistingBook` | GET book 1: HTTP 200, JSON content type and schema, expected ID/title/page count, non-empty description/excerpt, parseable timestamp. |
| API-02 | `createsBookAndEchoesPayload` | POST a synthetic book: HTTP 200, JSON schema, equality of every payload field; compare publication dates as instants. |
| API-03 | `rejectsMissingBook` | GET a missing but valid int32 ID: HTTP 404, problem+json, matching status/title and non-empty type/trace ID. |

The negative case distinguishes a missing resource from a malformed ID. `2147483647` is within the API's int32 contract but outside its current generated book set. If the provider changes its dataset, this assumption should be reviewed rather than silently relaxing the assertion.

## Project layout

```text
src/test/java/com/assessment/
  config/TestConfig.java       configuration and validation
  data/TestData.java           classpath JSON and fixture loading
  ui/DriverFactory.java       browser options and creation
  ui/pages/                   Page Objects and explicit waits
  ui/tests/                   UI assertions, lifecycle and screenshots
  api/model/Book.java          immutable request model
  api/service/                request specification, endpoints and evidence
  api/tests/                  API assertions and TestNG data providers
src/test/resources/
  config.properties           overridable environment settings
  data/                       UI and API input/expected data
  files/sample-image.png       tiny synthetic upload fixture
  schemas/book.schema.json     response contract
```

All code is test code, so it intentionally lives under `src/test`; no production application or empty framework layers are introduced. Fluent Page Object transitions make a scenario read in user-action order. The API tests use REST Assured's fluent assertions instead of adding an unnecessary assertion wrapper.

## Reports and evidence

After execution:

- `target/surefire-reports/index.html`: TestNG report.
- `target/surefire-reports/emailable-report.html`: compact shareable report.
- `target/surefire-reports/testng-results.xml` and `TEST-*.xml`: machine-readable results.
- `target/evidence/ui/`: success/failure screenshots and browser/URL metadata.
- `target/evidence/api/`: actual request payloads, response status/content type and response bodies.

`mvn clean` removes previous results. Archived submission evidence, when supplied, is separate from `target/`; it is evidence for its recorded revision and environment, not a promise that a public service will never change.

## Important service behavior

FakeRESTApi is a simulation. Creating a book may echo the submitted object without persisting it. Consequently, the create test validates the POST response and does not chain GET/update/delete calls that assume saved state. HTTP 200 is intentional for this service; a real creation API may specify 201 instead. Generated GET publication dates can vary, so their exact value is not hard-coded.

The response schema follows the published Book property types and nullability, and requires the six fields expected in these responses. Nullable schema properties are not automatically a valid business outcome: the existing-book test also checks useful non-empty values. Additional properties fail the schema intentionally to flag contract drift.

See `docs/TEST_STRATEGY.md` for design decisions, tradeoffs, and execution limitations. See the accompanying verification report for measured results.

## Troubleshooting

- **No POM found:** change into this project's root before running Maven.
- **Driver download or proxy failure:** check network/proxy settings and browser installation. No TLS verification is disabled by the suite.
- **A UI wait times out:** inspect its screenshot and URL; confirm the public application is reachable. Adjust the configured wait only when the environment warrants it.
- **Unexpected API status/body:** inspect the saved request/response. Do not change the expected result merely to get a green run.
- **Java compilation fails:** verify `mvn -version` uses the intended JDK, not a JRE or older Java installation.

No blanket retries, fixed sleeps, test ordering dependencies, or shared global REST Assured base URL are used.

## References

- [Assessment UI application](https://the-internet.herokuapp.com/)
- [FakeRESTApi Swagger](https://fakerestapi.azurewebsites.net/index.html)
- [FakeRESTApi OpenAPI document](https://fakerestapi.azurewebsites.net/swagger/v1/swagger.json)
- [Selenium waiting strategies](https://www.selenium.dev/documentation/webdriver/waits/)
- [REST Assured documentation](https://rest-assured.io/docs/usage/)
