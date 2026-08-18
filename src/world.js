export const WORLD_ZONES=[
 {id:'country',name:'COUNTRYSIDE',min:0},
 {id:'birds',name:'HILLTOP AIRSPACE',min:35},
 {id:'clouds',name:'CLOUD LAYER',min:120},
 {id:'highsky',name:'HIGH SKY',min:600},
 {id:'stratosphere',name:'STRATOSPHERE',min:2500},
 {id:'nearspace',name:'NEAR SPACE',min:12000},
 {id:'orbit',name:'LOW ORBIT',min:50000},
 {id:'space',name:'DEEP SPACE',min:250000}
];
export function worldZone(altitude){const height=Math.max(0,Number(altitude)||0);for(let i=WORLD_ZONES.length-1;i>=0;i--)if(height>=WORLD_ZONES[i].min)return WORLD_ZONES[i];return WORLD_ZONES[0]}
