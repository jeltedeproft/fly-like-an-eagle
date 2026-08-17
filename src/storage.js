import{defaultProgress,UPGRADES}from'./config.js';
const KEY='fly-like-an-eagle-v1';
export function sanitize(raw){const base=defaultProgress();if(!raw||typeof raw!=='object')return base;const levels={};for(const u of UPGRADES)levels[u.id]=Math.max(0,Math.min(u.max,Math.floor(Number(raw.levels?.[u.id])||0)));const volume=(value,fallback)=>Number.isFinite(Number(value))?Math.max(0,Math.min(1,Number(value))):fallback;return{...base,bolts:Math.max(0,Math.floor(Number(raw.bolts)||0)),best:Math.max(0,Number(raw.best)||0),runs:Math.max(0,Math.floor(Number(raw.runs)||0)),reducedMotion:Boolean(raw.reducedMotion),musicVolume:volume(raw.musicVolume,base.musicVolume),sfxVolume:volume(raw.sfxVolume,base.sfxVolume),levels};}
export function loadProgress(store=globalThis.localStorage){try{return sanitize(JSON.parse(store.getItem(KEY)))}catch{return defaultProgress()}}
export function saveProgress(progress,store=globalThis.localStorage){store.setItem(KEY,JSON.stringify(sanitize(progress)));}
