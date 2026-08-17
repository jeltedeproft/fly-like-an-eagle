# Feature Specification: Vehicle Technology Ladder

**Feature Branch**: `[003-vehicle-tech-ladder]`

**Created**: 2026-08-17

**Status**: Implemented

**Input**: User description: "Replace balanced vehicle choices with a clear advancement path from a terrible starter car to increasingly powerful and ridiculous vehicles, ending with a spaceship; every vehicle has separate upgrades."

## User Scenarios & Testing

### User Story 1 - Advance Through Vehicle Generations (Priority: P1)

The player begins with an obviously poor scrap vehicle and unlocks a fixed sequence of increasingly capable, visually advanced contraptions until reaching an absurd spacecraft.

**Why this priority**: The vehicle ladder becomes the main long-term progression fantasy.

**Independent Test**: Begin with a fresh save, inspect the locked ladder, meet each unlock goal in order, and verify every new generation has higher overall potential and a visibly more advanced design.

**Acceptance Scenarios**:

1. **Given** a fresh save, **When** the workshop opens, **Then** only the starter vehicle is available and later generations are shown in order with unlock goals.
2. **Given** an unlock goal is reached, **When** results are awarded, **Then** the next vehicle unlocks once with a celebration.
3. **Given** the final vehicle is unlocked, **When** it is selected, **Then** it is recognizably spacecraft-like and substantially outperforms the starter.

### User Story 2 - Upgrade Each Vehicle Separately (Priority: P1)

The player develops each vehicle independently, making the workshop history feel like a sequence of completed engineering projects rather than one shared statistics sheet.

**Why this priority**: Independent investment gives each vehicle its own progression arc and prevents upgrades from skipping a new vehicle's early experience.

**Independent Test**: Upgrade one vehicle, switch to another, and verify the second vehicle retains its own levels and installed parts; switch back and verify the original state persists.

**Acceptance Scenarios**:

1. **Given** upgrades on the starter, **When** a newly unlocked vehicle is selected, **Then** its standard upgrades begin at level zero.
2. **Given** different upgrades and parts on two vehicles, **When** switching repeatedly, **Then** each vehicle restores its own configuration.
3. **Given** an older save, **When** it is migrated, **Then** existing upgrades and parts belong to the starter without being lost.

### User Story 3 - Read Progress at a Glance (Priority: P2)

The player can quickly see the ordered vehicle ladder, current selection, unlock requirement, and relative power without reading dense descriptions.

**Why this priority**: The workshop must preserve the game's quick Flash-era cadence.

**Independent Test**: Open the workshop and identify the current vehicle, next unlock goal, and direction of advancement within ten seconds.

**Acceptance Scenarios**:

1. **Given** the workshop, **When** viewing vehicles, **Then** their generation order and locked, unlocked, or selected state are visually obvious.
2. **Given** a vehicle is selected, **When** viewing upgrades and parts, **Then** only that vehicle's progression is displayed and changed.

### User Story 4 - Improve the Launch Site (Priority: P2)

The player can invest in global facilities outside the selected vehicle, beginning with a visibly larger launch ramp that benefits every generation.

**Independent Test**: Purchase each ramp level and verify the takeoff structure becomes taller and steeper while the same vehicle achieves greater range.

**Acceptance Scenarios**:

1. **Given** sufficient bolts, **When** a ramp expansion is purchased, **Then** its global level persists and the physical ramp visibly grows.
2. **Given** two otherwise identical runs, **When** one uses an expanded ramp, **Then** it launches higher and farther than the stock ramp.

### Edge Cases

- Unlocks must occur in order and must never be awarded repeatedly.
- Unknown or locked vehicle selections in saved data fall back safely to the starter.
- Switching vehicles cannot copy, erase, or combine their upgrades or parts.
- Every vehicle must remain readable with every compatible installed part.

## Requirements

### Functional Requirements

- **FR-001**: The game MUST provide at least four ordered vehicle generations from scrap starter through spacecraft.
- **FR-002**: Each successive vehicle MUST have greater overall distance potential than the prior generation.
- **FR-003**: Later vehicles MUST be advancements, not balanced side-grades, while retaining distinct handling character.
- **FR-004**: Vehicles MUST unlock sequentially through persistent one-time milestones.
- **FR-005**: Each vehicle MUST maintain separate standard-upgrade levels.
- **FR-006**: Each vehicle MUST maintain separate installed-part ownership.
- **FR-007**: The workshop MUST show only the selected vehicle's upgrades and parts.
- **FR-008**: Existing upgrade and part progress MUST migrate to the starter vehicle.
- **FR-009**: Every generation MUST have an unmistakably distinct silhouette using the master palette and established art style.
- **FR-010**: The final generation MUST clearly read as an absurd improvised spacecraft.
- **FR-011**: The vehicle ladder MUST communicate generation order, selection state, and next unlock requirement with minimal text.
- **FR-012**: The workshop MUST offer persistent global facility upgrades separately from vehicle-specific progression.
- **FR-013**: The initial facility upgrade MUST provide at least four visible ramp-expansion levels that improve launch height and range for every vehicle.

### Key Entities

- **Vehicle Generation**: One ordered contraption with base performance, visual identity, unlock goal, and progression state.
- **Vehicle Progress**: Upgrade levels and installed parts owned specifically by one vehicle.
- **Unlock Milestone**: A one-time distance goal that opens the next generation and grants a celebration.
- **Facility Upgrade**: A persistent improvement to the launch site shared by all vehicles.

## Success Criteria

### Measurable Outcomes

- **SC-001**: A fresh player can identify the starter, final spacecraft, and next unlock goal within ten seconds.
- **SC-002**: Each generation's tested maximum distance exceeds its predecessor by at least 20% under equivalent upgrade conditions.
- **SC-003**: Switching among vehicles preserves 100% of their independent upgrade and part states across 20 switches and a reload.
- **SC-004**: 100% of old valid upgrades and parts remain available on the starter after migration.
- **SC-005**: All four silhouettes are distinguishable at the smallest supported gameplay viewport.
- **SC-006**: A maximum-level ramp increases the same vehicle's tested range by at least 20% over the stock ramp.

## Assumptions

- The first ladder contains four generations: Rustbucket Cart, Bathtub Bullet, Rocket Coffin, and Scrapstar spacecraft.
- Unlock goals use best distance and occur in a fixed sequence.
- Previously purchased progression is assigned to the starter during migration.
- The same upgrade categories and part catalogue are available independently on every vehicle for this increment.
