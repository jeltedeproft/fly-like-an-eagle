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
export const FACILITY_UPGRADES=[
 {id:'ramp',name:'Launch Facility',names:['Rotten Plank Ramp','Reinforced Ski Ramp','Scrapyard Launch Tower','Rocket Rail','Mountain Cannon','Orbital Railgun','Lunar Slingshot','Reality Catapult'],description:'Replace the entire launch site with increasingly unreasonable engineering.',baseCost:160,max:7,effect:l=>`${['WOOD','STEEL','TOWER','ROCKET','MOUNTAIN','ORBITAL','LUNAR','REALITY'][l]} CLASS · launch +${l*8}%`}
];
export const VEHICLES=[
 {id:'cart',generation:1,name:'Rustbucket Cart',description:'Barely a vehicle. Mostly rust, optimism, and four suspicious wheels.',strength:'GEN 1 · SCRAP POWER',weakness:'STARTER',rewardScale:1,stats:{launch:.78,drag:1.15,control:.82,tolerance:-.04,slide:0}},
 {id:'bathtub',generation:2,name:'Bathtub Bullet',description:'Porcelain engineering makes the first real leap forward.',strength:'GEN 2 · SUPERSONIC BATH',weakness:'UNLOCK 140 m',rewardScale:2,stats:{launch:1.4,drag:.72,control:1.05,tolerance:.1,slide:.08},unlock:'distance-150'},
 {id:'coffin',generation:3,name:'Rocket Coffin',description:'A deeply irresponsible rocket sled built for serious distance.',strength:'GEN 3 · HYPER ROCKET',weakness:'UNLOCK 1 km',rewardScale:6,stats:{launch:4,drag:.28,control:1.25,tolerance:.16,slide:.14},unlock:'distance-1000'},
 {id:'scrapstar',generation:4,name:'Scrapstar UFO',description:'The junkyard has achieved spaceflight. Nobody knows how.',strength:'GEN 4 · INTERPLANETARY',weakness:'UNLOCK 6 km',rewardScale:20,stats:{launch:12,drag:.005,control:1.55,tolerance:.25,slide:.22},unlock:'distance-6000'}
];
export const MILESTONES=[
 {id:'distance-50',name:'Cleared the Junkyard',description:'Travel 50 metres in one run.',reward:30,test:({result})=>result.distance>=50},
 {id:'clean-landing',name:'Any Landing You Walk From',description:'Make your first clean landing.',reward:45,test:({result})=>result.landing==='clean'},
 {id:'distance-150',name:'Porcelain Pioneer',description:'Travel 140 metres and unlock the Bathtub Bullet.',reward:75,unlockVehicle:'bathtub',test:({result})=>result.distance>=140},
 {id:'distance-200',name:'One Foot in the Grave',description:'Travel 1 kilometre and unlock the Rocket Coffin.',reward:1200,unlockVehicle:'coffin',test:({result,progress})=>progress.unlockedVehicles.includes('bathtub')&&result.distance>=1000},
 {id:'distance-310',name:'Junkyard Space Program',description:'Travel 6 kilometres and unlock the Scrapstar UFO.',reward:30000,unlockVehicle:'scrapstar',test:({result,progress})=>progress.unlockedVehicles.includes('coffin')&&result.distance>=6000},
 {id:'runs-10',name:'Frequent Flier',description:'Complete 10 runs.',reward:100,test:({progress})=>progress.runs>=10}
];
export const POWER_SYSTEMS={
 cart:{name:'Hamster Overdrive',description:'A determined wheel-driven reactor bolted to the back.',baseCost:900,max:3,multiplier:1.55},
 bathtub:{name:'Boiler Jets',description:'Weaponised plumbing turns bathwater into thrust.',baseCost:6500,max:4,multiplier:1.75},
 coffin:{name:'Nuclear Afterburner',description:'Definitely not approved for funeral use.',baseCost:80000,max:5,multiplier:2.05},
 scrapstar:{name:'Antimatter Warp Core',description:'Folds the road, sky, and common sense.',baseCost:1500000,max:6,multiplier:2.5}
};
export const powerCost=(system,level)=>Math.round(system.baseCost*Math.pow(7,level));
export const upgradeCost=(upgrade,level)=>Math.round(upgrade.baseCost*(1+level*.72+level*level*.18));
export const defaultVehicleBuild=()=>({levels:Object.fromEntries(UPGRADES.map(u=>[u.id,0])),parts:Object.fromEntries(PARTS.map(p=>[p.id,false])),powerLevel:0});
export const defaultProgress=()=>({schemaVersion:7,bolts:0,best:0,prestige:{genius:0},vehicleProgress:Object.fromEntries(VEHICLES.map(v=>[v.id,defaultVehicleBuild()])),facilities:Object.fromEntries(FACILITY_UPGRADES.map(f=>[f.id,0])),claimedMilestones:[],unlockedVehicles:['cart'],selectedVehicle:'cart',reducedMotion:false,musicVolume:.55,sfxVolume:.8,runs:0});
