# Research: Core Launch and Upgrade Loop

## Engine and architecture

**Decision**: Continue Java 17 with LibGDX 1.14.0 and Box2D after resolving the deleted working tree.

**Rationale**: The repository history already has desktop/web modules, a working fixed-step Box2D launch,
camera follow, airborne control, results, purchases, and local preferences. Refining and decomposing this
is less risky than changing engines before the core feel is validated.

**Alternatives considered**: Godot would improve editor-driven iteration but requires a rewrite; a
canvas-only web build would simplify distribution but discard the existing physics and desktop path.

## Simulation and scoring

**Decision**: Use a 1/60-second fixed simulation step, cap accumulated frame time, and derive rewards from
an immutable run result rather than live UI state.

**Rationale**: Fixed stepping makes feel repeatable across frame rates. An immutable result plus an awarded
flag prevents duplicate payouts and makes scoring testable.

**Alternatives considered**: Variable-step physics is simpler but inconsistent; awarding continuously
during travel complicates retries and interruption recovery.

## Early economy

**Decision**: Begin with five upgrade paths—launch power, aerodynamics, air control, durability, and boost—
using 4-6 visible levels and a hand-authored cost/effect curve. Tune the first purchase to arrive in runs
one through three.

**Rationale**: Five understandable axes cover launch, flight, agency, landing, and active power without the
seven overlapping paths in the historical prototype. Hand-authored curves are easier to balance than a
single formula during discovery.

**Alternatives considered**: Procedural infinite upgrades create shallow stat inflation; a large parts tree
adds content before the loop is proven.

## Art source strategy

**Decision**: Build the first style test from original, palette-constrained shapes and commissioned or
newly authored 2D art. Treat `C:/Users/Admin/Downloads/assets/assets` and the historical `assets/pack`
as reference-only because their contents identify Hill Climb Racing/Fingersoft. Audit candidates under
`E:/my files/game_assets_organized` by license and style before importing anything.

**Rationale**: The named commercial credits provide no reuse grant. A unified small original set is safer
and visually stronger than mixing even individually attractive packs.

**Alternatives considered**: Recoloring mixed packs does not resolve conflicting proportions, texture,
linework, animation, or licenses. Buying one comprehensive pack remains viable if it covers vehicle,
environment, UI, and effects or includes editable source suitable for a complete redraw pass.

## Visual direction

**Decision**: Use a chunky illustrated "scrap-yard aviation" look: dark ink-like outlines, warm rusty
vehicle materials, cool desaturated skies, cream UI surfaces, and restrained yellow-orange highlights.
Choose a compact 16-color master palette before production art.

**Rationale**: The direction supports a terrible starter contraption, readable silhouettes, playful
impacts, and Flash-era personality without copying the reference games.

**Alternatives considered**: Pure pixel art conflicts with the higher-resolution historical assets and
requires strict sprite-scale discipline; painterly art is harder to animate and keep readable at speed.

## Persistence

**Decision**: Store a schema-versioned player-progress record locally and migrate known prior keys once.
Validate non-negative currency, bounded levels, known vehicle IDs, and finite records on load.

**Rationale**: Local offline persistence matches scope while explicit validation prevents a corrupt save
from blocking play.

**Alternatives considered**: Cloud saves and accounts add network, privacy, and operational scope without
improving the first loop.
