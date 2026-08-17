import { createServer } from 'node:http';
import { readFile, stat } from 'node:fs/promises';
import { extname, join, normalize } from 'node:path';

const root = process.cwd();
const types = { '.html': 'text/html', '.js': 'text/javascript', '.css': 'text/css', '.json': 'application/json' };
const server = createServer(async (req, res) => {
  try {
    const raw = decodeURIComponent((req.url ?? '/').split('?')[0]);
    const relative = raw === '/' ? 'index.html' : raw.replace(/^\/+/, '');
    const file = normalize(join(root, relative));
    if (!file.startsWith(root)) throw new Error('Invalid path');
    const info = await stat(file);
    const target = info.isDirectory() ? join(file, 'index.html') : file;
    res.writeHead(200, { 'Content-Type': types[extname(target)] ?? 'application/octet-stream' });
    res.end(await readFile(target));
  } catch {
    res.writeHead(404, { 'Content-Type': 'text/plain' });
    res.end('Not found');
  }
});
server.listen(4173, '127.0.0.1', () => console.log('Fly Like an Eagle: http://127.0.0.1:4173'));
