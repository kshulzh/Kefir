# IntelliJ IDEA plugin plan

Goal: Provide an IDEA plugin that visualizes and synchronizes Kefir-generated sources with the project, and optionally
assists FIR stubs generation.

Key concepts:

- generated.file / generated.dir — A location (file or directory) that contains declarations created/modified by Kefir.
- Synchronization — The plugin watches generated locations and updates IDE views (Project, Structure, Navigation)
  accordingly.
- FIR integration — Optionally generate or refresh FIR-level stubs from generated sources to improve IDE features like
  navigation and code completion.

Responsibilities:

- Detect and index generated sources.
- Offer a toolwindow to inspect generated declarations and their origins.
- Provide navigation: from generated elements back to processor/DSL calls when possible.
- Trigger on-demand refresh (e.g., "Regenerate" button) and support auto-sync on file changes.
- Optional: generate/update FIR stubs from generated.file to provide richer IDE assistance.

Implementation notes:

- Use Gradle project settings (or kefir-gradle-plugin) to locate generated directories.
- Rely on VirtualFile listeners to track updates, with debouncing.
- Keep the plugin lightweight; avoid full builds; prefer reading text outputs when possible.

Acceptance criteria:

- Generated files are visible and indexed in the IDE without manual configuration.
- Users can navigate between generated code and its origin.
- Manual and automatic refresh work reliably.
- (Optional) FIR stub generation can be enabled/disabled and works on a sample project.