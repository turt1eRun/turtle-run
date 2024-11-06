import type {Metadata} from "next";
import "../styles/globals.css";
import { pretendard } from "../lib/fonts";
import Header from "@/components/common/Header";


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
        {/*<header>*/}
        {/*  <Header/>*/}
        {/*</header>*/}
        {/*<main>*/}
        {/*  {children}*/}
        {/*</main>*/}
        {/*<footer>*/}

        {/*</footer>*/}
        {children}
      </body>
    </html>
  );
}
