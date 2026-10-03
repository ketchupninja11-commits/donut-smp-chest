@echo off
where gradle >nul 2>nul
if errorlevel 1 (
  echo Gradle is not installed. For GitHub Actions, use the included workflow.
  echo For a local build, install Gradle 9.1 or generate a standard Gradle wrapper.
  exit /b 1
)
gradle %*
