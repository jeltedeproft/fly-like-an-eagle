# Player-Facing Game Flow Contract

## Controls

- Start/retry: one clearly labeled action; keyboard Space/R and primary pointer action are supported.
- Air control: left/right or A/D rotates in opposite, consistent directions.
- Pause: freezes simulation and limited resources.
- Workshop: every purchase action exposes name, current/next value, cost, and unavailable reason.

## Results

Exactly one results presentation follows a finalized run. It displays final distance, personal best,
distance reward, each bonus, and total reward. Continuing leads to workshop or immediate retry in no more
than two actions. Repeated input cannot award the same result twice.

## Feedback priorities

Vehicle silhouette, trajectory, ground, orientation, distance, and remaining boost are critical. Effects
and camera movement yield to those signals. Reduced motion preserves timing and information while reducing
shake, zoom pulses, and high-amplitude parallax.
