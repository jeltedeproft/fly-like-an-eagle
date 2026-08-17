export const PALETTE={ink:'#201A1E',coal:'#34272B',iron:'#514047',ash:'#76616A',cream:'#F5E3B5',paper:'#D9BE8B',sky:'#72A7A5',skyDark:'#497A7B',cloud:'#C8D3BF',moss:'#677A4A',grass:'#8E9A50',rust:'#A94F32',rustDark:'#71352B',gold:'#E2A53B',sun:'#F4CF62',red:'#D95B43'};
export const STEP=1/60;
export const UPGRADES=[
 {id:'launch',name:'Hotter Wheels',description:'A small speed tune for the ramp.',baseCost:35,max:5,effect:l=>`${Math.round(l*3)}% launch`},
 {id:'aero',name:'Tin Wings',description:'A small reduction in air resistance.',baseCost:45,max:5,effect:l=>`${Math.round(l*3)}% glide`},
 {id:'control',name:'Steering Rope',description:'A little more authority while airborne.',baseCost:30,max:5,effect:l=>`${Math.round(l*4)}% control`},
 {id:'durability',name:'Soft-ish Seat',description:'A little more forgiveness on landing.',baseCost:40,max:5,effect:l=>`${Math.round(l*4)}% toughness`},
 {id:'boost',name:'Soda Pressure',description:'A small fuel top-up for installed rockets.',baseCost:55,max:5,effect:l=>`${(l*.12).toFixed(2)}s fuel`}
];
export const PARTS=[
 {id:'cage',name:'Golden Roll Cage',description:'A chunky safety cage. Survive much harder landings.',cost:75,effect:'Major landing protection'},
 {id:'nose',name:'Cream Nose Cone',description:'A heroic pointy front. Charge off the ramp.',cost:105,effect:'+20% launch speed'},
 {id:'tailwing',name:'Moss Tail Wing',description:'A giant scrapyard spoiler. Command the air.',cost:135,effect:'+25% air control'},
 {id:'rocketpack',name:'Twin Soda Rockets',description:'Two very visible tanks with serious thrust time.',cost:180,effect:'+1.40s boost fuel'}
];
export const upgradeCost=(upgrade,level)=>Math.round(upgrade.baseCost*(1+level*.72+level*level*.18));
export const defaultProgress=()=>({schemaVersion:2,bolts:0,best:0,levels:Object.fromEntries(UPGRADES.map(u=>[u.id,0])),parts:Object.fromEntries(PARTS.map(p=>[p.id,false])),reducedMotion:false,musicVolume:.55,sfxVolume:.8,runs:0});
