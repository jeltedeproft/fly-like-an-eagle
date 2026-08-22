import{cp,mkdir,rm,writeFile}from'node:fs/promises';
import{join,resolve,sep}from'node:path';
import{packager}from'@electron/packager';

const root=process.cwd(),stage=resolve(root,'build','windows-app'),output=resolve(root,'dist','windows');
function safe(path){if(!path.startsWith(resolve(root)+sep))throw Error(`Unsafe build path: ${path}`);return path}
await rm(safe(stage),{recursive:true,force:true});await rm(safe(output),{recursive:true,force:true});await mkdir(stage,{recursive:true});
for(const file of['index.html','manifest.webmanifest','sw.js'])await cp(join(root,file),join(stage,file));
for(const folder of['src','assets/icons','assets/audio/normalized'])await cp(join(root,folder),join(stage,folder),{recursive:true});
await mkdir(join(stage,'assets/audio/sfx'),{recursive:true});for(const name of['Car Brake Screech_2.wav','Car Brake Screech_3.wav','Car Brake Screech_4.wav'])await cp(join(root,'assets/audio/sfx',name),join(stage,'assets/audio/sfx',name));
await cp(join(root,'desktop/main.cjs'),join(stage,'main.cjs'));await writeFile(join(stage,'package.json'),JSON.stringify({name:'fly-like-an-eagle',productName:'Fly Like an Eagle',version:'0.2.0',main:'main.cjs'},null,2));
const paths=await packager({dir:stage,out:output,platform:'win32',arch:'x64',name:'Fly Like an Eagle',executableName:'Fly Like an Eagle',appVersion:'0.2.0',buildVersion:'0.2.0',icon:join(root,'assets/icons/app.ico'),asar:true,overwrite:true,prune:true,win32metadata:{CompanyName:'jeltedeproft',FileDescription:'Fly Like an Eagle',OriginalFilename:'Fly Like an Eagle.exe',ProductName:'Fly Like an Eagle',InternalName:'Fly Like an Eagle'}});
console.log(paths.join('\n'));
