# Development Plan

## Core loop

1. Sled accelerates down the hill.
2. Player launches from the ramp.
3. Player controls pitch while airborne.
4. Run ends after landing/crashing/stopping.
5. Distance and performance generate rewards.
6. Player upgrades and retries.

## Milestone 1 — Physics prototype

- [x] Desktop LibGDX project structure
- [x] Box2D world
- [x] Hill and ramp
- [x] Dynamic sled
- [x] Pitch controls
- [x] Camera follow
- [x] Distance/speed feedback
- [x] Restart
- [ ] Verify build locally
- [ ] Tune hill, friction, gravity and ramp
- [ ] Detect ground contact / airborne state
- [ ] Detect run end

## Milestone 2 — Game loop

- Results screen
- Currency
- Basic upgrades: mass, friction, launch speed, aerodynamics
- Persistent save
- Restart flow

## Milestone 3 — Feel

- Replace debug geometry with sprites
- Parallax background
- Particles
- Camera zoom based on speed/altitude
- Screen shake on impacts
- Sound and music

## Milestone 4 — Depth

- Air drag and simple lift model
- Wind
- Tricks / rotation scoring
- Multiple sled configurations
- More terrain and landmarks
- Ghost / best-run replay

## First local session

1. Clone/pull the repository.
2. Check out `agent/initial-prototype`.
3. Run the LWJGL3 module.
4. Fix any build/configuration issues before adding features.
5. Spend 15–30 minutes only tuning the hill and sled until launching feels fun.
6. Write down the values that feel good before implementing progression.

Do not build the upgrade system until the base launch is fun by itself.
