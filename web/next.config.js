/** @type {import('next').NextConfig} */
const nextConfig = {
    reactStrictMode: true,
    async rewrites() {
        return [
            {
                source: '/api/:path*',
                destination: 'http://127.0.0.1:8080/:path*' // Proxy to Backend
            }
        ]
    }
}

module.exports = nextConfig
