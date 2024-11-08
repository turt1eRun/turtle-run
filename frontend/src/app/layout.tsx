import type {Metadata} from "next";
import "../styles/globals.css";
import {pretendard} from "../lib/fonts";


export const metadata: Metadata = {
  title: "터틀런",
  description: "디지털 교육 컨텐츠 플랫폼",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body className={pretendard.className}>
        {children}
      </body>
    </html>
  );
}
