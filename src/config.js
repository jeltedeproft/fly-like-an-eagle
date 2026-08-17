export const PALETTE={ink:'#201A1E',coal:'#34272B',iron:'#514047',ash:'#76616A',cream:'#F5E3B5',paper:'#D9BE8B',sky:'#72A7A5',skyDark:'#497A7B',cloud:'#C8D3BF',moss:'#677A4A',grass:'#8E9A50',rust:'#A94F32',rustDark:'#71352B',gold:'#E2A53B',sun:'#F4CF62',red:'#D95B43'};
export const STEP=1/60;
export const UPGRADES=[
 {id:'launch',name:'Hotter Wheels',description:'More speed off the ramp.',baseCost:35,max:5,effect:l=>`${Math.round(l*8)}% launch`},
 {id:'aero',name:'Tin Wings',description:'Lose less speed in the air.',baseCost:45,max:5,effect:l=>`${Math.round(l*9)}% glide`},
 {id:'control',name:'Steering Rope',description:'Lean faster while airborne.',baseCost:30,max:5,effect:l=>`${Math.round(l*12)}% control`},
 {id:'durability',name:'Soft-ish Seat',description:'Keep sliding after rough landings.',baseCost:40,max:5,effect:l=>`${Math.round(l*10)}% toughness`},
 {id:'boost',name:'Soda Rockets',description:'Hold Space in flight for a shove.',baseCost:55,max:5,effect:l=>`${(l*.35).toFixed(2)}s fuel`}
];
export const upgradeCost=(upgrade,level)=>Math.round(upgrade.baseCost*(1+level*.72+level*level*.18));
export const defaultProgress=()=>({schemaVersion:1,bolts:0,best:0,levels:Object.fromEntries(UPGRADES.map(u=>[u.id,0])),reducedMotion:false,runs:0});
