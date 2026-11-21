# Test strategy and assessment traceability

## Scope

The deliverable covers exactly two required UI scenarios and two positive plus one negative Books API scenario. Implementing all five listed Books endpoints is not required by the assessment; GET by ID and POST are sufficient for the chosen scenarios. Broader CRUD, authentication, load, security, and cross-browser certification are outside this submission's scope.

## Design choices

| Requirement | Implementation |
|---|---|
| Java, Selenium / REST Assured, TestNG, Maven | Pinned dependencies and build configuration in `pom.xml`. |
| Modular UI design | One Page Object per screen/state, shared wait helpers in `BasePage`. |
| Reusable API design | `BooksService` owns endpoints and a reusable request specification; the evidence filter handles diagnostics. |
| Fluent design | User-flow page transitions and REST Assured `then()` assertions. |
| OOP | Encapsulated page locators, a base page for common browser behavior, composition of services in tests, immutable `Book` record. |
| Externalized inputs | JSON data and classpath PNG fixture; TestNG DataProviders supply API cases. |
| Avoid unnecessary hard-coding | Base URLs, browser, timeouts and expected data are configurable; endpoint paths and selectors remain intentional application contracts. |
| Meaningful assertions | Exact UI messages/filename, API schema/status/content type and business-field checks. |
| Synchronization | Explicit condition-based waits; no implicit wait or `Thread.sleep`. |
| Git and run instructions | Local Git history/bundle and README; remote publication is a separate step requiring account access. |

## Isolation and reliability

Each UI method starts a fresh browser. The browser is closed in an always-run teardown even if assertion or evidence capture fails. Each API method gets a fresh service/request specification, and tests do not depend on execution order. The suite runs sequentially; current instance fields do not support method-level parallelism.

Example 2 inserts its result only after Start is clicked. We find and wait for that result after the click rather than cache a WebElement before it exists. We also wait for the loader to be hidden. Waiting only for loader invisibility could return early if the loader had not yet been inserted.

The upload test sends an absolute path to the HTML file input. It does not automate a native OS file picker, which would be less portable. Verification checks the application's success heading and filename, as requested; it does not claim to verify server-side byte integrity or malware scanning.

The POST payload is serialized by Jackson, not by string concatenation. Comparing timestamps as instants accepts equivalent offsets while rejecting a different time. Schema validation checks structure/types; field assertions check semantics.

## Evidence and failures

TestNG/Surefire reports are the source of test pass/fail status. Screenshots provide browser context for passing and failing UI methods. The API filter records actual HTTP responses including non-2xx responses. A transport exception before a response arrives is reported in Surefire; there is no fabricated response artifact.

Evidence contains synthetic public test data only. If authentication or personal data is introduced later, redact sensitive fields before recording them. The filter is designed for this unauthenticated assessment service and is not a generic production logging solution.

There are no automatic retries. A failed public-service run remains a failure and requires diagnosis. Review HTTP status/body or the screenshot before deciding whether the issue is code, data, environment, or upstream behavior.

## API assumptions and observations

- Swagger exposes Book IDs as signed int32 values. The missing-ID case uses a valid maximum int32 value rather than an invalid text ID.
- The public API returns a problem-details response for a missing Book. The test checks status/title and the existence of diagnostic fields, not a changing trace ID value.
- The create contract here is HTTP 200 plus the echoed Book. Persistence is not assumed.
- Publication dates from GET are generated and time-dependent. The suite checks timestamp validity instead of a fixed date.
- Exact observed responses and any additional diagnostic probes are recorded with the final verification evidence. Do not infer persistence from a successful POST alone.

## Future extensions if the project grows

Introduce per-thread/per-invocation driver and service state before parallelizing; add authenticated request handling only when needed; add update/delete coverage with simulation-aware expectations; add CI after choosing a repository and controlled execution environment. Avoid extra abstractions until there is a demonstrated repeated use case.
