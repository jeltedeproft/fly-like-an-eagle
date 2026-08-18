import test from'node:test';import assert from'node:assert/strict';import{WORLD_ZONES,worldZone}from'../src/world.js';
test('altitude advances through every world zone',()=>{for(const zone of WORLD_ZONES)assert.equal(worldZone(zone.min).id,zone.id);assert.equal(worldZone(-10).id,'country');assert.equal(worldZone(1e9).id,'space')});
