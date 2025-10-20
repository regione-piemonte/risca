var PROXY_CONF = {
  '/riscaboweb': {
    target: 'http://localhost:8080',
    secure: false,
    logLevel: 'debug',
    configure: (proxy, _options) => {
      proxy.on("error", (err, _req, _res) => {
        console.log("proxy error", err);
      });
      proxy.on("proxyReq", (proxyReq, req, _res) => {
        const headers = proxyReq.getHeaders();
        // console.log(headers);
        // console.log(
        //   req.method,
        //   req.url,
        //   " -> ",
        //   `${headers.host}${proxyReq.path}`,
        // );
      });
      proxy.on("proxyRes", (proxyRes, req, _res) => {
        // console.log(
        //   req.method,
        //   "Target Response",
        //   proxyRes.statusCode,
        //   ":",
        //   req.url,
        // );
      });
    },
    changeOrigin: true,
    pathRewrite: {
      '^/': ''
    }
  }
}

module.exports = PROXY_CONF;