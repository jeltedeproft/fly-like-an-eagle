import test from'node:test';import assert from'node:assert/strict';import{formatNumber,formatDistance,formatSpeed,formatBolts}from'../src/format.js';
test('large numbers stay compact and finite',()=>{assert.equal(formatNumber(999),'999');assert.equal(formatNumber(1250),'1.25K');assert.equal(formatNumber(2.5e9),'2.5B');assert.match(formatNumber(1e40),/e\+40/);assert.equal(formatNumber(Infinity),'0')});
test('game quantities use useful scale units',()=>{assert.equal(formatDistance(353369),'353 km');assert.equal(formatSpeed(2450),'Mach 2');assert.equal(formatBolts(1250000),'1.25M BOLTS')});
