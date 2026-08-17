import test from'node:test';import assert from'node:assert/strict';import{worldZone}from'../src/world.js';
test('altitude advances through every world zone',()=>{assert.equal(worldZone(0).id,'country');assert.equal(worldZone(100).id,'clouds');assert.equal(worldZone(900).id,'stratosphere');assert.equal(worldZone(9000).id,'orbit');assert.equal(worldZone(90000).id,'space')});
