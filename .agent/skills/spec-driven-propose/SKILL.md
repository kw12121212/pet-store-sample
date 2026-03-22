---
name: spec-driven-propose
description: Propose a new spec-driven change. Scaffolds proposal.md, design.md, and tasks.md for a named change, populated with project context.
version: 0.1.0
---

You are helping the user create a new spec-driven change proposal.

## Steps

1. **Get the change name** — ask the user for a short kebab-case name describing the change (e.g. `add-auth`, `refactor-db-layer`). If they already provided one, use it.

2. **Read project context and existing specs** — read the following before generating anything:
   - `.spec-driven/config.yaml` — use `context` to inform content; treat `rules` as binding constraints; note any `fileMatch` entries that apply to files this change will touch
   - `.spec-driven/specs/INDEX.md` — identifies all existing spec files and their scope
   - Every spec file referenced in INDEX.md that this change is likely to touch — read the full content to understand existing requirements before writing MODIFIED or ADDED entries

3. **Scaffold the change** — run:
   ```
   node .agent/skills/spec-driven-propose/scripts/spec-driven.js propose <name>
   ```
   This creates `.spec-driven/changes/<name>/` with empty seed files.

4. **Fill proposal.md** — write a clear, concise proposal covering:
   - **What**: What the change does (observable behavior, not implementation)
   - **Why**: Motivation and context
   - **Scope**: What is in scope, what is explicitly out of scope
   - **Unchanged Behavior**: List existing behaviors that must not break — things adjacent to or potentially affected by this change. Leave blank if truly nothing is at risk.

   If the user's description leaves anything unclear — motivation, scope boundaries, success criteria — mark it with `[NEEDS CLARIFICATION: <specific question>]` rather than assuming. Do not fill in details you are not certain about.

5. **Fill design.md** — write the technical approach:
   - **Approach**: How you'll implement it at a high level
   - **Key Decisions**: Significant choices and their rationale
   - **Alternatives Considered**: What was ruled out and why

   If a technical decision depends on unclear requirements or constraints, mark it with `[NEEDS CLARIFICATION: <specific question>]` rather than committing to an approach that may be wrong.

6. **Populate specs/ delta files** — look at the project's `.spec-driven/specs/` directory structure. For each spec file that this change touches, create a corresponding file under `.spec-driven/changes/<name>/specs/` mirroring the same relative path (e.g. `specs/auth/login.md` → `changes/<name>/specs/auth/login.md`).

   Each delta file uses ADDED/MODIFIED/REMOVED sections with the standard format:
   - `### Requirement: <name>` headings and RFC 2119 keywords (MUST/SHOULD/MAY)
   - `#### Scenario:` blocks (GIVEN/WHEN/THEN) where helpful
   - **ADDED**: new requirements; **MODIFIED**: changed requirements (include `Previously:` note); **REMOVED**: removed requirements (include reason)
   - Omit sections that don't apply — do not leave empty sections
   - If you cannot determine the correct classification due to unclear requirements, mark it with `[NEEDS CLARIFICATION: <question>]`
   - If this change has no observable spec impact, create a file with a brief explanation

7. **Fill tasks.md** — write a concrete implementation checklist:
   - Use `- [ ]` checkboxes for every task
   - Tasks should be independently completable
   - Group tasks logically (e.g. Implementation, Tests, Docs, Verification)
   - Do NOT add an "Update specs" task — the specs/ directory contains the spec artifacts

8. **Confirm** — show the user the four files and ask if they want to adjust anything. If any `[NEEDS CLARIFICATION]` markers exist, list each one explicitly and ask the user to resolve them before proceeding to `/spec-driven-apply`.

## Rules
- Do not implement anything — this is planning only
- Keep tasks atomic and verifiable
- proposal.md describes *what and why*; design.md describes *how*; tasks.md is the checklist
- Mark ambiguities with `[NEEDS CLARIFICATION: <specific question>]` — never guess at unclear requirements
