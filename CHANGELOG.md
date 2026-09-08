# Changelog

All notable changes to this project are documented in this file.

## v1.0.0 — Portfolio Release

### Added

- Java 21 + Selenium WebDriver + TestNG automation framework
- Page Object Model architecture
- Parallel-safe `ThreadLocal<WebDriver>` driver lifecycle
- Local execution in Chrome, Microsoft Edge and Firefox
- BrowserStack-ready remote execution without committed secrets
- GitHub Actions CI for pushes and pull requests to `main`
- Automatic Surefire/TestNG report upload from CI
- Automatic screenshot capture and upload for failed tests
- PowerShell test runner for local execution
- MIT license and professional project documentation

### Test coverage

- successful login
- invalid credentials
- locked-out user behavior
- logout
- product sorting
- cart badge/state validation
- add/remove product behavior
- end-to-end checkout
- final order confirmation

### Quality characteristics

- no `Thread.sleep()`
- no implicit waits
- explicit synchronization in page objects
- no hardcoded local WebDriver paths
- no credentials committed to source control
- fresh browser session per test
- CI execution in headless Chrome
- stable selectors where IDs or `data-test` attributes are available

### Validation

The `QA Automation` GitHub Actions workflow passed successfully on the `main` branch before this release was prepared.
