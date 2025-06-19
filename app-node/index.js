const http = require('http');
const PORT = 3000;
http.createServer((req, res) => {
  res.end('Hola desde Node.js');
}).listen(PORT);
