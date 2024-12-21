import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  /* config options here */
  output: 'export',
  images: {
    unoptimized: true,
  }
};

//개발환경에서만 설정
module.exports = {
  async rewrites() {
    return [
      {
        source: '/api/:path*', // 프론트엔드에서 호출하는 경로
        destination: 'http://localhost:8080/api/:path*', // 백엔드 서버 주소
      },
    ];
  },
};

export default nextConfig;
