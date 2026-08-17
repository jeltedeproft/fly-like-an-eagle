# Feature Specification: Absurd Escalation

**Created**: 2026-08-18
**Status**: In Progress

## User Scenarios & Testing

### User Story 1 - Feel Exponential Power (Priority: P1)
Players progress from metre-scale scrap runs to kilometre, orbital, and interplanetary travel, with every generation producing a dramatic increase in speed, distance, and rewards.

### User Story 2 - Read Ridiculous Numbers (Priority: P1)
Distances, speeds, prices, currency, and rewards remain readable using compact units at every scale.

### User Story 3 - Build Launch Infrastructure (Priority: P2)
Players replace the ramp with increasingly absurd launch facilities that visibly change the launch site and multiply performance.

### User Story 4 - Leave the Earth (Priority: P2)
Backgrounds and effects transition through countryside, clouds, upper atmosphere, orbit, and deep space as altitude and speed increase.

### User Story 5 - Install Generation Power Systems (Priority: P2)
Each vehicle generation has distinctive high-impact power upgrades culminating in fictional late-game propulsion.

### User Story 6 - Prestige the Scrapyard (Priority: P3)
After reaching the endgame, players may reset ordinary progression for a permanent Engineering Genius multiplier and faster future runs.

## Requirements

- **FR-001**: Vehicle generations MUST increase equivalent maximum range by at least 5x after the starter tier.
- **FR-002**: Rewards MUST scale with distance and generation without unsafe or non-finite values.
- **FR-003**: All large quantities MUST use compact readable units while retaining useful precision.
- **FR-004**: Launch facilities MUST form an ordered visual and mechanical advancement ladder.
- **FR-005**: World visuals MUST communicate increasing altitude through at least five distinct palette-consistent zones.
- **FR-006**: Each vehicle generation MUST offer a distinctive power-system progression.
- **FR-007**: Prestige MUST be voluntary, clearly preview its reward, reset only declared progression, and grant a persistent permanent multiplier.
- **FR-008**: Existing saves MUST migrate without losing earned currency, unlocks, purchases, records, or settings.

## Success Criteria

- **SC-001**: Endgame runs exceed starter runs by at least 1,000x distance.
- **SC-002**: Every displayed quantity remains legible up to at least 1e30.
- **SC-003**: Every facility and world zone is visually distinguishable at gameplay size.
- **SC-004**: Prestige rewards can never be claimed twice for one reset.
- **SC-005**: All progression math remains finite and deterministic in automated tests.

## Assumptions

- Fictional power and distance escalation takes priority over realistic physics.
- The existing master palette and outline language remain mandatory.
- Each of the six user stories ships as a separate commit.
