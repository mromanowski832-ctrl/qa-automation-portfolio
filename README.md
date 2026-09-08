# QA Automation Portfolio — Selenium · Java · TestNG

[![QA Automation](https://github.com/mromanowski832-ctrl/qa-automation-portfolio/actions/workflows/qa.yml/badge.svg?branch=main)](https://github.com/mromanowski832-ctrl/qa-automation-portfolio/actions/workflows/qa.yml)

Production-style UI test automation project built around a real e-commerce workflow.

The goal is not to demonstrate isolated WebDriver commands. The repository demonstrates how a maintainable QA automation framework can be structured: deterministic setup, explicit waits, Page Object Model, parallel-safe driver lifecycle, CI execution, failure evidence and optional BrowserStack execution.

## Stack

- Java 21
- Selenium WebDriver 4.48.0
- TestNG 7.12.0
- Maven
- Selenium Manager
- GitHub Actions
- BrowserStack Automate integration
- SauceDemo as the test application

## Test coverage

The current regression suite validates:

- successful authentication
- invalid credentials
- locked user behavior
- logout
- product sorting
- cart state and badge consistency
- add/remove product behavior
- complete checkout flow
- final order confirmation

## Engineering decisions

### No implicit waits

Implicit waits are disabled. All synchronization is handled through explicit waits in the page layer. This keeps timeout behavior predictable and prevents mixed-wait side effects.

### Parallel-safe WebDriver lifecycle

`ThreadLocal<WebDriver>` isolates browser sessions per TestNG execution thread. Every test method receives a fresh browser session and is fully cleaned up after execution.

### Page Object Model

Selectors and interaction logic stay inside page objects. Test classes express business scenarios instead of low-level browser commands.

### Failure evidence

On failure, the TestNG listener captures a timestamped screenshot into `screenshots/`. CI uploads screenshots and test reports as GitHub Actions artifacts.

### Local and cloud execution

The same suite can run:

- locally in Chrome
- locally in Microsoft Edge
- locally in Firefox
- remotely in BrowserStack Automate

No secrets are stored in the repository.

## Project structure

```text
.
├── .github/
│   └── workflows/
│       └── qa.yml
├── src/
│   └── test/
│       ├── java/pl/luzmen/qa/
│       │   ├── config/
│       │   ├── core/
│       │   ├── driver/
│       │   ├── listeners/
│       │   ├── pages/
│       │   └── tests/
│       └── resources/
│           └── config.properties
├── .gitignore
├── LICENSE
├── pom.xml
├── README.md
└── testng.xml
```

## Run locally

Requirements:

- JDK 21
- Maven 3.6.3+
- Chrome, Microsoft Edge or Firefox

Default configuration uses Microsoft Edge.

```powershell
mvn clean test
```

Run in Chrome:

```powershell
mvn clean test -Dbrowser=chrome
```

Run headless:

```powershell
mvn clean test -Dbrowser=edge -Dheadless=true
```

Run Firefox:

```powershell
mvn clean test -Dbrowser=firefox
```

Selenium Manager resolves the required browser driver automatically.

## Run on BrowserStack

Set credentials as environment variables.

PowerShell:

```powershell
$env:BROWSERSTACK_USERNAME="your_username"
$env:BROWSERSTACK_ACCESS_KEY="your_access_key"
mvn clean test -Drun.mode=browserstack -Dbrowser=edge -Dheadless=false
```

The framework sends project/build metadata to BrowserStack and marks the remote session as passed or failed through the BrowserStack executor.

## Configuration priority

Configuration is resolved in this order:

1. JVM system property (`-Dkey=value`)
2. environment variable (`KEY_NAME`)
3. `src/test/resources/config.properties`
4. framework default

Examples:

```powershell
mvn clean test -Dbrowser=chrome -Dheadless=true
mvn clean test -Drun.mode=browserstack -Dbrowser=edge
```

## CI

GitHub Actions runs the full regression suite on every push and pull request to `main`.

CI uses:

```text
Ubuntu latest
Java 21
Headless Chrome
Maven
```

Reports and screenshots are retained as workflow artifacts for debugging.

## Quality rules used in this project

- no `Thread.sleep()`
- no implicit waits
- no hardcoded local driver paths
- no credentials committed to source control
- independent tests
- fresh browser session per test
- explicit page-load assertions
- stable selectors where the application provides IDs or `data-test`
- screenshot evidence on test failure
- CI-ready execution
- optional cloud-grid execution

## Author

**Michał Romanowski**  
AI Tester · QA Automation · Selenium · Java · TestNG · BrowserStack · AI Evaluation

Built as a practical QA automation portfolio project under **LuzMen**.
