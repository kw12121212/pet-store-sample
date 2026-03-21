#!/usr/bin/env node
import fs from "fs";
import path from "path";
const targetArg = process.argv[2];
const targetDir = targetArg ? path.resolve(targetArg) : process.cwd();
const specDir = path.join(targetDir, ".spec-driven");
if (fs.existsSync(specDir)) {
    console.error(`Error: .spec-driven/ already exists at ${specDir}`);
    process.exit(1);
}
fs.mkdirSync(path.join(specDir, "changes"), { recursive: true });
fs.mkdirSync(path.join(specDir, "specs", "core"), { recursive: true });
fs.writeFileSync(path.join(specDir, "config.yaml"), `schema: spec-driven
context: |
  [Project context — populated by user, injected into skill prompts]
rules:
  specs:
    - Requirements specify observable behavior, not implementation details
  tasks:
    - Tasks should be independently completable
`);
fs.writeFileSync(path.join(specDir, "specs", "README.md"), `# Specs

Specs describe the current state of the system — what it does, not how it was built.

## Organization

Group specs by domain area. Use kebab-case directory names (e.g. \`core/\`, \`api/\`, \`auth/\`).

## Conventions

- Write in present tense ("the system does X")
- Describe observable behavior, not implementation details
- Keep each spec focused on one area
`);
console.log(`Initialized: ${specDir}`);
console.log(`  ${path.join(specDir, "config.yaml")}`);
console.log(`  ${path.join(specDir, "specs", "README.md")}`);
console.log(`  Edit config.yaml to add project context`);
