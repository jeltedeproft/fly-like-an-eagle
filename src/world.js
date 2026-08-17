export const WORLD_ZONES=[
 {id:'country',name:'COUNTRYSIDE',min:0},
 {id:'clouds',name:'CLOUD LAYER',min:80},
 {id:'stratosphere',name:'STRATOSPHERE',min:500},
 {id:'orbit',name:'LOW ORBIT',min:5000},
 {id:'space',name:'DEEP SPACE',min:50000}
];
export function worldZone(altitude){const height=Math.max(0,Number(altitude)||0);for(let i=WORLD_ZONES.length-1;i>=0;i--)if(height>=WORLD_ZONES[i].min)return WORLD_ZONES[i];return WORLD_ZONES[0]}
