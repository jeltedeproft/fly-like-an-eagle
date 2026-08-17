# Feature Specification: Visible Car Progression

**Feature Branch**: `[002-visible-car-progression]`

**Created**: 2026-08-17

**Status**: Ready

**Input**: User description: "Add more progression through standard performance upgrades and new car parts; purchased parts must appear clearly on the car."

## User Scenarios & Testing

### User Story 1 - Buy Performance Upgrades (Priority: P1)

After a run, the player spends earned bolts on familiar statistical improvements such as speed and air control and feels their effect on later runs.

**Why this priority**: Meaningful repeat-run improvement is the foundation of the progression loop.

**Independent Test**: Earn or grant bolts, purchase a performance upgrade, begin another run, and verify the relevant capability improves while the purchase remains owned after reload.

**Acceptance Scenarios**:

1. **Given** sufficient bolts, **When** the player buys a performance level, **Then** its cost is deducted, its level increases, and its gameplay effect applies to the next run.
2. **Given** insufficient bolts or a maximum-level upgrade, **When** the player attempts purchase, **Then** no currency or progression changes.

---

### User Story 2 - Install Visible Car Parts (Priority: P1)

The player buys distinct vehicle parts in the workshop and immediately sees each installed part on the workshop car and during the next run.

**Why this priority**: Visible transformation makes progression tangible and gives the improvised vehicle personality.

**Independent Test**: Purchase each part individually and verify that its unique, clearly readable silhouette appears on the car at gameplay scale before and during a run.

**Acceptance Scenarios**:

1. **Given** sufficient bolts, **When** a part is purchased, **Then** it becomes permanently owned and appears on the car immediately.
2. **Given** several owned parts, **When** the vehicle is displayed or driven, **Then** all compatible parts remain individually recognizable and use the approved palette and drawing style.
3. **Given** a reloaded saved game, **When** the car is shown, **Then** all previously purchased parts remain installed and visible.

---

### User Story 3 - Understand Upgrade Choices (Priority: P2)

The player can quickly distinguish performance upgrades from visible car parts and understand the cost, ownership state, and benefit of every choice.

**Why this priority**: Clear choices preserve the quick, low-friction workshop cadence.

**Independent Test**: Open the workshop and identify both categories, each item's cost or owned state, and its benefit without beginning a run.

**Acceptance Scenarios**:

1. **Given** the workshop is open, **When** the player scans it, **Then** performance upgrades and car parts are presented as separate named groups.
2. **Given** an owned one-time part, **When** it is viewed, **Then** it is clearly marked installed and cannot be purchased twice.

### Edge Cases

- Older saves without part ownership load with no parts owned and retain all currency and standard upgrade progress.
- Repeated purchase input cannot deduct currency twice for a one-time part.
- Multiple installed parts must not obscure the wheels, vehicle direction, or each other's defining silhouettes.
- Parts remain readable during rotation, airborne control, boost effects, and impact feedback.

## Requirements

### Functional Requirements

- **FR-001**: The workshop MUST separate repeatable performance upgrades from one-time visible car parts.
- **FR-002**: Performance upgrades MUST continue to provide perceptible, described statistical benefits.
- **FR-003**: The initial visible-parts set MUST contain at least three separately purchasable parts.
- **FR-004**: Every purchased part MUST immediately create a clearly visible change to the vehicle silhouette or major color mass.
- **FR-005**: Purchased parts MUST appear in both workshop presentation and active runs.
- **FR-006**: Part ownership MUST persist between sessions and migrate safely from older saves.
- **FR-007**: A one-time part MUST not be purchasable more than once.
- **FR-008**: Purchase controls MUST communicate item name, effect, cost, affordability, and owned or maximum state.
- **FR-009**: All new parts MUST use the master palette, heavy outline language, and playful improvised-machine art direction.
- **FR-010**: Installed parts MUST not compromise vehicle-state, wheel, direction, or trajectory readability.

### Key Entities

- **Performance Upgrade**: A multi-level purchase with a name, cost curve, current level, maximum level, description, and gameplay effect.
- **Car Part**: A one-time purchase with a name, fixed cost, described benefit, ownership state, and distinct installed appearance.
- **Player Progress**: The saved bolts, records, upgrade levels, and owned car parts.

## Success Criteria

### Measurable Outcomes

- **SC-001**: A player can identify the two purchase categories and make a valid purchase within 15 seconds of entering the workshop.
- **SC-002**: 100% of purchased parts are visible immediately and after reload in both workshop and gameplay views.
- **SC-003**: At least three visible parts have distinguishable silhouettes at the smallest supported gameplay viewport.
- **SC-004**: Existing player saves retain 100% of previously stored currency, records, settings, and upgrade levels after migration.
- **SC-005**: All purchase boundaries—including insufficient funds, duplicate part purchase, and maximum level—complete without unintended currency loss.

## Assumptions

- New parts are installed automatically when purchased; manual equip and inventory management are outside this increment.
- Standard upgrades provide small incremental gains between runs, while each visible car part is a major progression milestone with a substantially larger gameplay benefit.
- The first part set is code-drawn in the established palette so it matches the current vehicle exactly and requires no external asset licensing.
- Existing controls, run cadence, and vehicle base body remain unchanged.
