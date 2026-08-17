import{defaultProgress}from'./config.js';
export const PRESTIGE_DISTANCE=100000;
export function prestigeReward(progress){return progress.best<PRESTIGE_DISTANCE?0:Math.max(1,Math.floor(Math.sqrt(progress.best/PRESTIGE_DISTANCE)))}
export const prestigeMultiplier=genius=>1+Math.max(0,genius||0)*.5;
export function applyPrestige(progress){const earned=prestigeReward(progress);if(!earned)return{ok:false,reason:'REACH 100 km',progress};const fresh=defaultProgress();return{ok:true,earned,progress:{...fresh,prestige:{genius:(progress.prestige?.genius||0)+earned},reducedMotion:progress.reducedMotion,musicVolume:progress.musicVolume,sfxVolume:progress.sfxVolume}}}
