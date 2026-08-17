# Fly Like an Eagle

A clean-slate, dependency-free browser launch game about sending a terrible scrap-built vehicle down a
huge ramp, flying as far as possible, earning bolts, and upgrading the contraption for the next run.

## Play locally

Requires Node.js 20 or later.

```powershell
npm start
```

Open <http://127.0.0.1:4173>.

## Controls

- `Space`: launch, retry, or use an installed soda-rocket boost while airborne
- `A` / `D` or `Left` / `Right`: lean in the air
- `R`: restart immediately
- Pointer/touch: press the left or right half of the game while airborne

## Tests

```powershell
npm test
```

Art is code-native and constrained to [`assets/art/palette.hex`](assets/art/palette.hex). Historical
commercial-game-derived assets are intentionally excluded; see [`assets/ASSET_LEDGER.md`](assets/ASSET_LEDGER.md).
