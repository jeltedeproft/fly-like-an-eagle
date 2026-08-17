# Feature Specification: Core Launch and Upgrade Loop

**Feature Branch**: `main`

**Created**: 2026-08-17

**Status**: Draft

**Input**: User description: "Create an incremental ramp, slide, and flight game inspired by the
charm and rapid replay loop of classic Flash launch games. Start with a bad vehicle, travel as far as
possible, earn points based on performance, buy upgrades, and repeat in one consistent art style and
palette."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Launch, Fly, and Set a Distance (Priority: P1)

As a player, I start at the top of a large ramp in a visibly ramshackle vehicle, accelerate down the
slope, launch into the air, influence the vehicle's pitch, land or crash, and see how far I traveled.

**Why this priority**: The launch itself must be enjoyable before progression can add value.

**Independent Test**: Start a fresh run, launch without purchasing anything, control pitch in the air,
come to a complete stop, and receive an unambiguous distance result.

**Acceptance Scenarios**:

1. **Given** a new run, **When** the player starts, **Then** the vehicle descends the ramp and launches
   without menu interruption.
2. **Given** the vehicle is airborne, **When** the player uses pitch controls, **Then** its rotation
   changes predictably and affects the landing.
3. **Given** the vehicle has landed, crashed, or stopped progressing, **When** the run ends, **Then** a
   result shows distance, points earned, and best distance.
4. **Given** a completed run, **When** the player chooses retry, **Then** the next run begins within two
   seconds and retains no transient physics state from the prior run.

---

### User Story 2 - Earn, Upgrade, and Feel Improvement (Priority: P2)

As a player, I earn points from a run, spend them on understandable vehicle upgrades, and immediately
feel the purchased improvement on my next attempt.

**Why this priority**: Visible improvement creates the motivating repeat loop.

**Independent Test**: Complete runs until one entry-level upgrade is affordable, purchase it, and
compare the relevant next-run behavior with the same input pattern.

**Acceptance Scenarios**:

1. **Given** a completed run, **When** points are awarded, **Then** the total equals the displayed
   distance and performance breakdown and is added exactly once.
2. **Given** sufficient points, **When** the player purchases an upgrade, **Then** the cost is deducted,
   the level increases once, and the affected statistic visibly changes.
3. **Given** insufficient points or a maximum-level upgrade, **When** purchase is attempted, **Then** no
   points are deducted and the reason is clearly communicated.
4. **Given** a purchase and restart, **When** the game is reopened, **Then** currency, upgrades, and best
   distance are restored.

---

### User Story 3 - Read the Run and Enjoy the Impact (Priority: P3)

As a player, I can read speed, altitude, distance, vehicle state, and remaining boost at a glance while
the world reacts with coherent camera, sound, particles, and environmental motion.

**Why this priority**: Feedback makes the same physics more expressive without expanding the core loop.

**Independent Test**: Play one run with all feedback enabled and another with reduced motion; both runs
must remain readable and controllable through launch, flight, impact, and results.

**Acceptance Scenarios**:

1. **Given** a high-speed launch or hard impact, **When** audiovisual effects play, **Then** trajectory,
   orientation, and critical gauges remain legible.
2. **Given** reduced motion is enabled, **When** the same events occur, **Then** camera shake and intense
   movement are reduced without removing gameplay information.
3. **Given** any gameplay or menu screen, **When** it is compared to the approved style test, **Then** it
   uses the same palette, outline, lighting, texture, and typography rules.

---

### User Story 4 - Unlock New Contraptions and Goals (Priority: P4)

As a returning player, I unlock distinct vehicle generations and distance milestones that change
strategy while preserving learned controls and fast retries.

**Why this priority**: Variety extends the game only after the base loop, progression, and feedback work.

**Independent Test**: Reach one milestone, unlock the next vehicle generation, and complete a run whose
tradeoffs differ meaningfully from the starter vehicle.

**Acceptance Scenarios**:

1. **Given** a milestone threshold is reached, **When** results appear, **Then** the unlock is celebrated
   once and becomes selectable.
2. **Given** two unlocked vehicles, **When** the player compares them, **Then** each communicates at least
   one meaningful strength and weakness before launch.

### Edge Cases

- A run terminates safely if the vehicle becomes stuck, leaves the playable bounds, falls indefinitely,
  or moves away from the goal for an extended period.
- Distance and rewards never become negative, non-finite, or duplicable through repeated input.
- Resizing, loss of focus, or pausing does not advance the simulation or consume limited resources.
- Corrupt or incompatible save data falls back to the newest valid data or a clean save with a clear
  message; it never blocks play.
