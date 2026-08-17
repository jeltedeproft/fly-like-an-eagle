import{STEP,VEHICLES}from'./config.js';
const clamp=(v,a,b)=>Math.max(a,Math.min(b,v));
export const RAMP_END=14;
export function groundAt(x,rampLevel=0){
 // A ski-jump silhouette: high straight inrun, concave transition, rising takeoff table.
 if(x<-48)return 62;
 if(x<-18){const t=(x+48)/30;return 62-45*t}
 if(x<RAMP_END){const t=(x+18)/32,t2=t*t,t3=t2*t,end=15+rampLevel*2.4,tangent=13.4+rampLevel*2.8;return(2*t3-3*t2+1)*17+(t3-2*t2+t)*-48+(-2*t3+3*t2)*end+(t3-t2)*tangent}
 return Math.sin(x*.045)*.65+Math.sin(x*.013)*.3;
}
export class Simulation{
 constructor(levels={},parts={},vehicleId='cart',facilities={}){this.levels=levels;this.parts=parts;this.vehicle=VEHICLES.find(v=>v.id===vehicleId)||VEHICLES[0];this.facilities=facilities;this.reset()}
 reset(){this.state='ready';this.x=-64;this.y=groundAt(this.x,this.facilities.ramp||0)+1;this.vx=0;this.vy=0;this.angle=0;this.spin=0;this.maxDistance=0;this.grounded=true;this.rest=0;this.goTimer=0;this.boost=(this.levels.boost||0)*.12+(this.parts.rocketpack?1.4:0);this.result=null;this.particles=[]}
 launch(){if(this.state!=='ready')return;this.state='starting';this.startTimer=3}
 step(input={},dt=STEP){if(!['starting','ramp','air','ground'].includes(this.state))return;const sub=Math.min(.05,dt),stats=this.vehicle.stats,ramp=this.facilities.ramp||0,terrain=x=>groundAt(x,ramp);if(this.state==='starting'){this.startTimer=Math.max(0,this.startTimer-sub);if(this.startTimer<=1e-6){this.startTimer=0;this.state='ramp';this.goTimer=.6;this.vx=7.4*stats.launch*(1+(this.levels.launch||0)*.03)*(this.parts.nose?1.2:1)}}else if(this.state==='ramp'){this.goTimer=Math.max(0,this.goTimer-sub);const slope=(terrain(this.x+.05)-terrain(this.x-.05))/.1;this.vx+=(-slope*11.5+1.4)*sub;const nextX=this.x+this.vx*sub;if(nextX>=RAMP_END){this.x=RAMP_END;this.y=terrain(RAMP_END-.001)+1;this.angle=Math.atan(.42+ramp*.085);this.state='air';this.grounded=false;this.vx*=stats.launch*(1+ramp*.06);this.vy=Math.max(13.5+ramp*1.4,this.vx*(.44+ramp*.018));this.spin=-.08}else{this.x=nextX;this.y=terrain(this.x)+1;this.angle=Math.atan(slope)}}
 else if(this.state==='air'){const control=(2.5+(this.levels.control||0)*.1)*stats.control*(this.parts.tailwing?1.25:1);this.spin+=(Number(Boolean(input.left))-Number(Boolean(input.right)))*control*sub;this.spin*=Math.pow(.985,sub/STEP);this.angle+=this.spin*sub;let thrust=0;if(input.boost&&this.boost>0){thrust=5.5;this.boost=Math.max(0,this.boost-sub)}const aero=(this.levels.aero||0)*.004;this.vx+=(thrust-this.vx*(.018-aero*.45)*stats.drag)*sub;this.vy+=(-9.2-this.vy*(.018+aero)*stats.drag)*sub;this.x+=this.vx*sub;this.y+=this.vy*sub;this.maxDistance=Math.max(this.maxDistance,this.x-RAMP_END);if(this.y<=terrain(this.x)+.8)this.land()}
 else{this.vx*=Math.pow(.972,sub/STEP);this.x+=this.vx*sub;this.y=terrain(this.x)+.8;this.angle*=.92;this.maxDistance=Math.max(this.maxDistance,this.x-RAMP_END);this.rest=this.vx<.45?this.rest+sub:0;if(this.rest>.55)this.finish()}
 if(this.x>2500||this.y<-60||!Number.isFinite(this.x+this.y))this.finish('bounds')}
 land(){const angle=Math.abs(Math.atan2(Math.sin(this.angle),Math.cos(this.angle)));const impact=Math.abs(this.vy),stats=this.vehicle.stats;const tolerance=.42+stats.tolerance+(this.levels.durability||0)*.02+(this.parts.cage?.2:0);this.landing=angle<tolerance&&impact<(this.parts.cage?16:13)?'clean':angle<tolerance*1.8?'rough':'crash';this.state='ground';const keep=this.landing==='clean'?.72:this.landing==='rough'?.45:.2+stats.slide+(this.levels.durability||0)*.014+(this.parts.cage?.12:0);this.vx=Math.max(0,this.vx*keep);this.vy=0;this.grounded=true;this.rest=0}
 finish(reason='stopped'){if(this.state==='complete')return;this.state='complete';this.result={distance:Math.max(0,this.maxDistance),landing:this.landing||'crash',reason}}
 get speed(){return Math.hypot(this.vx,this.vy)}
}
