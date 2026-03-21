#!/usr/bin/env node
import fs from "fs";
import path from "path";
const name = process.argv[2];
if (!name) {
    console.error("Usage: node dist/scripts/cancel.js <change-name>");
    process.exit(1);
}
const changesDir = path.join(".spec-driven", "changes");
const changeDir = path.join(changesDir, name);
if (!fs.existsSync(changeDir)) {
    console.error(`Error: change '${name}' not found at ${changeDir}`);
    process.exit(1);
}
fs.rmSync(changeDir, { recursive: true, force: true });
console.log(`Cancelled: ${changeDir}`);