- Simultaneous landing and milestone events produce one result, one reward, and one unlock.
- All critical actions remain possible without relying on color alone.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The game MUST provide a complete prepare, ramp descent, launch, travel, stop, score,
  upgrade, and retry loop.
- **FR-002**: The starter vehicle MUST be deliberately weak but controllable and capable of earning an
  entry-level upgrade within the first three completed runs.
- **FR-003**: The player MUST be able to influence airborne pitch with clear, reversible input.
- **FR-004**: Run completion MUST account for landing, crashing, stopping, stuck states, and leaving safe
  play bounds.
- **FR-005**: Results MUST show distance, base distance points, performance bonuses, total points, best
  distance, and any milestones.
- **FR-006**: Reward calculation MUST be deterministic for the same recorded run outcome and MUST award
  each run exactly once.
- **FR-007**: The initial progression MUST include upgrade paths for launch power, aerodynamics, air
  control, landing durability, and optional boost capacity.
- **FR-008**: Each upgrade MUST show current level, next effect, price, maximum level, and affordability.
- **FR-009**: A purchased upgrade MUST cause a perceivable and measurable next-run difference in its
  advertised statistic.
- **FR-010**: Currency, upgrade levels, selected vehicle, unlocked milestones, settings, and personal
  best MUST persist between sessions.
- **FR-011**: The player MUST be able to restart a completed run in two actions or fewer.
- **FR-012**: The run display MUST communicate current distance, speed, altitude, orientation, airborne
  state, and remaining limited-use power when applicable.
- **FR-013**: Camera and effects MUST scale with meaningful events while preserving gameplay readability.
- **FR-014**: The game MUST offer independent volume controls and reduced-motion behavior.
- **FR-015**: Every shipped visual MUST comply with the approved art bible and master palette.
- **FR-016**: Every shipped external or generated asset MUST have a complete provenance and license entry.
- **FR-017**: Assets with unclear rights or extracted from another commercial game MUST NOT ship.
- **FR-018**: New vehicle generations MUST present meaningful tradeoffs rather than strictly cosmetic
  duplication.
- **FR-019**: The first playable release MUST include one ramp environment, one starter vehicle, five
  upgrade paths, persistent progress, and a results flow; additional environments and vehicles are
  outside the first increment.

### Key Entities *(include if feature involves data)*

- **Run**: One attempt, including start state, maximum and final distance, airtime, impacts, landing
  quality, bonuses, termination reason, and awarded status.
- **Vehicle Definition**: Base performance, visual identity, unlock rule, and available upgrade paths.
- **Upgrade Path**: Named statistic, levels, costs, per-level effects, and maximum level.
- **Player Progress**: Currency balance, best results, owned upgrade levels, unlocks, selection, and save
  version.
- **Milestone**: A distance or performance threshold with a one-time reward or unlock.
- **Art Style Profile**: Approved palette, outline and lighting rules, scale, texture language, and
  gameplay-scale reference composition.
- **Asset Ledger Entry**: Asset identity, author, source, license, proof of purchase when relevant,
  attribution, modifications, palette compliance, and release eligibility.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: At least 80% of first-time playtesters can launch, control pitch, finish a run, identify
  their score, buy an available upgrade, and retry without instruction beyond the control prompts.
- **SC-002**: A normal early run lasts 20-60 seconds, and retry from results to active descent takes no
  more than two seconds and two player actions.
- **SC-003**: A fresh player can afford the first upgrade within three completed runs, and at least 75%
  of testers correctly identify its effect on the next run.
- **SC-004**: Replaying an identical recorded outcome produces exactly the same reward total in 100% of
  validation cases.
- **SC-005**: In a blind art-cohesion review, at least 90% of reviewers judge representative vehicle,
  environment, effect, and interface samples as belonging to the same game.
- **SC-006**: Critical run information remains correctly readable by at least 90% of playtesters during
  the strongest launch and impact effects.
- **SC-007**: All release assets have complete provenance records and zero assets with unclear shipping
  rights.
- **SC-008**: Progress survives 20 consecutive save, close, reopen, and continue cycles without lost or
  duplicated currency, upgrades, or records.

## Assumptions

- The first release is a single-player, 2D side-view experience designed first for desktop web and
  desktop application play with keyboard and pointer controls.
- The experience is inspired by genre structure and feel, not by copying protected content from named
  games.
- The existing LibGDX prototype may be reused only after the current working-tree deletions are confirmed
  as accidental or intentionally reversed by the project owner.
- Local asset libraries are candidates only after license and visual-cohesion review; the downloaded
  commercial-game-looking directory is reference-only unless independently proven reusable.
- Online services, monetization, multiplayer, and mobile-specific touch layout are outside the first
  increment.
