const{app,BrowserWindow,shell}=require('electron');
const{createServer}=require('node:http');
const{readFile,stat}=require('node:fs/promises');
const{extname,join,normalize}=require('node:path');

const PORT=41737,root=__dirname,types={'.html':'text/html; charset=utf-8','.js':'text/javascript; charset=utf-8','.css':'text/css; charset=utf-8','.json':'application/json; charset=utf-8','.webmanifest':'application/manifest+json; charset=utf-8','.png':'image/png','.mp3':'audio/mpeg','.wav':'audio/wav'};
let server,window;

function serve(){return new Promise((resolve,reject)=>{server=createServer(async(req,res)=>{try{const raw=decodeURIComponent((req.url||'/').split('?')[0]),relative=raw==='/'?'index.html':raw.replace(/^\/+/,''),file=normalize(join(root,relative));if(!file.startsWith(root))throw Error('Invalid path');const info=await stat(file),target=info.isDirectory()?join(file,'index.html'):file;res.writeHead(200,{'Content-Type':types[extname(target)]||'application/octet-stream','Cache-Control':'no-cache'});res.end(await readFile(target))}catch{res.writeHead(404,{'Content-Type':'text/plain'});res.end('Not found')}});server.once('error',reject);server.listen(PORT,'127.0.0.1',resolve)})}

async function createWindow(){await serve();window=new BrowserWindow({title:'Fly Like an Eagle',width:1280,height:720,minWidth:960,minHeight:540,backgroundColor:'#201A1E',autoHideMenuBar:true,icon:join(root,'assets','icons','app.ico'),webPreferences:{contextIsolation:true,nodeIntegration:false,sandbox:true}});window.setMenuBarVisibility(false);window.webContents.setWindowOpenHandler(({url})=>{if(/^https?:/.test(url))shell.openExternal(url);return{action:'deny'}});window.webContents.on('will-navigate',(event,url)=>{if(!url.startsWith(`http://127.0.0.1:${PORT}/`))event.preventDefault()});window.webContents.on('before-input-event',(event,input)=>{if(input.type==='keyDown'&&input.key==='F11'){window.setFullScreen(!window.isFullScreen());event.preventDefault()}if(input.type==='keyDown'&&input.key==='Escape'&&window.isFullScreen()){window.setFullScreen(false);event.preventDefault()}});await window.loadURL(`http://127.0.0.1:${PORT}/`)}

if(!app.requestSingleInstanceLock())app.quit();else{app.on('second-instance',()=>{if(window){if(window.isMinimized())window.restore();window.focus()}});app.whenReady().then(createWindow);app.on('window-all-closed',()=>app.quit());app.on('before-quit',()=>server?.close())}
