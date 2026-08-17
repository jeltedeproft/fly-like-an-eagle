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
export const VEHICLES=[
 {id:'cart',name:'Rustbucket Cart',description:'Quick off the hill and easy to wrestle around, but flimsy in flight.',strength:'FAST LAUNCH · SHARP CONTROL',weakness:'WEAK GLIDE · FRAGILE',stats:{launch:1,drag:1,control:1,tolerance:0,slide:0}},
 {id:'bathtub',name:'Bathtub Bullet',description:'Heavy porcelain glides and lands beautifully, but starts slowly and turns like a bath.',strength:'STRONG GLIDE · TOUGH LANDING',weakness:'SLOW LAUNCH · HEAVY CONTROL',stats:{launch:.82,drag:.62,control:.76,tolerance:.14,slide:.1},unlock:'distance-150'}
];
export const MILESTONES=[
 {id:'distance-50',name:'Cleared the Junkyard',description:'Travel 50 metres in one run.',reward:30,test:({result})=>result.distance>=50},
 {id:'clean-landing',name:'Any Landing You Walk From',description:'Make your first clean landing.',reward:45,test:({result})=>result.landing==='clean'},
 {id:'distance-150',name:'Porcelain Pioneer',description:'Travel 150 metres and unlock the Bathtub Bullet.',reward:75,unlockVehicle:'bathtub',test:({result})=>result.distance>=150},
 {id:'runs-10',name:'Frequent Flier',description:'Complete 10 runs.',reward:100,test:({progress})=>progress.runs>=10}
];
export const upgradeCost=(upgrade,level)=>Math.round(upgrade.baseCost*(1+level*.72+level*level*.18));
export const defaultProgress=()=>({schemaVersion:3,bolts:0,best:0,levels:Object.fromEntries(UPGRADES.map(u=>[u.id,0])),parts:Object.fromEntries(PARTS.map(p=>[p.id,false])),claimedMilestones:[],unlockedVehicles:['cart'],selectedVehicle:'cart',reducedMotion:false,musicVolume:.55,sfxVolume:.8,runs:0});
