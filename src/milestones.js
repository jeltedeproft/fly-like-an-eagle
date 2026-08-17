import{MILESTONES,VEHICLES}from'./config.js';

export function evaluateMilestones(progress,result){
 const claimed=new Set(progress.claimedMilestones||[]),unlocked=new Set(progress.unlockedVehicles||['cart']),earned=[];
 let reward=0;
 for(const milestone of MILESTONES){
  if(claimed.has(milestone.id)||!milestone.test({progress,result}))continue;
  claimed.add(milestone.id);reward+=milestone.reward||0;
  if(milestone.unlockVehicle&&VEHICLES.some(v=>v.id===milestone.unlockVehicle))unlocked.add(milestone.unlockVehicle);
  earned.push(milestone);
 }
 return{earned,progress:{...progress,bolts:progress.bolts+reward,claimedMilestones:[...claimed],unlockedVehicles:[...unlocked]}};
}
