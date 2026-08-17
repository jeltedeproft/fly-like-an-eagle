# Data Model: Core Launch and Upgrade Loop

## RunState

States: `READY -> DESCENDING -> AIRBORNE -> GROUNDED -> COMPLETE -> RESULTS -> WORKSHOP -> READY`.
`DESCENDING` may return to `READY` only through an explicit restart. Out-of-bounds or stuck detection
transitions to `COMPLETE`. A run can be finalized and awarded once.

## RunResult

- `runId`: unique per attempt
- `distanceMeters`: finite, non-negative final forward distance
- `bestAirDistanceMeters`: finite, non-negative maximum distance while airborne
- `airtimeSeconds`: finite, non-negative
- `landingQuality`: clean, rough, crash, or no-landing
- `impactSeverity`: finite, non-negative
- `terminationReason`: stopped, crashed, stuck, bounds, or restart
- `bonuses`: named non-negative reward components
- `totalReward`: deterministic sum of distance reward and bonuses
- `awarded`: whether the result has already changed player currency

## PlayerProgress

- `schemaVersion`: positive supported save version
- `currency`: non-negative integer
- `bestDistanceMeters`: finite, non-negative
- `upgradeLevels`: one bounded level for each known upgrade ID
- `unlockedVehicleIds`: known IDs only; includes starter
- `selectedVehicleId`: one unlocked known ID
- `claimedMilestoneIds`: known IDs only
- `settings`: audio levels, reduced motion, input hints

On invalid data, recover valid fields where safe and replace invalid fields with defaults. Save only after
validated state transitions.

## VehicleDefinition

- `id`, `displayName`, `description`
- Base mass, launch impulse, drag/lift response, pitch authority, impact tolerance, boost capacity
- Visual profile and collision dimensions
- Unlock milestone and allowed upgrade IDs

Definitions are read-only game data. The starter must remain controllable at level zero.

## UpgradeDefinition

- `id`: launch-power, aerodynamics, air-control, durability, or boost-capacity
- `displayName`, `description`
- Ordered levels with exact price and advertised effect
- Maximum level and affected vehicle statistic

A purchase transitions only when the path is not maxed and currency covers the exact price. Currency and
level change atomically.

## Milestone

- `id`, threshold type and value
- One-time reward or vehicle unlock
- Celebration content reference

Claiming is idempotent.

## ArtStyleProfile

- Master palette token names and color values
- Outline widths by gameplay scale
- Lighting direction and shadow steps
- Texture/grain constraints
- Shape language and silhouette examples
- Typography rules and approved font licenses
- Reference style-test version

## AssetLedgerEntry

- Repository path, logical asset ID, category
- Creator, source URL or purchase record, license text/path
- Required attribution and modification notes
- Palette/style review status and reviewer/date
- Shipping status: approved, prototype-only, reference-only, rejected

Only `approved` entries may be included in release packaging.
