# Implementation Plan: Core Launch and Upgrade Loop

**Branch**: `001-core-launch-loop` | **Date**: 2026-08-17 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `specs/001-core-launch-loop/spec.md`

## Summary

Deliver the game as small playable browser slices, beginning with a deterministic fixed-step ramp
launch, airborne pitch control, landing/stop detection, distance scoring, results, and instant retry.
Extend that slice with a data-driven five-path upgrade economy and versioned local persistence. Use a
dependency-free Canvas and ES-module architecture for instant clean-slate iteration. Use a
repository-owned palette and cohesive code-native illustrated set until a legally reusable,
style-matched art source passes the constitution's style test.

## Technical Context

**Language/Version**: JavaScript ES2022 on Node.js 22 for tests and modern browsers at runtime

**Primary Dependencies**: Browser Canvas 2D, Web Storage, Node.js built-in test runner; no runtime packages

**Storage**: Versioned local-storage payload with defensive validation and reset behavior

**Testing**: Node.js built-in test runner for simulation, economy, and persistence; browser smoke test

**Target Platform**: Modern desktop browsers at 16:9 and resizable layouts

**Project Type**: Static browser game with testable ES modules

**Performance Goals**: Stable 60 simulation updates per second; responsive input; results-to-retry under 2 seconds

**Constraints**: Offline-capable; deterministic rewards; no unclear-license release assets; no runtime dependencies;
one palette and art treatment; fixed-step physics; no network dependency in the first release

**Scale/Scope**: One environment, one starter vehicle, five upgrade paths, results/shop flow, local save,
keyboard/pointer controls, and approximately 8-12 minutes to experience the full early loop

## Constitution Check

*GATE: Passed before research and passed again after design.*

- **Cohesive Art Direction**: PASS. Art tokens, swatch, style test, and review checklist are foundational
  tasks. Procedural placeholders obey the target palette. No pack is adopted piecemeal.
- **Feel Before Breadth**: PASS. Physics tuning and playtest instrumentation precede economy and content.
- **Incremental Run Loop**: PASS. User stories map directly to separately playable slices; the first is a
  complete launch/result/retry experience.
- **Modern Power, Flash-Era Clarity**: PASS. Fixed-step physics, particles, parallax, camera feedback, and
  reduced motion are planned behind readability gates.
- **Licensed and Traceable Assets**: PASS. The current `assets/pack` history is identified as commercial
  Hill Climb Racing material and is prohibited from shipping. Candidate assets require ledger approval.

## Project Structure

### Documentation (this feature)

```text
specs/001-core-launch-loop/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   ├── game-flow.md
│   └── art-acceptance.md
└── tasks.md
```

### Source Code (repository root)

```text
assets/
├── art/style-guide.md
├── art/palette.hex
├── art/style-test/
└── ASSET_LEDGER.md

src/
├── config.js
├── economy.js
├── simulation.js
├── storage.js
├── renderer.js
├── main.js
└── style.css

test/
├── economy.test.js
├── simulation.test.js
└── storage.test.js

scripts/serve.mjs
index.html
```

**Structure Decision**: The project owner confirmed the historical deletion was intentional. Build a
small browser-first replacement without restoring any old source or assets. Keep simulation, economy,
and storage independent of rendering so they run in the built-in Node.js test runner.

## Complexity Tracking

No constitution violations or unjustified complexity are planned.
