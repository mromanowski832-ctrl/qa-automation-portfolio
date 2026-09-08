param(
    [ValidateSet("edge", "chrome", "firefox")]
    [string]$Browser = "edge",

    [switch]$Headless,

    [ValidateSet("local", "browserstack")]
    [string]$RunMode = "local"
)

$ErrorActionPreference = "Stop"

$headlessValue = if ($Headless) { "true" } else { "false" }

Write-Host "Running LuzMen QA Automation Portfolio"
Write-Host "Browser: $Browser"
Write-Host "Headless: $headlessValue"
Write-Host "Run mode: $RunMode"

mvn clean test `
    "-Dbrowser=$Browser" `
    "-Dheadless=$headlessValue" `
    "-Drun.mode=$RunMode"

if ($LASTEXITCODE -ne 0) {
    throw "Test execution failed with exit code $LASTEXITCODE"
}
