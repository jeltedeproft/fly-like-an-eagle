# Tasks: Core Launch and Upgrade Loop

## Phase 1: Clean-slate setup

- [X] T001 Record the intentional deletion decision in `specs/001-core-launch-loop/quickstart.md`
- [X] T002 Scaffold the dependency-free browser project in `index.html`, `package.json`, and `scripts/serve.mjs`
- [X] T003 Exclude generated/build/editor files in `.gitignore`
- [X] T004 Record rejected historical assets and audit rules in `assets/ASSET_LEDGER.md`
- [X] T005 [P] Define the master palette in `assets/art/palette.hex`
- [X] T006 [P] Define cohesive art rules in `assets/art/style-guide.md`

## Phase 2: Foundations

- [X] T007 [P] Define balance and five upgrade paths in `src/config.js`
- [X] T008 [P] Implement deterministic scoring and atomic purchases in `src/economy.js`
- [X] T009 [P] Implement validated versioned persistence in `src/storage.js`
- [X] T010 Implement fixed-step run states, ramp, flight, landing, and termination in `src/simulation.js`
- [X] T011 [P] Add economy tests in `test/economy.test.js`
- [X] T012 [P] Add persistence tests in `test/storage.test.js`
- [X] T013 Add simulation tests in `test/simulation.test.js`

## Phase 3: User Story 1 - Launch, Fly, and Set a Distance (P1) MVP

- [X] T014 [US1] Implement responsive launch/flight/ground rendering in `src/renderer.js`
- [X] T015 [US1] Implement keyboard and pointer pitch controls in `src/main.js`
- [X] T016 [US1] Implement distance, speed, state, results, and fast retry in `index.html` and `src/main.js`
- [ ] T017 [US1] Perform and record a five-run feel-tuning session in `specs/001-core-launch-loop/quickstart.md`

## Phase 4: User Story 2 - Earn, Upgrade, and Feel Improvement (P2)

- [X] T018 [US2] Implement one-time result awarding and persistent records in `src/main.js`
- [X] T019 [US2] Implement five-level workshop purchases in `src/main.js`
- [X] T020 [US2] Make purchased levels alter launch, glide, control, durability, and boost in `src/simulation.js`
- [ ] T021 [US2] Validate first-upgrade pacing across three fresh saves in `specs/001-core-launch-loop/quickstart.md`

## Phase 5: User Story 3 - Read and Enjoy the Run (P3)

- [X] T022 [US3] Draw all world, vehicle, effects, and UI in the approved palette in `src/renderer.js` and `src/style.css`
- [X] T023 [US3] Implement persistent reduced-motion behavior in `src/main.js`
- [X] T024 [US3] Add original reactive sound and independent volume controls in `src/audio.js`
- [ ] T025 [US3] Record gameplay-scale art/readability review in `assets/art/style-test/REVIEW.md`

## Phase 6: User Story 4 - New Contraptions and Goals (P4)

- [ ] T026 [US4] Implement idempotent milestones and tests in `src/milestones.js` and `test/milestones.test.js`
- [ ] T027 [US4] Add an original second vehicle with a clear strength and weakness in `src/config.js` and `src/renderer.js`
- [ ] T028 [US4] Add unlock celebration and vehicle selection to `index.html` and `src/main.js`

## Phase 7: Validation

- [X] T029 Run all automated tests with `npm test`
- [ ] T030 Run the complete browser quickstart and record results in `specs/001-core-launch-loop/quickstart.md`

## Dependencies

US1 is the playable MVP. US2 depends on completed run results. US3 may proceed after US1. US4 is a
post-MVP extension and follows stable progression. T017 and T021 are tuning gates; T025 and T030 are
human-visible browser review gates.
