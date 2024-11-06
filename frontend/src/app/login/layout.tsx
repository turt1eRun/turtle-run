import type {Metadata} from "next";
import "../../styles/globals.css";
import Sidebar from "@/components/common/Sidebar";
import React from "react";
import Header from "@/components/common/Header";


export const metadata: Metadata = {
  title: "로그인 - 터틀런",
  description: "디지털 교육 컨텐츠 플랫폼",
};

export default function MainLayout({
                                     children,
                                   }: Readonly<{
  children: React.ReactNode;
}>) {
  return (
      <>
        <main>
          {children}
        </main>
      </>

  )
      ;
}