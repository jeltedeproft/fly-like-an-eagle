import{STEP}from'./config.js';
const clamp=(v,a,b)=>Math.max(a,Math.min(b,v));
export function groundAt(x){if(x<-25)return 22;if(x<0){const t=(x+25)/25;return 22-18*t+Math.sin(t*Math.PI)*-2.2}return Math.sin(x*.045)*.65+Math.sin(x*.013)*.3;}
export class Simulation{
 constructor(levels={}){this.levels=levels;this.reset()}
 reset(){this.state='ready';this.x=-23;this.y=groundAt(this.x)+1;this.vx=0;this.vy=0;this.angle=-.52;this.spin=0;this.maxDistance=0;this.grounded=true;this.rest=0;this.boost=(this.levels.boost||0)*.35;this.result=null;this.particles=[]}
 launch(){if(this.state!=='ready')return;this.state='ramp';this.vx=7.4*(1+(this.levels.launch||0)*.08)}
 step(input={},dt=STEP){if(!['ramp','air','ground'].includes(this.state))return;const sub=Math.min(.05,dt);if(this.state==='ramp'){const slope=(groundAt(this.x+.05)-groundAt(this.x-.05))/.1;this.vx+=(-slope*11.5+1.4)*sub;this.x+=this.vx*sub;this.y=groundAt(this.x)+1;this.angle=Math.atan(slope);if(this.x>=0){this.state='air';this.grounded=false;this.vy=Math.max(7.8,this.vx*.43);this.spin=-.2}}
 else if(this.state==='air'){const control=(2.5+(this.levels.control||0)*.34);this.spin+=(Number(Boolean(input.left))-Number(Boolean(input.right)))*control*sub;this.spin*=Math.pow(.985,sub/STEP);this.angle+=this.spin*sub;let thrust=0;if(input.boost&&this.boost>0){thrust=5.5;this.boost=Math.max(0,this.boost-sub)}const aero=(this.levels.aero||0)*.012;this.vx+=(thrust-this.vx*(.018-aero*.45))*sub;this.vy+=(-9.2-this.vy*(.018+aero))*sub;this.x+=this.vx*sub;this.y+=this.vy*sub;this.maxDistance=Math.max(this.maxDistance,this.x);if(this.y<=groundAt(this.x)+.8)this.land()}
 else{this.vx*=Math.pow(.972,sub/STEP);this.x+=this.vx*sub;this.y=groundAt(this.x)+.8;this.angle*=.92;this.maxDistance=Math.max(this.maxDistance,this.x);this.rest=this.vx<.45?this.rest+sub:0;if(this.rest>.55)this.finish()}
 if(this.x>2500||this.y<-60||!Number.isFinite(this.x+this.y))this.finish('bounds')}
 land(){const angle=Math.abs(Math.atan2(Math.sin(this.angle),Math.cos(this.angle)));const impact=Math.abs(this.vy);const tolerance=.42+(this.levels.durability||0)*.055;this.landing=angle<tolerance&&impact<13?'clean':angle<tolerance*1.8?'rough':'crash';this.state='ground';const keep=this.landing==='clean'?.72:this.landing==='rough'?.45:.2+(this.levels.durability||0)*.035;this.vx=Math.max(0,this.vx*keep);this.vy=0;this.grounded=true;this.rest=0}
 finish(reason='stopped'){if(this.state==='complete')return;this.state='complete';this.result={distance:Math.max(0,this.maxDistance),landing:this.landing||'crash',reason}}
 get speed(){return Math.hypot(this.vx,this.vy)}
}
