import httpProxy from "http-proxy";
import {NextApiRequest, NextApiResponse} from "next";

export const config = {
  api: {
    // Enable `externalResolver` option in Next.js
    externalResolver: true,
    bodyParser: false,
  },
};

export default (req: NextApiRequest, res: NextApiResponse) =>
  new Promise((resolve, reject) => {
    const proxy: httpProxy = httpProxy.createProxy();
    req.url = req.url?.replace(/^\/api/, "")
    proxy.once("proxyRes", resolve).once("error", reject).web(req, res, {
      changeOrigin: true,
      target: 'http://127.0.0.1:8080',
    });
  });
