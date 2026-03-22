---
name: spec-driven-init
description: Initialize a .spec-driven/ directory in a project. Creates config.yaml and specs/ scaffold, then guides the user to fill in project context.
version: 0.1.0
---

You are helping the user initialize the spec-driven workflow in a project.

## Steps

1. **Confirm the target project** — ask which project to initialize. If the user is already in the project root, use the current directory. Accept either `.` or an explicit path.

2. **Run init** — run:
   ```
   node .agent/skills/spec-driven-init/scripts/spec-driven.js init [path]
   ```
   Pass the path only if it differs from the current directory.

3. **Guide config.yaml setup** — read the generated `.spec-driven/config.yaml`. Ask the user to describe their project briefly:
   - What the project does (one sentence)
   - Tech stack and language(s)
   - Key conventions or constraints worth noting

   Fill in the `context` field with 3–5 sentences based on their answers.

4. **Capture existing behavior** — ask: "Does this project already have behavior worth documenting?" If yes, help the user write initial spec files under `.spec-driven/specs/<category>/` using the standard format:
   - Group by domain area (e.g. `auth/`, `api/`, `core/`)
   - Use `### Requirement: <name>` headings with RFC 2119 keywords
   - Describe what the system currently does, not what it should do
   - Add an entry for each new file to `.spec-driven/specs/INDEX.md`

   This step is important for existing projects — without initial specs, `propose` has nothing to read and cannot detect conflicts.

5. **Confirm** — show the user what was created and suggest running `/spec-driven-propose` to create their first change.

## Rules
- Do not create any changes — initialization only
- Keep the context field concise: 3–5 sentences is enough for the AI to work from
- If .spec-driven/ already exists, do not reinitialize — suggest `/spec-driven-propose` instead
