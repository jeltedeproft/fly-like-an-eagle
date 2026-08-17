# Quickstart Validation: Core Launch and Upgrade Loop

## Repository decision

The project owner confirmed the historical game deletion was intentional. The new browser implementation
does not restore or package any historical source or commercial-game-derived assets.

## Browser validation

1. With Node.js 20 or later installed, run `npm start` and open `http://127.0.0.1:4173`.
2. Start from a clean save and complete a no-input launch. Confirm one final distance and one reward.
3. Retry using pitch controls. Confirm opposite inputs rotate oppositely and change landing outcome.
4. Complete up to three runs, purchase the first affordable upgrade, and verify its advertised statistic
   changes on the next run.
5. Close and reopen. Confirm currency, upgrade, settings, and best distance persist.
6. Enable reduced motion and repeat a hard impact. Confirm critical signals remain visible.

## Intended automated validation

Run `npm test`. Tests must cover deterministic rewards, idempotent awarding, purchase
boundaries, save validation/migration, state transitions, and stuck/out-of-bounds completion.

## Art and license validation

Review `assets/ASSET_LEDGER.md`, `assets/art/style-guide.md`, the master swatch, and the gameplay-scale
style test. Every packaged asset must be approved under [the art contract](contracts/art-acceptance.md).
