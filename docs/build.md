# Build and publish

Follow these steps to build Kefir locally and, if needed, publish artifacts to your local Maven repository.

Prerequisites:

- JDK 17 or newer
- Git
- Internet access to fetch dependencies

1) Clone

```shell
git clone https://github.com/kshulzh/Kefir.git
```

2) Build Kefir

- From this repository root, run:
    - Windows: gradlew build
    - macOS/Linux: ./gradlew build

Notes:

- After publishing to mavenLocal, artifacts are available under your local Maven repository (usually ~/.m2/repository).
- Run gradlew test to execute tests.