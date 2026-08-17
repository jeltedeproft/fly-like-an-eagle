<!--
Sync Impact Report
- Version change: template -> 1.0.0
- Added principles: Cohesive Art Direction; Feel Before Breadth; Incremental Run Loop;
  Modern Power, Flash-Era Clarity; Licensed and Traceable Assets
- Added sections: Art Bible and Palette; Development Workflow and Quality Gates
- Removed sections: none
- Follow-up TODOs: none
-->
# Fly Like an Eagle Constitution

## Core Principles

### I. Cohesive Art Direction (NON-NEGOTIABLE)
Every visible element MUST look authored for the same game. Vehicles, characters, terrain,
backgrounds, effects, animation, typography, icons, and interface elements MUST follow one approved
art bible, pixel density, outline language, lighting direction, rendering treatment, and palette.
An asset pack is a source of raw material, never permission to mix styles. Any imported asset MUST be
redrawn, recolored, or rejected when it does not pass the art-direction review. Temporary programmer
art MUST be visibly labeled and MUST NOT ship. Cohesion is a product requirement, not polish.

### II. Feel Before Breadth
The ramp descent, launch, flight, landing, crash, reward reveal, upgrade purchase, and retry MUST feel
good before additional vehicles, stages, systems, or content are added. Each control MUST produce
immediate audiovisual feedback. Tuning values MUST be data-driven so launch cadence, air control,
drag, lift, impacts, and rewards can be iterated without structural code changes. A feature that adds
scope while weakening the core launch loop MUST be deferred.

### III. Incremental Run Loop
Development MUST deliver independently playable increments in this order: launch and distance;
results and rewards; meaningful upgrades and persistence; juice and variety; long-term depth. Every
increment MUST preserve a complete loop of prepare, launch, travel, stop, score, improve, and retry.
Runs MUST be short enough to invite immediate repetition, and each early upgrade MUST create a
perceptible change on the next run. Progression MUST reward skill and distance without requiring
grind to compensate for an unfun base vehicle.

### IV. Modern Power, Flash-Era Clarity
The game MUST preserve the readable silhouettes, direct controls, quick restarts, playful exaggeration,
and low-friction menus associated with classic browser launch games. Modern hardware SHOULD deepen
the experience through smooth physics, layered parallax, particles, reactive audio, camera work,
dynamic weather, destruction, ghosts, and accessibility options. Effects MUST never obscure vehicle
state, trajectory, landing angle, remaining boost, or upgrade decisions. Loading and restart delays
MUST be kept negligible during normal play.

### V. Licensed and Traceable Assets
Every external asset MUST have a recorded creator, source, license, permitted uses, and modification
status before entering a release build. Assets extracted from commercial games or lacking clear reuse
rights MUST be treated as reference-only and MUST NOT be committed or shipped. Paid assets require
proof of purchase and a license compatible with the intended platforms. The project MUST maintain an
asset ledger, including generated assets, and preserve required attribution.

## Art Bible and Palette

The target is a chunky, hand-crafted 2D illustrated style with playful mechanical exaggeration,
slightly grimy materials, bold silhouettes, and limited animation that gains life from squash,
rotation, particles, and secondary motion. It may evoke the energy of old-school Flash games but
MUST NOT copy protected characters, vehicles, interface layouts, logos, or artwork from Potty Racers,
Learn to Fly, or any other title.

One named master palette MUST be stored in the repository as both human-readable color tokens and an
artist-usable swatch. New colors require an art-bible amendment. Tints and shades MUST be derived from
the master palette, and all assets MUST share the same contrast hierarchy. UI, world art, and effects
MUST use the same palette, outline rules, and texture language. Before adopting a pack, at least one
vehicle, one terrain tile, one background element, one effect, and one UI panel MUST be assembled into
a style test; adoption requires the composition to read as one authored set at gameplay scale.

## Development Workflow and Quality Gates

Each feature begins with a user-visible scenario and measurable acceptance criteria. Physics,
economy, vehicle statistics, upgrade curves, palette tokens, and effect intensity MUST be configurable.
Automated tests MUST cover deterministic scoring, purchase rules, progression math, save migration,
and state transitions. A repeatable manual playtest MUST cover launch, airborne control, landing,
results, purchase, and retry at the target frame rate.

Every increment MUST pass: constitution compliance; asset-license review; gameplay-scale art review;
keyboard and pointer usability; readable feedback at common viewport sizes; and a clean run from a new
save. Performance work MUST be based on measurements, while visual additions MUST define reduced-motion
or reduced-intensity behavior when they materially affect readability or comfort.

## Governance

This constitution supersedes conflicting project practices and feature documents. Any amendment MUST
be documented in the Sync Impact Report, approved by the project owner, and accompanied by migration
notes when it changes existing content or workflows. Versions follow semantic versioning: MAJOR for
incompatible principle changes or removals, MINOR for new principles or materially expanded rules,
and PATCH for clarifications that do not change obligations.

Every specification, plan, task list, pull request, and release review MUST explicitly check the five
core principles. Exceptions require a written rationale, a named owner, and an expiry or remediation
task; the cohesive-art and asset-license principles cannot be waived for a release build. Runtime and
contributor guidance MUST remain consistent with this constitution.

**Version**: 1.0.0 | **Ratified**: 2026-08-17 | **Last Amended**: 2026-08-17
