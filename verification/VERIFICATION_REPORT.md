# Verification report

- Verified source revision: `76b78067aba85d975b342f52be584325355d2fad`.
- Run date: 19 November 2026, Africa/Cairo (UTC+02:00).
- Suite start: 2026-11-19T11:05:28 EET (timestamp as recorded by TestNG).
- Suite end: 2026-11-19T11:05:58 EET (timestamp as recorded by TestNG).
- Host: Windows 11 amd64; Oracle JDK 25; Maven 3.9.16.
- Compiler target: Java 17. JDK 17 runtime was not separately exercised.
- Browser: Chrome 157.0.8103.51, headless; Selenium 4.53.0.
- REST Assured 6.3.0; TestNG 7.12.0; Jackson 2.23.0.
- Command: `mvn -B -ntp clean test`.
- Result: **BUILD SUCCESS; 5 tests, 0 failures, 0 errors, 0 skipped**.
- Total Maven time: 41.522 seconds; Surefire suite: 31.62 seconds.

| Test | Result | Test method duration (seconds) |
|---|---|---:|
| `createsBookAndEchoesPayload` | PASS | 5.133 |
| `getsExistingBook` | PASS | 0.435 |
| `rejectsMissingBook` | PASS | 0.363 |
| `displaysDynamicallyLoadedText` | PASS | 10.278 |
| `uploadsSmallImage` | PASS | 3.514 |

Method duration excludes some setup/teardown work; it need not sum to the suite duration.

## Evidence

- [TestNG report](testng-reports/index.html) and [compact report](testng-reports/emailable-report.html).
- [TestNG XML](testng-reports/testng-results.xml).
- [Maven execution log](maven-run.log).
- [Upload screenshot](evidence/ui/uploadsSmallImage-PASS.png).
- [Dynamic Loading screenshot](evidence/ui/displaysDynamicallyLoadedText-PASS.png).
- Actual HTTP request/response evidence is in `evidence/api/`.
- [Persistence diagnostic](persistence-probe.txt), separate from the five automated test cases.

The POST returned HTTP 200 with all submitted fields. A subsequent diagnostic GET for ID 501 returned HTTP 404. This confirms non-persistence for this observed run and supports the decision to validate the POST response independently.

## Verification limits

The required scenarios ran against the real public applications, without mocks or bypassed TLS checks. Screenshots were visually inspected. The final source was committed after the run without further source changes; the manifest records the delivered file bytes. `git diff --cached --check` passed and the Git bundle was verified.

Chrome was exercised; optional Edge support was not. No dedicated lint configuration, CI run, load test, security test, or cross-browser certification is claimed. These public services may change or become unavailable. Browser screenshots verify the requested visible result, not server-side file integrity.

TestNG reports are copied from the real run. Surefire system-property dumps and duplicate JUnit exports are omitted from the packaged evidence to avoid unnecessary host details; test statuses are unchanged.
