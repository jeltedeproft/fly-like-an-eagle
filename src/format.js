const SUFFIXES=['','K','M','B','T','Qa','Qi','Sx','Sp','Oc','No','Dc'];

export function formatNumber(value,{digits=2}={}){
 const n=Number(value);if(!Number.isFinite(n))return'0';const sign=n<0?'-':'',absolute=Math.abs(n);if(absolute<1000)return sign+Math.floor(absolute).toLocaleString('en-US');
 const tier=Math.floor(Math.log10(absolute)/3);if(tier>=SUFFIXES.length)return`${sign}${absolute.toExponential(2)}`;
 const scaled=absolute/1000**tier,precision=scaled>=100?0:scaled>=10?1:digits;
 return`${sign}${scaled.toFixed(precision).replace(/\.0+$|(?<=\.[0-9])0+$/,'')}${SUFFIXES[tier]}`;
}

export function formatDistance(meters){const n=Math.max(0,Number(meters)||0);if(n<1000)return`${formatNumber(n)} m`;return`${formatNumber(n/1000)} km`}
export function formatSpeed(kmh){const n=Math.max(0,Number(kmh)||0);if(n<1225)return`${formatNumber(n)} km/h`;return`Mach ${formatNumber(n/1225,{digits:1})}`}
export const formatBolts=value=>`${formatNumber(value)} BOLTS`;
